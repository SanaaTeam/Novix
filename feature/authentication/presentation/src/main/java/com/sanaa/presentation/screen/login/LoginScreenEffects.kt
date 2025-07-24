package com.sanaa.presentation.screen.login

sealed class LoginScreenEffects {
    object NavigateBack : LoginScreenEffects()
    object NavigateToHome : LoginScreenEffects()
    object NavigateToForgotPassword : LoginScreenEffects()
    object NavigateToCreateAccount : LoginScreenEffects()
    data class ShowError(val message: String) : LoginScreenEffects()
    data class ShowSuccess(val message: String) : LoginScreenEffects()
}