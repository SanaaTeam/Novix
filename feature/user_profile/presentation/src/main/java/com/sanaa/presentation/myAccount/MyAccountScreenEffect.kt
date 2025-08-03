package com.sanaa.presentation.myAccount

sealed interface MyAccountScreenEffect {
    object NavigateToWatchingHistory : MyAccountScreenEffect
    object NavigateToMyRating : MyAccountScreenEffect
    object NavigateToContentRestrictionSetting : MyAccountScreenEffect
    object NavigateToChangePasswordSetting : MyAccountScreenEffect
    object NavigateToLanguageSetting : MyAccountScreenEffect
}