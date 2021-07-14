package com.github.daniellfalcao.data.user.repository

import com.github.daniellfalcao.common.utilities.ParrotResult
import com.github.daniellfalcao.common.utilities.onSuccess
import com.github.daniellfalcao.data._module.database.ParrotDatabase
import com.github.daniellfalcao.data.user.model.entity.toDTO
import com.github.daniellfalcao.domain.user.model.UserDTO
import com.github.daniellfalcao.domain.user.model.UsernameAvailabilityDTO
import com.github.daniellfalcao.domain.user.repository.UserRepository
import com.proto.parrot.service.user.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.coroutines.coroutineContext

class UserRepositoryImpl(
    private val local: UserLocalDataSource,
    private val remote: UserRemoteDataSource
) : UserRepository {

    override suspend fun checkUsernameAvailability(
        username: String
    ): ParrotResult<UsernameAvailabilityDTO> {
        return remote.requestUsernameAvailability(username)
    }

    override suspend fun signUp(
        username: String,
        password: String,
        birthday: Date,
        parrot: UserDTO.Parrot
    ): ParrotResult<Any> {
        val birthdayValue = UserDTO.birthdayDateFormat.format(birthday)
        return remote.requestSignUp(username, password, birthdayValue, parrot.name)
    }

    override suspend fun hasUserAuthenticated(): Boolean {
        val user = local.user()
        val token = local.token()
        return user != null && token != null
    }

    override suspend fun signIn(username: String, password: String): ParrotResult<Any> {
        return remote.requestSignIn(username, password).onSuccess {
            local.saveUser(it.user)
            local.saveToken(it.token)
        }
    }

    override suspend fun signOut(): ParrotResult<Any> {
        return remote.requestSignOut().also {
            local.deleteToken()
            ParrotDatabase.dropDatabase()
        }
    }

    override fun flowUser(): Flow<UserDTO> {
        return local.flowUser().map { it.toDTO() }
    }

    override suspend fun updateUserAsStream(): Job {
        val pendingUpdates = mutableListOf<User>()
        val defaultDelay = 500L
        return CoroutineScope(coroutineContext).launch {
            launch {
                while (isActive) {
                    delay(defaultDelay)
                    if (pendingUpdates.isNotEmpty()) {
                        local.saveUser(pendingUpdates.removeFirst())
                    }
                }
            }
            remote.requestProfile().catch {
                while (pendingUpdates.isNotEmpty()) {
                    delay(defaultDelay)
                }
            }.collect {
                pendingUpdates.add(it)
            }
        }
    }

}