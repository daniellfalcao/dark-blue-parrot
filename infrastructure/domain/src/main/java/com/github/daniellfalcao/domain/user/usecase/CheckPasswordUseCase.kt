package com.github.daniellfalcao.domain.user.usecase

import com.github.daniellfalcao.common.utilities.runCatching
import com.github.daniellfalcao.domain.user.exception.InvalidPasswordException
import com.github.daniellfalcao.domain.user.repository.UserRepository

/**
 * Check if the given password comply with the requirements.
 *
 * @return ParrotResult.Success if password is valid
 * @return ParrotResult.Failure if password is not valid
 *
 * */
class CheckPasswordUseCase(private val userRepository: UserRepository) {

    operator fun invoke(password: String) = runCatching {
        if (!userRepository.isPasswordValid(password)) {
            throw InvalidPasswordException()
        }
    }
}