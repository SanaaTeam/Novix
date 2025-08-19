package com.sanaa.tvapp.presentation.screens.category

sealed interface CategoriesScreenEffects {
    data class NavigateToTvGenreDetails(val genreId: Int, val genreName: String) :
        CategoriesScreenEffects

    data class NavigateToMovieGenreDetails(val genreId: Int, val genreName: String) :
        CategoriesScreenEffects
}