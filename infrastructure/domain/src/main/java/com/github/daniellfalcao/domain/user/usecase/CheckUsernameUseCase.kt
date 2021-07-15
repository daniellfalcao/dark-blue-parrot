package com.github.daniellfalcao.domain.user.usecase

import com.github.daniellfalcao.common.utilities.ParrotResult
import com.github.daniellfalcao.common.utilities.getOrThrow
import com.github.daniellfalcao.common.utilities.runCatching
import com.github.daniellfalcao.domain.user.exception.InvalidUsernameException
import com.github.daniellfalcao.domain.user.model.UsernameAvailabilityDTO
import com.github.daniellfalcao.domain.user.repository.UserRepository

/**
 * Check if the given username it's available to a new user.
 *
 * @return ParrotResult.Success if username is valid and is available or not
 * @return ParrotResult.Failure if username is not valid or because some error in API
 *
 * ParrotResult.Failure can assume the following exceptions:
 *  - [InvalidUsernameException]
 *  and service error handling generic exceptions
 *
 * */
class CheckUsernameUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(
        username: String
    ): ParrotResult<UsernameAvailabilityDTO> = runCatching {
        if (!userRepository.isUsernameValid(username)) {
            throw InvalidUsernameException()
        }
        userRepository.checkUsernameAvailability(username).getOrThrow()
    }

}