package com.sanaa.presentation.screen.movie_categories

sealed class MovieCategoriesScreenEffects {
    object NavigateBack : MovieCategoriesScreenEffects()
    data class NavigateToMovieDetails(val id: Int) : MovieCategoriesScreenEffects()
}