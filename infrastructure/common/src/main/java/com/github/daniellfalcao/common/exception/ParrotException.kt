package com.github.daniellfalcao.common.exception


/** Base exception to be used as default in app. */
open class ParrotException(
    override val message: String = "",
    override val cause: Throwable? = null
) : Exception()

fun Throwable.toParrotException(message: String = "") = ParrotException(message, cause = this)