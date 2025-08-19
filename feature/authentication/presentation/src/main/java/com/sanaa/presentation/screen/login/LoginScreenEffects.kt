package com.sanaa.presentation.screen.login

sealed interface LoginScreenEffects {
    data object NavigateBack : LoginScreenEffects
    data object ReturnLoggedInResultCode : LoginScreenEffects
    data object NavigateToForgotPassword : LoginScreenEffects
    data object NavigateToCreateAccount : LoginScreenEffects
}