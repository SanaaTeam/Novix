package search.usecase.search_param

data class RecentViewedMedia(
    val id: Int,
    val posterImageUrl: String,
    val mediaType: MediaType,
    val isSaved: Boolean,
)