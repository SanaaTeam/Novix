package com.sanaa.presentation.filter_bottomsheet

import entity.Genre

data class FilterUiState(
    val yearRange: ClosedFloatingPointRange<Float> = 1995f..2012f,
    val allGenres: List<Genre> = emptyList(),
    val selectedGenres: Set<Genre> = emptySet(),
    val imdbRating: Int = 8,
    val isLoading: Boolean = true
)
