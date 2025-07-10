package com.sanaa.presentation.filter_bottomsheet

data class FilterUiState(
    val yearRange: ClosedFloatingPointRange<Float> = 1995f..2012f,
    val allGenres: List<String> = emptyList(),
    val selectedGenre: String = "Animation",
    val imdbRating: Int = 8,
    val isLoading: Boolean = true
)