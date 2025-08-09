package com.sanaa.tvapp.presentation.screens.mediaDetails.movieScreen

import com.sanaa.presentation.screen.movieDetails.MovieDetailsUiEffect

sealed class MovieDetailsScreenUiEffect {
    object NavigateBack : MovieDetailsScreenUiEffect()
    data class OpenTrailer(val url: String?) : MovieDetailsScreenUiEffect()
    data class NavigateToAnotherMovieDetails(val movieId: Int) : MovieDetailsScreenUiEffect()
    data class NavigateToActorScreen(val actorId: Int) : MovieDetailsScreenUiEffect()
    data object  ShowErrorSnackBar : MovieDetailsScreenUiEffect()
    data object ShowSuccessSnackBar : MovieDetailsScreenUiEffect()
    object NavigateToLogin : MovieDetailsScreenUiEffect()
}