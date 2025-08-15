package entity

import usecase.search.search_param.MediaType

data class MediaHistoryItem(
    val id: Int,
    val posterImageUrl: String,
    val mediaType: MediaType,
    val genres: List<Genre>,
    val lastWatchedAt: Long,
)