package com.sanaa.tvapp.presentation.screens.mediaDetails.actorScreen

sealed class ActorScreenEffects {
    data class NavigateToMovieDetails(val movieId: Int) : ActorScreenEffects()
    data class NavigateToSeriesDetails(val seriesId: Int) : ActorScreenEffects()
    object NavigateToLogin : ActorScreenEffects()
}
