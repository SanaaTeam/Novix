package usecase.params

data class MediaFilters(
    val releaseYear: Int? = null,
    val genres: List<String>? = null,
    val imdbRating: Float? = null
)
