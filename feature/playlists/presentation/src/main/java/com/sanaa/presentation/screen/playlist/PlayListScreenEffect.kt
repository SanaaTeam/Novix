package com.sanaa.presentation.screen.playlist

sealed interface PlayListScreenEffect {
    object NavigateToLogin : PlayListScreenEffect
    data class NavigateToSavedDetails(val listId: Int, val title: String) : PlayListScreenEffect
}
