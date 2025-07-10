package usecase.search

data class RecentViewedMedia(
    val id: Int,
    val posterImageUrl: String,
    val mediaType: MediaType,
)