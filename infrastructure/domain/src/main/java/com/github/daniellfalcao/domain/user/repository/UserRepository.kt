package com.github.daniellfalcao.domain.user.repository

import com.github.daniellfalcao.common.utilities.ParrotResult
import com.github.daniellfalcao.domain.user.model.dto.UsernameAvailabilityResult
import com.github.daniellfalcao.domain.user.model.entity.UserEntity
import java.util.Date

interface UserRepository {
    suspend fun checkUsernameAvailability(username: String): ParrotResult<UsernameAvailabilityResult>
    suspend fun profile(id: String): ParrotResult<UserEntity>
    suspend fun signIn(username: String, password: String): ParrotResult<UserEntity>
    suspend fun signUp(username: String, password: String, birthday: Date, parrot: String): ParrotResult<Any>
    suspend fun signOut(): ParrotResult<Any>
}