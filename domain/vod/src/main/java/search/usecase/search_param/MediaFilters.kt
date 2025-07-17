package search.usecase.search_param

import entity.Genre

data class MediaFilters(
    val startYear: Int,
    val endYear: Int,
    val genres: List<Genre> = emptyList(),
    val imdbRating: Float
)