package com.github.daniellfalcao.domain.user.exception

import com.github.daniellfalcao.common.exception.ParrotException

/**
 * Throws a [InvalidUsernameException] when a username not matches with the requirements.
 *
 * @see [com.github.daniellfalcao.domain.user.repository.UserRepository.isUsernameValid] impl to
 * view requirements.
 *
 * */
class InvalidUsernameException : ParrotException()