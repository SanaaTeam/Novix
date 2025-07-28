package com.sanaa.presentation.screen.login

sealed interface LoginScreenEffects {
    data object NavigateBack : LoginScreenEffects
    data object NavigateToHome : LoginScreenEffects
    data object NavigateToForgotPassword : LoginScreenEffects
    data class NavigateApproveAccessToken(val requestToken: String) : LoginScreenEffects
    data object NavigateToCreateAccount : LoginScreenEffects
    data class ShowError(val message: String) : LoginScreenEffects
    data class ShowSuccess(val message: String) : LoginScreenEffects
}