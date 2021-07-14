package com.github.daniellfalcao.domain.user.usecase

import com.github.daniellfalcao.common.exception.ParrotException
import com.github.daniellfalcao.common.utilities.ParrotResult
import com.github.daniellfalcao.common.utilities.getOrThrow
import com.github.daniellfalcao.common.utilities.runCatching
import com.github.daniellfalcao.domain.user.exception.InvalidPasswordException
import com.github.daniellfalcao.domain.user.exception.InvalidUsernameException
import com.github.daniellfalcao.domain.user.model.UserDTO
import com.github.daniellfalcao.domain.user.repository.UserRepository

/**
 * Attempt to register a new user.
 *
 * */
class SignUpUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(
        username: String,
        password: String,
        birthday: String,
        parrot: UserDTO.Parrot
    ): ParrotResult<Any> = runCatching {
        val date = UserDTO.birthdayDateFormat.parse(birthday) ?: throw ParrotException()
        if (!userRepository.isUsernameValid(username)) {
            throw InvalidUsernameException()
        }
        if (!userRepository.isPasswordValid(password)) {
            throw InvalidPasswordException()
        }
        userRepository.signUp(username, password, date, parrot).getOrThrow()
    }
}