package com.sanaa.presentation.screen.welcome

import com.sanaa.presentation.screen.login_base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class WelcomeViewModel(
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<Unit, WelcomeScreenEffects>(Unit),
    WelcomeScreenInteractionListener {

    override fun onLoginClicked() = emitEffect(WelcomeScreenEffects.NavigateToLogin)

    override fun onContinueClicked() = emitEffect(WelcomeScreenEffects.ContinueAsGuest)

    override fun onExit() = emitEffect(WelcomeScreenEffects.ExitApp)
}