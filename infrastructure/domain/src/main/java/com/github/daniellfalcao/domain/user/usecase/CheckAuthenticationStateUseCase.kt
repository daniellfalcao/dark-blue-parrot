package com.github.daniellfalcao.domain.user.usecase

import com.github.daniellfalcao.domain.user.repository.UserRepository


/**
 * Check the current state of user authentication in the app.
 *
 * @return true if has a user and a token saved.
 * @return false if there is no user or token saved.
 *
 * */
class CheckAuthenticationStateUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke() = userRepository.hasUserAuthenticated()

}