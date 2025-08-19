package com.sanaa.tvapp.presentation.screens.myAccount

sealed interface MyAccountScreenEffect {
    object NavigateToWatchingHistory : MyAccountScreenEffect
    object NavigateToMyRating : MyAccountScreenEffect
    object NavigateToChangePasswordSetting : MyAccountScreenEffect
    data class UpdateAppLanguage(
        val language: String
    ) : MyAccountScreenEffect
    object NavigateToLogin : MyAccountScreenEffect
    object Recreate : MyAccountScreenEffect
}