package com.github.daniellfalcao.data._module.extension

import com.github.daniellfalcao.common.ParrotException
import com.github.daniellfalcao.common.toParrotException
import com.github.daniellfalcao.common.utilities.ParrotResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

internal suspend fun <A, T> A.executeRequest(
    errorHandler: suspend (error: Throwable) -> ParrotException? = { _ -> null },
    request: suspend A.() -> T
): ParrotResult<T> {
    return try {
        ParrotResult.success(request())
    } catch (exception: Throwable) {
        ParrotResult.failure(errorHandler(exception) ?: exception.toParrotException())
    }
}

internal suspend fun <A, T> A.executeFlowRequest(
    errorHandler: suspend (error: Throwable) -> ParrotException? = { _ -> null },
    request: suspend A.() -> Flow<T>
): Flow<T> {
    return request().catch { error -> throw errorHandler(error) ?: error.toParrotException() }
}