package com.sanaa.presentation.screen.movieDetails

sealed class MovieDetailsUiEffect {
    object NavigateBack : MovieDetailsUiEffect()
    data class NavigateToReviewsScreen(val movieId: Int) : MovieDetailsUiEffect()
    data class OpenTrailer(val url: String?) : MovieDetailsUiEffect()
    data class NavigateToAnotherMovieDetails(val movieId: Int) : MovieDetailsUiEffect()
    data class NavigateToActorScreen(val actorId: Int) : MovieDetailsUiEffect()
    data class NavigateToMovieCategoriesScreen(val categoryId: Int, val categoryName: String) : MovieDetailsUiEffect()
    data object  ShowErrorSnackBar : MovieDetailsUiEffect()
    data object ShowSuccessSnackBar : MovieDetailsUiEffect()
    object NavigateToLogin : MovieDetailsUiEffect()
}