package com.github.daniellfalcao.test.data.user

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.daniellfalcao.common.ParrotException
import com.github.daniellfalcao.data.user.repository.UserRemoteDataSource
import com.github.daniellfalcao.domain.user.model.UserDTO
import com.github.daniellfalcao.test._module.RepositoryTest
import com.github.daniellfalcao.test._module.mock.user1
import com.google.protobuf.Empty
import com.proto.parrot.service.authentication.AuthenticationServiceGrpcKt.AuthenticationServiceCoroutineStub
import com.proto.parrot.service.authentication.CheckUsernameAvailabilityRequest
import com.proto.parrot.service.authentication.CheckUsernameAvailabilityResponse
import com.proto.parrot.service.authentication.RegisterServiceGrpcKt.RegisterServiceCoroutineStub
import com.proto.parrot.service.authentication.SignInRequest
import com.proto.parrot.service.authentication.SignInResponse
import com.proto.parrot.service.authentication.SignUpRequest
import com.proto.parrot.service.user.ProfileResponse
import com.proto.parrot.service.user.User
import com.proto.parrot.service.user.UserServiceGrpcKt.UserServiceCoroutineStub
import io.grpc.Status
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE, sdk = [28])
class UserRemoteDataSourceTest : RepositoryTest() {

    private fun remote(
        userService: UserServiceCoroutineStub = mock(),
        registerService: RegisterServiceCoroutineStub = mock(),
        authenticationService: AuthenticationServiceCoroutineStub = mock()
    ) = UserRemoteDataSource(userService, registerService, authenticationService)

    @Test
    fun `test request username successful`() = runBlocking {
        // given
        val request = CheckUsernameAvailabilityRequest.newBuilder()
            .setUsername("test")
            .build()
        val response = CheckUsernameAvailabilityResponse.newBuilder()
            .setIsAvailable(true)
            .build()
        val service = mock<RegisterServiceCoroutineStub> {
            onBlocking { checkUsernameAvailability(request) } doReturn response
        }
        val userRemoteDataSource = remote(registerService = service)
        // when
        val result = userRemoteDataSource.requestUsernameAvailability("test")
        // then
        Assert.assertTrue(result.getOrNull()!!.isAvailable)
    }

    @Test
    fun `test request username failed (unknown)`() = runBlocking {
        // given
        val request = CheckUsernameAvailabilityRequest.newBuilder()
            .setUsername("test")
            .build()
        val service = mock<RegisterServiceCoroutineStub> {
            onBlocking { checkUsernameAvailability(request) } doThrow StatusRuntimeException(Status.UNKNOWN)
        }
        val userRemoteDataSource = remote(registerService = service)
        // when
        val result = userRemoteDataSource.requestUsernameAvailability("test")
        // then
        Assert.assertTrue(result.exceptionOrNull() != null)
    }

    @Test
    fun `test request profile successful`() = runBlocking {
        // given
        val response1 = ProfileResponse.newBuilder()
            .setUser(user1.build())
            .build()
        val response2 = ProfileResponse.newBuilder()
            .setUser(user1.setLikes(6).build())
            .build()
        val service = mock<UserServiceCoroutineStub> {
            onBlocking { profile(Empty.getDefaultInstance()) } doReturn flow {
                emit(response1)
                emit(response2)
            }
        }
        val userRemoteDataSource = remote(userService = service)
        // when
        var lastResponse: User? = null
        userRemoteDataSource.requestProfile().collect { lastResponse = it }
        // then
        Assert.assertTrue(lastResponse!! == response2.user)
    }

    @Test(expected = ParrotException::class)
    fun `test request profile failure (unknown)`() = runBlocking {
        // given
        val response1 = ProfileResponse.newBuilder()
            .setUser(user1.build())
            .build()
        val response2 = ProfileResponse.newBuilder()
            .setUser(user1.setLikes(6).build())
            .build()
        val service = mock<UserServiceCoroutineStub> {
            onBlocking { profile(Empty.getDefaultInstance()) } doReturn flow {
                emit(response1)
                emit(response2)
                throw StatusRuntimeException(Status.UNKNOWN)
            }
        }
        val userRemoteDataSource = remote(userService = service)
        // when
        userRemoteDataSource.requestProfile().catch { error ->
            throw error
        }.collect()
    }

    @Test
    fun `test request sign in successful`() = runBlocking {
        // given
        val user = user1.build()
        val request = SignInRequest.newBuilder()
            .setUsername(user.username)
            .setPassword("123")
            .build()
        val response = SignInResponse.newBuilder()
            .setToken("token-123")
            .setUser(user)
            .build()
        val service = mock<AuthenticationServiceCoroutineStub>() {
            onBlocking { signIn(request) } doReturn response
        }
        val userRemoteDataSource = remote(authenticationService = service)
        // when
        val result = userRemoteDataSource.requestSignIn(user.username, "123")
        // then
        Assert.assertTrue(result.getOrNull()!!.token == response.token)
        Assert.assertTrue(result.getOrNull()!!.user == user)
    }

    @Test
    fun `test request sign in failure (invalid user or password)`() = runBlocking {
        // given
        val user = user1.build()
        val request = SignInRequest.newBuilder()
            .setUsername(user.username)
            .setPassword("123")
            .build()
        val service = mock<AuthenticationServiceCoroutineStub> {
            onBlocking { signIn(request) } doThrow StatusRuntimeException(Status.NOT_FOUND)
        }
        val userRemoteDataSource = remote(authenticationService = service)
        // when
        val result = userRemoteDataSource.requestSignIn(user.username, "123")
        // then
        Assert.assertTrue((result.exceptionOrNull()!!.cause!! as StatusRuntimeException).status == Status.NOT_FOUND)
        Assert.assertThrows(StatusRuntimeException::class.java) {
            throw result.exceptionOrNull()!!.cause!!
        }
        Unit
    }

    @Test
    fun `test request sign in failure (unknown)`() = runBlocking {
        // given
        val user = user1.build()
        val request = SignInRequest.newBuilder()
            .setUsername(user.username)
            .setPassword("123")
            .build()
        val service = mock<AuthenticationServiceCoroutineStub> {
            onBlocking { signIn(request) } doThrow StatusRuntimeException(Status.UNKNOWN)
        }
        val userRemoteDataSource = remote(authenticationService = service)
        // when
        val result = userRemoteDataSource.requestSignIn(user.username, "123")
        // then
        Assert.assertTrue((result.exceptionOrNull()!!.cause!! as StatusRuntimeException).status == Status.UNKNOWN)
        Assert.assertThrows(StatusRuntimeException::class.java) {
            throw result.exceptionOrNull()!!.cause!!
        }
        Unit
    }

    @Test
    fun `test request sign up success`() = runBlocking {
        // given
        val request = SignUpRequest.newBuilder()
            .setUsername("test")
            .setPassword("123")
            .setBirthday("15/12/1995")
            .setParrot(UserDTO.Parrot.BLUE.name)
            .build()
        val service = mock<RegisterServiceCoroutineStub> {
            onBlocking { signUp(request) } doReturn Empty.getDefaultInstance()
        }
        val userRemoteDataSource = remote(registerService = service)
        // when
        val result = userRemoteDataSource.requestSignUp(
            request.username, request.password, request.birthday, request.parrot
        )
        // then
        Assert.assertTrue(result.isSuccess)
    }

    @Test
    fun `test request sign up failure (user already exists)`() = runBlocking {
        // given
        val request = SignUpRequest.newBuilder()
            .setUsername("test")
            .setPassword("123")
            .setBirthday("15/12/1995")
            .setParrot(UserDTO.Parrot.BLUE.name)
            .build()
        val service = mock<RegisterServiceCoroutineStub> {
            onBlocking { signUp(request) } doThrow StatusRuntimeException(Status.ALREADY_EXISTS)
        }
        val userRemoteDataSource = remote(registerService = service)
        // when
        val result = userRemoteDataSource.requestSignUp(
            request.username, request.password, request.birthday, request.parrot
        )
        // then
        Assert.assertTrue((result.exceptionOrNull()!!.cause!! as StatusRuntimeException).status == Status.ALREADY_EXISTS)
        Assert.assertThrows(StatusRuntimeException::class.java) {
            throw result.exceptionOrNull()!!.cause!!
        }
        Unit
    }

    @Test
    fun `test request sign up failure (unknown)`() = runBlocking {
        // given
        val request = SignUpRequest.newBuilder()
            .setUsername("test")
            .setPassword("123")
            .setBirthday("15/12/1995")
            .setParrot(UserDTO.Parrot.BLUE.name)
            .build()
        val service = mock<RegisterServiceCoroutineStub> {
            onBlocking { signUp(request) } doThrow StatusRuntimeException(Status.UNKNOWN)
        }
        val userRemoteDataSource = remote(registerService = service)
        // when
        val result = userRemoteDataSource.requestSignUp(
            request.username, request.password, request.birthday, request.parrot
        )
        // then
        Assert.assertTrue((result.exceptionOrNull()!!.cause!! as StatusRuntimeException).status == Status.UNKNOWN)
        Assert.assertThrows(StatusRuntimeException::class.java) {
            throw result.exceptionOrNull()!!.cause!!
        }
        Unit
    }

    @Test
    fun `test request sign out successful`() = runBlocking {
        // given
        val service = mock<AuthenticationServiceCoroutineStub> {
            onBlocking { signOut(Empty.getDefaultInstance()) } doReturn Empty.getDefaultInstance()
        }
        val userRemoteDataSource = remote(authenticationService = service)
        // when
        val result = userRemoteDataSource.requestSignOut()
        // then
        Assert.assertTrue(result.isSuccess)
    }

    @Test
    fun `test request sign out failure (unavailable)`() = runBlocking {
        // given
        val service = mock<AuthenticationServiceCoroutineStub> {
            onBlocking { signOut(Empty.getDefaultInstance()) } doThrow StatusRuntimeException(Status.UNAVAILABLE)
        }
        val userRemoteDataSource = remote(authenticationService = service)
        // when
        val result = userRemoteDataSource.requestSignOut()
        // then
        Assert.assertTrue((result.exceptionOrNull()!!.cause!! as StatusRuntimeException).status == Status.UNAVAILABLE)
        Assert.assertThrows(StatusRuntimeException::class.java) {
            throw result.exceptionOrNull()!!.cause!!
        }
        Unit
    }

}