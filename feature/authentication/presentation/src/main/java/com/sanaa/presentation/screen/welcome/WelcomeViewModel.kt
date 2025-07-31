package com.sanaa.presentation.screen.welcome

import com.sanaa.presentation.screen.login_base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : BaseViewModel<Unit, WelcomeScreenEffects>(Unit),
    WelcomeScreenInteractionListener {

    override fun onLoginClicked() = emitEffect(WelcomeScreenEffects.NavigateToLogin)

    override fun onContinueClicked() = emitEffect(WelcomeScreenEffects.ContinueAsGuest)

    override fun onExit() = emitEffect(WelcomeScreenEffects.ExitApp)
}