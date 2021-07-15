package com.github.daniellfalcao.authentication.viewmodel.state

import com.github.daniellfalcao.common.exception.ParrotException
import kotlinx.coroutines.flow.MutableStateFlow

sealed class SignUpState {
    object Initial : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
    data class Error(val exception: ParrotException) : SignUpState()
}

suspend fun MutableStateFlow<SignUpState>.initialState() {
    emit(SignUpState.Initial)
}

suspend fun MutableStateFlow<SignUpState>.loadingState() {
    emit(SignUpState.Loading)
}

suspend fun MutableStateFlow<SignUpState>.successState() {
    emit(SignUpState.Success)
}

suspend fun MutableStateFlow<SignUpState>.errorState(exception: ParrotException) {
    emit(SignUpState.Error(exception))
}