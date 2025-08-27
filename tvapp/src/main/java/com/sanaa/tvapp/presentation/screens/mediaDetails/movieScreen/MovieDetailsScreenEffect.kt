package com.sanaa.tvapp.presentation.screens.mediaDetails.movieScreen

sealed interface MovieDetailsScreenEffect {
    data class OpenTrailer(val url: String?) : MovieDetailsScreenEffect
    data class NavigateToAnotherMovieDetails(val movieId: Int) : MovieDetailsScreenEffect
    data class NavigateToActorScreen(val actorId: Int) : MovieDetailsScreenEffect
    object NavigateToLogin : MovieDetailsScreenEffect
    object UpdateRate : MovieDetailsScreenEffect
}