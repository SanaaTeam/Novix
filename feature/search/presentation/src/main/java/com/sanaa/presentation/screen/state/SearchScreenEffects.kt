package com.sanaa.presentation.screen.state

sealed class SearchScreenEffects {
    data class NavigateToMovieDetails(val id: Int) : SearchScreenEffects()
    data class NavigateToTvShowDetails(val id: Int) : SearchScreenEffects()
    data class NavigateToActorDetails(val id: Int) : SearchScreenEffects()

    object NavigateToLogin : SearchScreenEffects()

}