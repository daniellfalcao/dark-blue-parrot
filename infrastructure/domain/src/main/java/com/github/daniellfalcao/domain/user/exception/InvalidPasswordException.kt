package com.github.daniellfalcao.domain.user.exception

import com.github.daniellfalcao.common.exception.ParrotException

/**
 * Throws a [InvalidPasswordException] when a password not matches with the requirements.
 *
 * @see [com.github.daniellfalcao.domain.user.repository.UserRepository.isPasswordValid] impl to
 * view requirements.
 *
 * */
class InvalidPasswordException : ParrotException()