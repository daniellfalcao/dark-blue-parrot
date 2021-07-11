package com.github.daniellfalcao.data.user

import com.github.daniellfalcao.common.utilities.ParrotResult
import com.github.daniellfalcao.domain.user.model.dto.UsernameAvailabilityResult
import com.github.daniellfalcao.domain.user.model.entity.UserEntity
import com.github.daniellfalcao.domain.user.repository.UserRepository
import java.util.Date

class UserRepositoryImpl(
    private val local: UserLocalDataSource,
    private val remote: UserRemoteDataSource
) : UserRepository {

    override suspend fun checkUsernameAvailability(
        username: String
    ): ParrotResult<UsernameAvailabilityResult> {
        return remote.requestUsernameAvailability(username)
    }

    override suspend fun profile(id: String): ParrotResult<UserEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun signIn(
        username: String,
        password: String
    ): ParrotResult<UserEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun signUp(
        username: String,
        password: String,
        birthday: Date,
        parrot: String
    ): ParrotResult<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun signOut(): ParrotResult<Any> {
        TODO("Not yet implemented")
    }
}