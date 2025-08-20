package com.sanaa.presentation.screen.genreMovies

sealed class GenreMoviesEffects {
    object NavigateBack : GenreMoviesEffects()
    data class NavigateToMovieDetails(val id: Int) : GenreMoviesEffects()
    object NavigateToLogin : GenreMoviesEffects()
}