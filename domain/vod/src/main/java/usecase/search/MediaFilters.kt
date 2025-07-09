package usecase.search

import entity.Genre

data class MediaFilters(
    val releaseYear: Int? = null,
    val genres: List<Genre>? = null,
    val imdbRating: Float? = null
)