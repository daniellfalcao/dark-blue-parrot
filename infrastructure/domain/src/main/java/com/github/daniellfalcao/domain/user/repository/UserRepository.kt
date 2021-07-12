package com.github.daniellfalcao.domain.user.repository

import com.github.daniellfalcao.common.utilities.ParrotResult
import com.github.daniellfalcao.domain.user.model.ProfileDTO
import com.github.daniellfalcao.domain.user.model.UserDTO
import com.github.daniellfalcao.domain.user.model.UsernameAvailabilityDTO
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface UserRepository {

    // register
    suspend fun checkUsernameAvailability(username: String): ParrotResult<UsernameAvailabilityDTO>
    suspend fun signUp(username: String, password: String, birthday: Date, parrot: UserDTO.Parrot): ParrotResult<Any>

    // authentication
    suspend fun hasUserAuthenticated(): Boolean
    suspend fun signIn(username: String, password: String): ParrotResult<Any>
    suspend fun signOut(): ParrotResult<Any>

    // profile
    suspend fun flowProfile(): Flow<ProfileDTO>
    suspend fun updateProfileAsStream()

}