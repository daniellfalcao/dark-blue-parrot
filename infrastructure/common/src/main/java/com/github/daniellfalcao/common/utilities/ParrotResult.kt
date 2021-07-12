@file:Suppress("UNCHECKED_CAST", "unused")

package com.github.daniellfalcao.common.utilities

import com.github.daniellfalcao.common.ParrotException
import com.github.daniellfalcao.common.toParrotException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * A discriminated union that encapsulates a successful outcome with a value of type [T]
 * or a failure with an arbitrary [Throwable] exception.
 *
 */
class ParrotResult<out T> @PublishedApi internal constructor(
    @PublishedApi
    internal val value: Any?
) {
    // discovery

    /**
     * Returns `true` if this instance represents a successful outcome.
     * In this case [isFailure] returns `false`.
     *
     */
    val isSuccess: Boolean get() = value !is Failure

    /**
     * Returns `true` if this instance represents a failed outcome.
     * In this case [isSuccess] returns `false`.
     *
     */
    val isFailure: Boolean get() = value is Failure

    // value & exception retrieval

    /**
     * Returns the encapsulated value if this instance represents [success][ParrotResult.isSuccess]
     * or `null` if it is [failure][ParrotResult.isFailure].
     *
     * This function is a shorthand for `getOrElse { null }` (see [getOrElse]) or
     * `fold(onSuccess = { it }, onFailure = { null })` (see [fold]).
     *
     */
    fun getOrNull(): T? =
        when {
            isFailure -> null
            else -> value as T
        }

    /**
     * Returns the encapsulated [Throwable] exception if this instance represents
     * [failure][isFailure] or `null` if it is [success][isSuccess].
     *
     * This function is a shorthand for `fold(onSuccess = { null }, onFailure = { it })` (see [fold]).
     *
     */
    fun exceptionOrNull(): Throwable? =
        when (value) {
            is Failure -> value.exception
            else -> null
        }

    /**
     * Returns a string `Success(v)` if this instance represents [success][ParrotResult.isSuccess]
     * where `v` is a string representation of the value or a string `Failure(x)` if
     * it is [failure][isFailure] where `x` is a string representation of the exception.
     *
     */
    override fun toString(): String =
        when (value) {
            is Failure -> value.toString() // "Failure($exception)"
            else -> "Success($value)"
        }

    // companion with constructors

    /**
     * Companion object for [ParrotResult] class that contains its constructor functions
     * [success] and [failure].
     */
    companion object {

        /**
         * Returns an instance that encapsulates the given [value] as successful value.
         *
         */
        fun <T> success(value: T) = ParrotResult<T>(value)

        /**
         * Returns an instance that encapsulates the given [Throwable] [exception] as failure.
         *
         */
        fun <T> failure(exception: Throwable) = ParrotResult<T>(createFailure(exception))
    }

    internal class Failure(@JvmField val exception: ParrotException) {
        override fun equals(other: Any?): Boolean = other is Failure && exception == other.exception
        override fun hashCode(): Int = exception.hashCode()
        override fun toString(): String = "Failure($exception)"
    }
}

/**
 * Creates an instance of internal marker [ParrotResult.Failure] class to
 * make sure that this class is not exposed in ABI.
 *
 */
@PublishedApi
internal fun createFailure(exception: Throwable): Any {
    val error: ParrotException = if (exception is ParrotException) {
        exception
    } else {
        exception.toParrotException()
    }
    return ParrotResult.Failure(error)
}



/**
 * Calls the specified function [block] and returns its encapsulated result if invocation was successful,
 * catching any [Throwable] exception that was thrown from the [block] function execution and encapsulating it as a failure.
 *
 */
inline fun <R> runCatching(block: () -> R): ParrotResult<R> {
    return try {
        ParrotResult.success(block())
    } catch (e: Throwable) {
        ParrotResult.failure(e)
    }
}

/**
 * Calls the specified function [block] with `this` value as its receiver and returns its encapsulated result if invocation was successful,
 * catching any [Throwable] exception that was thrown from the [block] function execution and encapsulating it as a failure.
 *
 */
inline fun <T, R> T.runCatching(block: T.() -> R): ParrotResult<R> {
    return try {
        ParrotResult.success(block())
    } catch (e: Throwable) {
        ParrotResult.failure(e)
    }
}

/**
 * Throws exception if the result is failure. This internal function minimizes
 * inlined bytecode for [getOrThrow] and makes sure that in the future we can
 * add some exception-augmenting logic here (if needed).
 */
fun ParrotResult<*>.throwOnFailure() {
    if (value is ParrotResult.Failure) throw value.exception
}

/**
 * Returns the encapsulated value if this instance represents [success][ParrotResult.isSuccess] or throws the encapsulated [Throwable] exception
 * if it is [failure][ParrotResult.isFailure].
 *
 * This function is a shorthand for `getOrElse { throw it }` (see [getOrElse]).
 */
fun <T> ParrotResult<T>.getOrThrow(): T {
    throwOnFailure()
    return value as T
}

/**
 * Returns the encapsulated value if this instance represents [success][ParrotResult.isSuccess] or the
 * result of [onFailure] function for the encapsulated [Throwable] exception if it is [failure][ParrotResult.isFailure].
 *
 * Note, that this function rethrows any [Throwable] exception thrown by [onFailure] function.
 *
 * This function is a shorthand for `fold(onSuccess = { it }, onFailure = onFailure)` (see [fold]).
 *
 */
@OptIn(ExperimentalContracts::class)
inline fun <R, T : R> ParrotResult<T>.getOrElse(onFailure: (exception: Throwable) -> R): R {
    contract {
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }
    return when (val exception = exceptionOrNull()) {
        null -> value as T
        else -> onFailure(exception)
    }
}

/**
 * Returns the encapsulated value if this instance represents [success][ParrotResult.isSuccess] or the
 * [defaultValue] if it is [failure][ParrotResult.isFailure].
 *
 * This function is a shorthand for `getOrElse { defaultValue }` (see [getOrElse]).
 *
 */
fun <R, T : R> ParrotResult<T>.getOrDefault(defaultValue: R): R {
    if (isFailure) return defaultValue
    return value as T
}

/**
 * Returns the result of [onSuccess] for the encapsulated value if this instance represents [success][ParrotResult.isSuccess]
 * or the result of [onFailure] function for the encapsulated [Throwable] exception if it is [failure][ParrotResult.isFailure].
 *
 * Note, that this function rethrows any [Throwable] exception thrown by [onSuccess] or by [onFailure] function.
 *
 */
@OptIn(ExperimentalContracts::class)
inline fun <R, T> ParrotResult<T>.fold(
    onSuccess: (value: T) -> R,
    onFailure: (exception: Throwable) -> R
): R {
    contract {
        callsInPlace(onSuccess, InvocationKind.AT_MOST_ONCE)
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }
    return when (val exception = exceptionOrNull()) {
        null -> onSuccess(value as T)
        else -> onFailure(exception)
    }
}

// transformation

/**
 * Returns the encapsulated result of the given [transform] function applied to the encapsulated value
 * if this instance represents [success][ParrotResult.isSuccess] or the
 * original encapsulated [Throwable] exception if it is [failure][ParrotResult.isFailure].
 *
 * Note, that this function rethrows any [Throwable] exception thrown by [transform] function.
 * See [mapCatching] for an alternative that encapsulates exceptions.
 *
 */
@OptIn(ExperimentalContracts::class)
inline fun <R, T> ParrotResult<T>.map(transform: (value: T) -> R): ParrotResult<R> {
    contract {
        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
    }
    return when {
        isSuccess -> ParrotResult.success(transform(value as T))
        else -> this as ParrotResult<R>
    }
}

/**
 * Returns the encapsulated result of the given [transform] function applied to the encapsulated value
 * if this instance represents [success][ParrotResult.isSuccess] or the
 * original encapsulated [Throwable] exception if it is [failure][ParrotResult.isFailure].
 *
 * This function catches any [Throwable] exception thrown by [transform] function and encapsulates it as a failure.
 * See [map] for an alternative that rethrows exceptions from `transform` function.
 *
 */
inline fun <R, T> ParrotResult<T>.mapCatching(transform: (value: T) -> R): ParrotResult<R> {
    return when {
        isSuccess -> runCatching { transform(value as T) }
        else -> ParrotResult(value)
    }
}

/**
 * Returns the encapsulated result of the given [transform] function applied to the encapsulated [Throwable] exception
 * if this instance represents [failure][ParrotResult.isFailure] or the
 * original encapsulated value if it is [success][ParrotResult.isSuccess].
 *
 * Note, that this function rethrows any [Throwable] exception thrown by [transform] function.
 * See [recoverCatching] for an alternative that encapsulates exceptions.
 *
 */
@OptIn(ExperimentalContracts::class)
inline fun <R, T : R> ParrotResult<T>.recover(transform: (exception: Throwable) -> R): ParrotResult<R> {
    contract {
        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
    }
    return when (val exception = exceptionOrNull()) {
        null -> this
        else -> ParrotResult.success(transform(exception))
    }
}

/**
 * Returns the encapsulated result of the given [transform] function applied to the encapsulated [Throwable] exception
 * if this instance represents [failure][ParrotResult.isFailure] or the
 * original encapsulated value if it is [success][ParrotResult.isSuccess].
 *
 * This function catches any [Throwable] exception thrown by [transform] function and encapsulates it as a failure.
 * See [recover] for an alternative that rethrows exceptions.
 *
 */
inline fun <R, T : R> ParrotResult<T>.recoverCatching(transform: (exception: Throwable) -> R): ParrotResult<R> {
    return when (val exception = exceptionOrNull()) {
        null -> this
        else -> runCatching { transform(exception) }
    }
}

// "peek" onto value/exception and pipe

/**
 * Performs the given [action] on the encapsulated [Throwable] exception if this instance represents [failure][ParrotResult.isFailure].
 * Returns the original [ParrotResult] unchanged.
 *
 */
@OptIn(ExperimentalContracts::class)
suspend fun <T> ParrotResult<T>.onFailure(action: suspend (exception: ParrotException) -> Unit): ParrotResult<T> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    exceptionOrNull()?.let {
        if (it is ParrotException) action(it) else action(it.toParrotException())
    }
    return this
}

/**
 * Performs the given [action] on the encapsulated value if this instance represents [success][ParrotResult.isSuccess].
 * Returns the original [ParrotResult] unchanged.
 *
 */
@OptIn(ExperimentalContracts::class)
suspend fun <T> ParrotResult<T>.onSuccess(action: suspend (value: T) -> Unit): ParrotResult<T> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isSuccess) action(value as T)
    return this
}