package com.sanaa.presentation.screen.playlist

sealed class PlayListScreenEffect {
    object NavigateToLogin : PlayListScreenEffect()
    data class NavigateToSavedDetails(val listId: Int, val title: String) : PlayListScreenEffect()
    data class ShowSuccess(val message: String) : PlayListScreenEffect()
    data class ShowError(val message: String) : PlayListScreenEffect()
}
