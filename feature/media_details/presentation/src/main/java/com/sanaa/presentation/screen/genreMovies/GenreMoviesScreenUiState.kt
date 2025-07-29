package com.sanaa.presentation.screen.genreMovies

import com.sanaa.presentation.model.MovieUiModel

data class GenreMoviesScreenUiState(
    val title: String? = null,
    val movies: List<MovieUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showBottomSheet: Boolean = false,
    val noInternetConnection: Boolean = false
)


