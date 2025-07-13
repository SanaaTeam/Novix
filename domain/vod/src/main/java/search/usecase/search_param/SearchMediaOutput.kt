package search.usecase.search_param

data class SearchMediaOutput(
    val id: Int,
    val title: String,
    val posterImageUrl: String,
    val isSaved: Boolean
)