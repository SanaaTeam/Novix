package com.sanaa.presentation.screen.welcome

sealed class WelcomeScreenEffects {
    object NavigateToLogin : WelcomeScreenEffects()
    object ContinueAsGuest : WelcomeScreenEffects()
    object ExitApp : WelcomeScreenEffects()
}