package com.sanaa.presentation.screen.playlist

sealed class PlayListScreenEffect{
    object NavigateToLogin : PlayListScreenEffect()
    data object  ShowErrorSnackBar : PlayListScreenEffect()
    data object ShowSuccessSnackBar : PlayListScreenEffect()
    data class NavigateToSavedDetails(val listId: Int, val title: String) : PlayListScreenEffect()
}