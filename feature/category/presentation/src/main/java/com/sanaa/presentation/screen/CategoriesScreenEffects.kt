package com.sanaa.presentation.screen

sealed interface CategoriesScreenEffects {
    data class NavigateToTvGenreDetails(val genreId: Int, val genreName: String) : CategoriesScreenEffects
    data class NavigateToMovieGenreDetails(val genreId: Int, val genreName: String) : CategoriesScreenEffects
}