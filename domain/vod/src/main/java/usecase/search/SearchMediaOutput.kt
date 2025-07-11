package usecase.search

data class SearchMediaOutput(
    val id: Int,
    val title: String,
    val posterImageUrl: String,
    val isSaved: Boolean
)