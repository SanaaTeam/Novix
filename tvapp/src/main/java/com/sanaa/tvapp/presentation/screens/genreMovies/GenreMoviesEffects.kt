package com.sanaa.tvapp.presentation.screens.genreMovies

sealed class GenreMoviesEffects {
    object NavigateBack : GenreMoviesEffects()
    data class NavigateToMovieDetails(val id: Int) : GenreMoviesEffects()
}
