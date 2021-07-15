package com.github.daniellfalcao.domain._module.exception

import com.github.daniellfalcao.common.exception.ParrotException

class UnexpectedException(override val cause: Throwable?) : ParrotException(
    message = "Something unexpected happened."
)