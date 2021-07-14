package com.github.daniellfalcao.darkblueparrot.viewmodel.action

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

interface SplashAction {

    val action: MutableSharedFlow<Action>

    sealed class Action {
        data class ContinueToNavigation(val hasUserAuthenticated: Boolean) : Action()
    }

    fun ViewModel.continueToNavigation(
        hasUserAuthenticated: Boolean
    ) = viewModelScope.launch(Dispatchers.Main) {
        action.emit(Action.ContinueToNavigation(hasUserAuthenticated))
    }
}

class SplashActionImpl : SplashAction {
    override val action = MutableSharedFlow<SplashAction.Action>()
}