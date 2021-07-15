package com.github.daniellfalcao.authentication.viewmodel.state

import com.github.daniellfalcao.common.exception.ParrotException
import kotlinx.coroutines.flow.MutableStateFlow

sealed class UsernameFieldState {
    object Initial : UsernameFieldState()
    object Loading : UsernameFieldState()
    object Available : UsernameFieldState()
    object Unavailable : UsernameFieldState()
    data class Error(val exception: ParrotException) : UsernameFieldState()
}

suspend fun MutableStateFlow<UsernameFieldState>.initialState() {
    emit(UsernameFieldState.Initial)
}

suspend fun MutableStateFlow<UsernameFieldState>.loadingState() {
    emit(UsernameFieldState.Loading)
}

suspend fun MutableStateFlow<UsernameFieldState>.availableState() {
    emit(UsernameFieldState.Available)
}

suspend fun MutableStateFlow<UsernameFieldState>.unavailableState() {
    emit(UsernameFieldState.Unavailable)
}

suspend fun MutableStateFlow<UsernameFieldState>.errorState(exception: ParrotException) {
    emit(UsernameFieldState.Error(exception))
}