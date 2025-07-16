package com.sanaa.presentation.screen.state

data class MovieCategoriesScreenUiState(
    val title: String = "",
    val movies: List<MovieCardUiModel> = emptyList()
)

data class MovieCardUiModel(
    val id: Int = 0,
    val title: String = "",
    val imageUrl: String = "",
    val rating: String = "",
)