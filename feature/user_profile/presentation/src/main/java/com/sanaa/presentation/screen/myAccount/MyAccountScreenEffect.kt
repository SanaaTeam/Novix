package com.sanaa.presentation.screen.myAccount

sealed interface MyAccountScreenEffect {
    object NavigateToWatchingHistory : MyAccountScreenEffect
    object NavigateToMyRating : MyAccountScreenEffect
    object NavigateToContentRestrictionSetting : MyAccountScreenEffect
    object NavigateToChangePasswordSetting : MyAccountScreenEffect
    data class UpdateAppLanguage(
        val language: String
    ) : MyAccountScreenEffect

    data class UpdateAppTheme(
        val isDarkMode: Boolean
    ): MyAccountScreenEffect

    object NavigateToLogin : MyAccountScreenEffect
    object PopBackStackToWelcomeScreen: MyAccountScreenEffect
}