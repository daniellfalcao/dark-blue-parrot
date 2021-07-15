package com.github.daniellfalcao.data._module.extension

import com.github.daniellfalcao.common.exception.ParrotException
import com.github.daniellfalcao.domain._module.exception.ServiceUnavailableException
import com.github.daniellfalcao.domain._module.exception.UnexpectedException
import io.grpc.Status
import io.grpc.StatusException

fun StatusException.toGenericNetworkParrotException(): ParrotException {
    return if (status.code == Status.Code.UNAVAILABLE) {
        ServiceUnavailableException()
    } else {
        UnexpectedException(this)
    }
}