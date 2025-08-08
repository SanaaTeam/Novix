package com.sanaa.presentation.screen.playlist

sealed class PlayListScreenEffect{
    object NavigateToLogin : PlayListScreenEffect()
    data object  ShowErrorToAddListSnackBar : PlayListScreenEffect()
    data object ShowSuccessToDeleteListSnackBar : PlayListScreenEffect()
    data object  ShowErrorToDeleteListSnackBar : PlayListScreenEffect()
    data object ShowSuccessToAddListSnackBar : PlayListScreenEffect()
    data class NavigateToSavedDetails(val listId: Int, val title: String) : PlayListScreenEffect()
}