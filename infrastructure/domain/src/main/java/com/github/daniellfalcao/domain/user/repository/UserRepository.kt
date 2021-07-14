package com.github.daniellfalcao.domain.user.repository

import com.github.daniellfalcao.common.utilities.ParrotResult
import com.github.daniellfalcao.domain.user.model.UserDTO
import com.github.daniellfalcao.domain.user.model.UsernameAvailabilityDTO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface UserRepository {

    // validations
    fun isUsernameValid(username: String): Boolean
    fun isPasswordValid(password: String): Boolean

    // register
    suspend fun checkUsernameAvailability(username: String): ParrotResult<UsernameAvailabilityDTO>
    suspend fun signUp(username: String, password: String, birthday: Date, parrot: UserDTO.Parrot): ParrotResult<Any>

    // authentication
    suspend fun hasUserAuthenticated(): Boolean
    suspend fun signIn(username: String, password: String): ParrotResult<Any>
    suspend fun signOut(): ParrotResult<Any>

    // profile
    fun flowUser(): Flow<UserDTO>
    suspend fun updateUserAsStream(): Job

    companion object {
        const val USERNAME_MIN_LENGTH = 3
        const val PASSWORD_LENGTH = 8
    }


}