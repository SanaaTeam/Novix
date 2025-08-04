package com.sanaa.presentation.screen.login

sealed interface LoginScreenEffects {
    data object NavigateBack : LoginScreenEffects
    data object ReturnLoggedInResultCode : LoginScreenEffects
    data object NavigateToForgotPassword : LoginScreenEffects
    data object NavigateToCreateAccount : LoginScreenEffects
    data class ShowError(val message: String) : LoginScreenEffects
    data class ShowSuccess(val message: String) : LoginScreenEffects
}