package com.sanaa.tvapp.presentation.screens.myAccount

sealed interface MyAccountScreenEffect {
    object NavigateToWatchingHistory : MyAccountScreenEffect
    object NavigateToMyRating : MyAccountScreenEffect
    object NavigateToChangePasswordSetting : MyAccountScreenEffect
    data class UpdateAppLanguage(
        val language: String
    ) : MyAccountScreenEffect

    data class UpdateAppTheme(
        val isDarkMode: Boolean
    ) : MyAccountScreenEffect

    object NavigateToLogin : MyAccountScreenEffect
    object PopBackStackToWelcomeScreen : MyAccountScreenEffect
}