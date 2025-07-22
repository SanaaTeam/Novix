package com.sanaa.presentation.model

data class MediaItem(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val mediaType: MediaType,
)

enum class MediaType {
    MOVIE, TV_SHOW,
}
