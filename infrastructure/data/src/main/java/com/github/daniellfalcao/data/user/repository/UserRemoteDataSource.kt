package com.github.daniellfalcao.data.user.repository

import com.github.daniellfalcao.common.utilities.ParrotResult
import com.github.daniellfalcao.common.utilities.mapCatching
import com.github.daniellfalcao.data._module.extension.executeFlowRequest
import com.github.daniellfalcao.data._module.extension.executeRequest
import com.github.daniellfalcao.domain.user.model.UsernameAvailabilityDTO
import com.google.protobuf.Empty
import com.proto.parrot.service.authentication.AuthenticationServiceGrpcKt
import com.proto.parrot.service.authentication.CheckUsernameAvailabilityRequest
import com.proto.parrot.service.authentication.RegisterServiceGrpcKt
import com.proto.parrot.service.authentication.SignInRequest
import com.proto.parrot.service.authentication.SignInResponse
import com.proto.parrot.service.authentication.SignUpRequest
import com.proto.parrot.service.user.User
import com.proto.parrot.service.user.UserServiceGrpcKt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRemoteDataSource(
    private val userService: UserServiceGrpcKt.UserServiceCoroutineStub,
    private val registerService: RegisterServiceGrpcKt.RegisterServiceCoroutineStub,
    private val authenticationService: AuthenticationServiceGrpcKt.AuthenticationServiceCoroutineStub
) {

    suspend fun requestUsernameAvailability(username: String): ParrotResult<UsernameAvailabilityDTO> {
        val request = CheckUsernameAvailabilityRequest.newBuilder()
            .setUsername(username)
            .build()
        return registerService.executeRequest {
            checkUsernameAvailability(request)
        }.mapCatching {
            UsernameAvailabilityDTO(it.isAvailable)
        }
    }

    suspend fun requestProfile(): Flow<User> {
        return userService.executeFlowRequest {
            profile(Empty.getDefaultInstance())
        }.map {
            it.user
        }
    }

    suspend fun requestSignIn(username: String, password: String): ParrotResult<SignInResponse> {
        val request = SignInRequest.newBuilder()
            .setUsername(username)
            .setPassword(password)
            .build()
        return authenticationService.executeRequest { signIn(request) }
    }

    suspend fun requestSignUp(
        username: String,
        password: String,
        birthday: String,
        parrot: String
    ): ParrotResult<Any> {
        val request = SignUpRequest.newBuilder()
            .setUsername(username)
            .setPassword(password)
            .setBirthday(birthday)
            .setParrot(parrot)
            .build()
        return registerService.executeRequest { signUp(request) }
    }

    suspend fun requestSignOut(): ParrotResult<Any> {
        return authenticationService.executeRequest { signOut(Empty.getDefaultInstance()) }
    }

}