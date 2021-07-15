package com.github.daniellfalcao.data._module.extension

import com.github.daniellfalcao.common.exception.ParrotException
import com.github.daniellfalcao.common.exception.toParrotException
import com.github.daniellfalcao.common.utilities.ParrotResult
import com.github.daniellfalcao.domain._module.exception.UnexpectedException
import io.grpc.StatusException
import io.grpc.StatusRuntimeException
import io.grpc.kotlin.AbstractCoroutineStub
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

internal suspend fun <A : AbstractCoroutineStub<*>, T> A.executeRequest(
    errorHandler: suspend (error: StatusRuntimeException) -> ParrotException? = { _ -> null },
    request: suspend A.() -> T
): ParrotResult<T> {
    return try {
        ParrotResult.success(request())
    } catch (exception: Throwable) {
        ParrotResult.failure(parseException(errorHandler, exception))
    }
}

internal suspend fun <A, T> A.executeFlowRequest(
    errorHandler: suspend (error: Throwable) -> ParrotException? = { _ -> null },
    request: suspend A.() -> Flow<T>
): Flow<T> {
    return request().catch { error -> throw parseException(errorHandler, error) }
}

private suspend fun parseException(
    externalErrorHandler: suspend (error: StatusRuntimeException) -> ParrotException?,
    exception: Throwable
): ParrotException {
    return when (exception) {
        is StatusRuntimeException -> {
            externalErrorHandler(exception) ?: UnexpectedException(exception)
        }
        is StatusException -> {
            exception.toGenericNetworkParrotException()
        }
        else -> {
            UnexpectedException(exception)
        }
    }
}