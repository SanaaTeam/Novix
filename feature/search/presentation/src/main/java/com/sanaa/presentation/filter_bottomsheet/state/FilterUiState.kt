package com.sanaa.presentation.filter_bottomsheet.state

data class FilterUiState(
    val movieGenres: List<GenreUiState> = emptyList(),
    val tvGenres: List<GenreUiState> = emptyList(),
    val movieFilters: MediaTabFilters = MediaTabFilters(),
    val tvFilters: MediaTabFilters = MediaTabFilters(),
    val isLoading: Boolean = false
)


data class MediaTabFilters(
    val yearRange: ClosedFloatingPointRange<Float> = 1850f..2025f,
    val selectedGenres: Set<GenreUiState> = emptySet(),
    val imdbRating: Int = 0,
){
    val hasUserSelectedFilters: Boolean
        get() = selectedGenres.isNotEmpty() || imdbRating > 0 || yearRange != 1850f..2025f

}

data class GenreUiState(
    val id: Int = 0,
    val name: String = ""
)