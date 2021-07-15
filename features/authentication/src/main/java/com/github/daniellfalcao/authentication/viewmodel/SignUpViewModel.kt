package com.github.daniellfalcao.authentication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.daniellfalcao.authentication.viewmodel.state.FieldState
import com.github.daniellfalcao.authentication.viewmodel.state.SignUpState
import com.github.daniellfalcao.authentication.viewmodel.state.UsernameFieldState
import com.github.daniellfalcao.authentication.viewmodel.state.availableState
import com.github.daniellfalcao.authentication.viewmodel.state.errorState
import com.github.daniellfalcao.authentication.viewmodel.state.initialState
import com.github.daniellfalcao.authentication.viewmodel.state.loadingState
import com.github.daniellfalcao.authentication.viewmodel.state.unavailableState
import com.github.daniellfalcao.common.exception.readOnly
import com.github.daniellfalcao.common.utilities.onFailure
import com.github.daniellfalcao.common.utilities.onSuccess
import com.github.daniellfalcao.common_ui.extension.toUnit
import com.github.daniellfalcao.domain.user.usecase.CheckPasswordUseCase
import com.github.daniellfalcao.domain.user.usecase.CheckUsernameUseCase
import com.github.daniellfalcao.domain.user.usecase.SignUpUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val checkUsernameUseCase: CheckUsernameUseCase,
    private val checkPasswordUseCase: CheckPasswordUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow<SignUpState>(SignUpState.Initial)
    val screenState = _screenState.readOnly()

    private val _usernameState = MutableStateFlow<UsernameFieldState>(UsernameFieldState.Initial)
    val usernameState = _usernameState.readOnly()

    private val _passwordState = MutableStateFlow<FieldState>(FieldState.Initial)
    val passwordState = _passwordState.readOnly()

    fun validateUsername(username: String) = viewModelScope.launch(Dispatchers.Default) {
        _usernameState.loadingState()
        checkUsernameUseCase(username)
            .onSuccess {
                if (it.isAvailable) {
                    _usernameState.availableState()
                } else {
                    _usernameState.unavailableState()
                }
            }.onFailure {
                _usernameState.errorState(it)
            }
    }.toUnit()

    fun validatePassword(password: String) = viewModelScope.launch(Dispatchers.Default) {
        checkPasswordUseCase(password)
            .onSuccess {
                _passwordState.initialState()
            }.onFailure {
                _passwordState.errorState(it)
            }
    }.toUnit()

    fun resetUsernameState() = viewModelScope.launch {
        _usernameState.initialState()
    }

    fun resetPasswordState() = viewModelScope.launch {
        _passwordState.initialState()
    }

}