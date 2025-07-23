package com.sanaa.presentation.filter_bottomsheet.state

data class FilterUiState(
    val yearRange: ClosedFloatingPointRange<Float> = 1980f..2025f,
    val allGenres: List<GenreUiState> = emptyList(),
    val selectedGenres: Set<GenreUiState> = emptySet(),
    val imdbRating: Int = 0,
    val isLoading: Boolean = false,
)

data class GenreUiState(
    val id: Int = 0,
    val name: String = ""
)