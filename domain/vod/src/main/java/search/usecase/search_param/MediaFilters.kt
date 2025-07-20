package search.usecase.search_param

import entity.Genre

data class MediaFilters(
    val startYear: Int = 1980,
    val endYear: Int = 2025,
    val genres: List<Genre> = emptyList(),
    val imdbRating: Float = 0f
)