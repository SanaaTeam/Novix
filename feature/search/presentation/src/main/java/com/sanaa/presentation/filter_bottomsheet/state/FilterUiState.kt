package com.sanaa.presentation.filter_bottomsheet.state

import entity.Genre

data class FilterUiState(
    val yearRange: ClosedFloatingPointRange<Float> = 1980f..2025f,
    val allGenres: List<String> = emptyList(),
    val selectedGenres: Set<String> = emptySet(),
    val imdbRating: Int = 0,
    val isLoading: Boolean = false,
    val isDefaultState: Boolean = true,
)