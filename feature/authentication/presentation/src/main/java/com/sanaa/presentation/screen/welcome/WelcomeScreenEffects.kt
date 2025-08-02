package com.sanaa.presentation.screen.welcome

sealed class WelcomeScreenEffects {
    object NavigateToLogin : WelcomeScreenEffects()
    object ReturnGuestResultCode : WelcomeScreenEffects()
    object ExitApp : WelcomeScreenEffects()
}