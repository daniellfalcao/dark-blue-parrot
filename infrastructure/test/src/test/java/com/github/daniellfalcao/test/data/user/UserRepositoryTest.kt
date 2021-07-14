package com.github.daniellfalcao.test.data.user

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.daniellfalcao.common.exception.toParrotException
import com.github.daniellfalcao.common.utilities.ParrotResult
import com.github.daniellfalcao.data.user.model.entity.toDTO
import com.github.daniellfalcao.data.user.model.entity.toEntity
import com.github.daniellfalcao.data.user.repository.UserLocalDataSource
import com.github.daniellfalcao.data.user.repository.UserRemoteDataSource
import com.github.daniellfalcao.data.user.repository.UserRepositoryImpl
import com.github.daniellfalcao.domain.user.model.UserDTO
import com.github.daniellfalcao.domain.user.model.UserDTO.Parrot.BLUE
import com.github.daniellfalcao.domain.user.model.UserDTO.Parrot.YELLOW
import com.github.daniellfalcao.domain.user.model.UsernameAvailabilityDTO
import com.github.daniellfalcao.test._module.RepositoryTest
import com.github.daniellfalcao.test._module.mock.user1
import com.google.protobuf.Empty
import com.proto.parrot.service.authentication.SignInResponse
import com.proto.parrot.service.user.ProfileResponse
import com.proto.parrot.service.user.UserServiceGrpcKt
import io.grpc.Status
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.get
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE, sdk = [28])
class UserRepositoryTest : RepositoryTest() {

    private fun buildRepository(
        remote: UserRemoteDataSource = mock()
    ) = UserRepositoryImpl(get(), remote)

    @Test
    fun `test check username available`() = runBlocking {
        // given
        val remote = mock<UserRemoteDataSource> {
            onBlocking {
                requestUsernameAvailability("test")
            } doReturn ParrotResult.success(UsernameAvailabilityDTO(true))
        }
        val repository = buildRepository(remote)
        // when
        val result = repository.checkUsernameAvailability("test")
        // then
        Assert.assertTrue(result.getOrNull()!!.isAvailable)
    }

    @Test
    fun `test check username not available`() = runBlocking {
        // given
        val remote = mock<UserRemoteDataSource> {
            onBlocking {
                requestUsernameAvailability("test")
            } doReturn ParrotResult.success(UsernameAvailabilityDTO(false))
        }
        val repository = buildRepository(remote)
        // when
        val result = repository.checkUsernameAvailability("test")
        // then
        Assert.assertFalse(result.getOrNull()!!.isAvailable)
    }

    @Test
    fun `test check username failure`() = runBlocking {
        // given
        val remote = mock<UserRemoteDataSource> {
            onBlocking {
                requestUsernameAvailability("test")
            } doReturn ParrotResult.failure(StatusRuntimeException(Status.UNAVAILABLE).toParrotException())
        }
        val repository = buildRepository(remote)
        // when
        val result = repository.checkUsernameAvailability("test")
        // then
        Assert.assertTrue(result.isFailure)
        Assert.assertTrue(result.exceptionOrNull()!!.cause is StatusRuntimeException)
    }

    @Test
    fun `test sign up successful`() = runBlocking {
        // given
        val remote = mock<UserRemoteDataSource> {
            onBlocking {
                requestSignUp("test", "123", "15/12/1995", BLUE.name)
            } doReturn ParrotResult.success(Empty.getDefaultInstance())
        }
        val repository = buildRepository(remote)
        // when
        val result = repository.signUp(
            "test",
            "123",
            UserDTO.birthdayDateFormat.parse("15/12/1995")!!,
            BLUE
        )
        // then
        Assert.assertTrue(result.isSuccess)
    }

    @Test
    fun `test sign up failure`() = runBlocking {
        // given
        val remote = mock<UserRemoteDataSource> {
            onBlocking {
                requestSignUp("test", "123", "15/12/1995", BLUE.name)
            } doReturn ParrotResult.failure(StatusRuntimeException(Status.ALREADY_EXISTS).toParrotException())
        }
        val repository = buildRepository(remote)
        // when
        val result = repository.signUp(
            "test",
            "123",
            UserDTO.birthdayDateFormat.parse("15/12/1995")!!,
            BLUE
        )
        // then
        Assert.assertTrue(result.isFailure)
        Assert.assertTrue(result.exceptionOrNull()!!.cause is StatusRuntimeException)
    }

    @Test
    fun `test if user is authenticated (with user and token)`() = runBlocking {
        // given
        val user = user1.build()
        val token = "token-safe"
        get<UserLocalDataSource>().apply {
            saveUser(user)
            saveToken(token)
        }
        val repository = buildRepository()
        // when
        val result = repository.hasUserAuthenticated()
        // then
        Assert.assertTrue(result)
    }

    @Test
    fun `test if user is not authenticated (without user and token)`() = runBlocking {
        // given
        val repository = buildRepository()
        // when
        val result = repository.hasUserAuthenticated()
        // then
        Assert.assertFalse(result)
    }

    @Test
    fun `test if user is not authenticated (without user and with token)`() = runBlocking {
        // given
        val token = "token-safe"
        get<UserLocalDataSource>().apply {
            saveToken(token)
        }
        val repository = buildRepository()
        // when
        val result = repository.hasUserAuthenticated()
        // then
        Assert.assertFalse(result)
    }

    @Test
    fun `test if user is not authenticated (with user and without token)`() = runBlocking {
        // given
        val user = user1.build()
        get<UserLocalDataSource>().apply {
            saveUser(user)
        }
        val repository = buildRepository()
        // when
        val result = repository.hasUserAuthenticated()
        // then
        Assert.assertFalse(result)
    }

    @Test
    fun `test if there is user and token stored when sign in successful`() = runBlocking {
        // given
        val user = user1.build()
        val token = "token-yey"
        val response = SignInResponse.newBuilder()
            .setUser(user)
            .setToken(token)
            .build()
        val remote = mock<UserRemoteDataSource> {
            onBlocking {
                requestSignIn(user.username, "123")
            } doReturn ParrotResult.success(response)
        }
        val local = get<UserLocalDataSource>()
        val repository = buildRepository(remote)
        // when
        val result = repository.signIn(user.username, "123")
        val storedUser = local.user()!!
        val storedToken = local.token()!!
        val isUserAuthenticated = repository.hasUserAuthenticated()
        // then
        Assert.assertTrue(result.isSuccess)
        Assert.assertTrue(storedToken == token)
        Assert.assertTrue(isUserAuthenticated)
        Assert.assertTrue(storedUser.id == user.id)
        Assert.assertTrue(storedUser.username == user.username)
        Assert.assertTrue(storedUser.parrot.name == user.parrot)
        Assert.assertTrue(storedUser.likes == user.likes)
        Assert.assertTrue(storedUser.bookmarks == user.bookmarks)
        Assert.assertTrue(storedUser.birthday!!.time == UserDTO.birthdayDateFormat.parse(user.birthday)!!.time)
    }

    @Test
    fun `test if there is no user or token stored when sign in failure`() = runBlocking {
        // given
        val user = user1.build()
        val remote = mock<UserRemoteDataSource> {
            onBlocking {
                requestSignIn(user.username, "123")
            } doReturn ParrotResult.failure(StatusRuntimeException(Status.NOT_FOUND).toParrotException())
        }
        val local = get<UserLocalDataSource>()
        val repository = buildRepository(remote)
        // when
        val result = repository.signIn(user.username, "123")
        val storedUser = local.user()
        val storedToken = local.token()
        val isUserAuthenticated = repository.hasUserAuthenticated()
        // then
        Assert.assertTrue(result.isFailure)
        Assert.assertTrue(result.exceptionOrNull()!!.cause is StatusRuntimeException)
        Assert.assertTrue(storedToken == null)
        Assert.assertTrue(storedUser == null)
        Assert.assertFalse(isUserAuthenticated)
    }

    @Test
    fun `test if there is no user or token stored when sign out successful`() = runBlocking {
        // given
        val user = user1.build()
        val token = "token-safe"
        get<UserLocalDataSource>().apply {
            saveUser(user)
            saveToken(token)
        }
        val local = get<UserLocalDataSource>()
        val remote = mock<UserRemoteDataSource> {
            onBlocking {
                requestSignOut()
            } doReturn ParrotResult.success(Empty.getDefaultInstance())
        }
        val repository = buildRepository(remote)
        // when
        val result = repository.signOut()
        val storedUser = local.user()
        val storedToken = local.token()
        val isUserAuthenticated = repository.hasUserAuthenticated()
        // then
        Assert.assertTrue(result.isSuccess)
        Assert.assertTrue(storedUser == null)
        Assert.assertTrue(storedToken == null)
        Assert.assertFalse(isUserAuthenticated)
    }

    @Test
    fun `test if there is no user or token stored when sign out failure`() = runBlocking {
        // given
        val user = user1.build()
        val token = "token-safe"
        get<UserLocalDataSource>().apply {
            saveUser(user)
            saveToken(token)
        }
        val local = get<UserLocalDataSource>()
        val remote = mock<UserRemoteDataSource> {
            onBlocking {
                requestSignOut()
            } doReturn ParrotResult.failure(StatusRuntimeException(Status.UNAVAILABLE).toParrotException())
        }
        val repository = buildRepository(remote)
        // when
        val result = repository.signOut()
        val storedUser = local.user()
        val storedToken = local.token()
        val isUserAuthenticated = repository.hasUserAuthenticated()
        // then
        Assert.assertTrue(result.isFailure)
        Assert.assertTrue(storedUser == null)
        Assert.assertTrue(storedToken == null)
        Assert.assertTrue(storedToken == null)
        Assert.assertFalse(isUserAuthenticated)
    }

    @Test
    fun `test update profile and flow updates`() = runBlocking {
        // given
        val user1update1 = user1.build()
        val user1update2 = user1.setLikes(2).build()
        val user1update3 = user1.setParrot(YELLOW.name).build()
        val local = get<UserLocalDataSource>()
        val repository = buildRepository()
        // when
        var result: UserDTO? = null
        val flowProfile = launch {
            withTimeout(1000) { repository.flowUser().collect { result = it } }
        }
        local.saveUser(user1update1)
        local.saveUser(user1update2)
        local.saveUser(user1update3)
        flowProfile.join()
        // then
        Assert.assertTrue(result == user1update3.toEntity().toDTO())
    }

    @Test
    fun `test update profile with stream incoming from remote`() = runBlocking {
        // given
        val user1update1 = user1.build()
        val user1update2 = user1.setLikes(2).build()
        val user1update3 = user1.setBookmarks(5).build()
        val remote = mock<UserRemoteDataSource> {
            onBlocking {
                requestProfile()
            } doReturn flow {
                emit(user1update1)
                emit(user1update2)
                emit(user1update3)
            }
        }
        val repository = buildRepository(remote)
        // when
        val result = mutableListOf<UserDTO>()
        val flowProfileJob = launch {
            repository.flowUser().take(3).toList(result)
        }
        val updateProfileJob = repository.updateUserAsStream()
        flowProfileJob.join()
        updateProfileJob.cancelAndJoin()
        // then
        Assert.assertTrue(result.size == 3)
        Assert.assertTrue(result[0] == user1update1.toEntity().toDTO())
        Assert.assertTrue(result[1] == user1update2.toEntity().toDTO())
        Assert.assertTrue(result[2] == user1update3.toEntity().toDTO())
    }

    @Test
    fun `test update profile with stream incoming from remote and receive error`() = runBlocking {
        // given
        val response1 = ProfileResponse.newBuilder()
            .setUser(user1.build())
            .build()
        val response2 = ProfileResponse.newBuilder()
            .setUser(user1.setLikes(6).build())
            .build()
        val userService = mock<UserServiceGrpcKt.UserServiceCoroutineStub> {
            onBlocking { profile(Empty.getDefaultInstance()) } doReturn flow {
                emit(response1)
                emit(response2)
                throw StatusRuntimeException(Status.UNKNOWN)
            }
        }
        val remote = UserRemoteDataSource(userService, mock(), mock())
        val repository = buildRepository(remote)
        // when
        val result = mutableListOf<UserDTO>()
        val flowProfileJob = launch {
            repository.flowUser().take(2).toList(result)
        }
        val updateProfileJob = repository.updateUserAsStream()
        flowProfileJob.join()
        updateProfileJob.cancelAndJoin()
        // then
        Assert.assertTrue(result.size == 2)
        Assert.assertTrue(result[0] == response1.user.toEntity().toDTO())
        Assert.assertTrue(result[1] == response2.user.toEntity().toDTO())
    }

}