package search.usecase.search_param

import entity.Genre

data class MediaFilters(
    val startYear: Int? = null,
    val endYear: Int? = null,
    val genres: List<Genre> = emptyList(),
    val imdbRating: Float? = null
)