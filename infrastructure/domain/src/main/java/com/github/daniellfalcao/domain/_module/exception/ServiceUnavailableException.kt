package com.github.daniellfalcao.domain._module.exception

import com.github.daniellfalcao.common.exception.ParrotException


class ServiceUnavailableException : ParrotException(
    message = "It looks like our server is not available, try again later."
)