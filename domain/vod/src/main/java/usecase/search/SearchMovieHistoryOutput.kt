package usecase.search

data class SearchMovieHistoryOutput(
    val id: Long? = null,
    val title: String,
    val posterImageUrl: String,
    val isSaved: Boolean = false
)