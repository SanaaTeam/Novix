package com.sanaa.tvapp.presentation.screens.mediaDetails.movieScreen

sealed class MovieDetailsScreenUiEffect {
    data class OpenTrailer(val url: String?) : MovieDetailsScreenUiEffect()
    data class NavigateToAnotherMovieDetails(val movieId: Int) : MovieDetailsScreenUiEffect()
    data class NavigateToActorScreen(val actorId: Int) : MovieDetailsScreenUiEffect()
    object NavigateToLogin : MovieDetailsScreenUiEffect()
}