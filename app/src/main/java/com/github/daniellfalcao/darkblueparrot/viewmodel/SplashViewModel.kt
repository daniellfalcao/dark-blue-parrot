package com.github.daniellfalcao.darkblueparrot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.daniellfalcao.darkblueparrot.viewmodel.action.SplashAction
import com.github.daniellfalcao.darkblueparrot.viewmodel.action.SplashActionImpl
import com.github.daniellfalcao.domain.user.usecase.CheckAuthenticationStateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashViewModel(
    private val checkAuthenticationStateUseCase: CheckAuthenticationStateUseCase
) : ViewModel(), SplashAction by SplashActionImpl() {

    fun dispatchNavigation() = viewModelScope.launch(Dispatchers.Default) {
        continueToNavigation(checkAuthenticationStateUseCase())
    }

}