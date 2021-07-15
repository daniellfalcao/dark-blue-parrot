package com.github.daniellfalcao.authentication.viewmodel.state

import com.github.daniellfalcao.common.exception.ParrotException
import kotlinx.coroutines.flow.MutableStateFlow

sealed class FieldState {
    object Initial : FieldState()
    data class Error(val exception: ParrotException) : FieldState()
}

suspend fun MutableStateFlow<FieldState>.initialState() {
    emit(FieldState.Initial)
}

suspend fun MutableStateFlow<FieldState>.errorState(exception: ParrotException) {
    emit(FieldState.Error(exception))
}