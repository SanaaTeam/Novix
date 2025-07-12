package com.sanaa.presentation.filter_bottomsheet

import entity.Genre

data class FilterUiState(
    val yearRange: ClosedFloatingPointRange<Float> = 1980f..2025f,
    val allGenres: List<Genre> = emptyList(),
    val selectedGenres: Set<Genre> = emptySet(),
    val imdbRating: Int = 8,
    val isLoading: Boolean = true,
    val isDefaultState: Boolean = true,
)
