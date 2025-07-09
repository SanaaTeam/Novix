package usecase.search

import entity.Genre

data class MediaFilters(
    val startYear: Int? = null,
    val endYear: Int? = null,
    val genres: List<Genre>? = null,
    val imdbRating: Float? = null
)