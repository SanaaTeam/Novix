package com.sanaa.presentation.screen.movie_categories

import com.sanaa.presentation.model.MovieUiModel

data class MovieCategoriesScreenUiState(
    val title: String? = null,
    val movies: List<MovieUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showBottomSheet: Boolean = false
)