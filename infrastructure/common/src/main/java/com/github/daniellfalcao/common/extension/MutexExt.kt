package com.github.daniellfalcao.common.extension

import kotlinx.coroutines.sync.Mutex

fun Mutex.safeUnlock() {
    try {
        unlock()
    } catch (e: IllegalStateException) {
    }
}

fun Mutex.doOnLock(action: () -> Unit) {
    if (tryLock()) {
        try {
            return action()
        } finally {
            safeUnlock()
        }
    }
}

suspend fun Mutex.doOnSuspendLock(owner: Any? = null, action: suspend () -> Unit) {
    if (tryLock()) {
        try {
            return action()
        } finally {
            safeUnlock()
        }
    }
}