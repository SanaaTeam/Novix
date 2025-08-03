package com.sanaa.presentation.screen.myAccount

sealed interface MyAccountScreenEffect {
    object NavigateToWatchingHistory : MyAccountScreenEffect
    object NavigateToMyRating : MyAccountScreenEffect
    object NavigateToContentRestrictionSetting : MyAccountScreenEffect
    object NavigateToChangePasswordSetting : MyAccountScreenEffect
    object NavigateToLanguageSetting : MyAccountScreenEffect
}