package com.github.daniellfalcao.domain.user.usecase

import com.github.daniellfalcao.common.utilities.ParrotResult
import com.github.daniellfalcao.common.utilities.getOrThrow
import com.github.daniellfalcao.common.utilities.runCatching
import com.github.daniellfalcao.domain.user.exception.InvalidUsernameException
import com.github.daniellfalcao.domain.user.repository.UserRepository

/**
 * Attempt authenticate a user with the given username and password.
 *
 * */
class SignInUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(
        username: String,
        password: String
    ): ParrotResult<Any> = runCatching {
        if (username.isBlank()) {
            throw InvalidUsernameException()
        }
        if (password.isBlank()) {
            throw InvalidUsernameException()
        }
        userRepository.signIn(username, password).getOrThrow()
    }

}