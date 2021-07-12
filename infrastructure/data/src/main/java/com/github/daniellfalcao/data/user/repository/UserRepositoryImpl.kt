package com.github.daniellfalcao.data.user.repository

import com.github.daniellfalcao.common.utilities.ParrotResult
import com.github.daniellfalcao.common.utilities.onSuccess
import com.github.daniellfalcao.data._module.database.ParrotDatabase
import com.github.daniellfalcao.data.user.model.view.toDTO
import com.github.daniellfalcao.domain.user.model.ProfileDTO
import com.github.daniellfalcao.domain.user.model.UserDTO
import com.github.daniellfalcao.domain.user.model.UsernameAvailabilityDTO
import com.github.daniellfalcao.domain.user.repository.UserRepository
import com.proto.parrot.service.user.User
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Date

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
        return remote.requestSignOut().onSuccess {
            ParrotDatabase.dropDatabase()
        }
    }

    override suspend fun flowProfile(): Flow<ProfileDTO> {
        return local.flowProfile().map { it.toDTO() }
    }

    override suspend fun updateProfileAsStream() {
        val pendingUpdates = mutableListOf<User>()
        coroutineScope {
            launch {
                while (isActive) {
                    delay(500)
                    if (pendingUpdates.isNotEmpty()) {
                        local.saveUser(pendingUpdates.removeFirst())
                    }
                }
            }
            remote.requestProfile().onEach { pendingUpdates.add(it) }
        }

    }

}