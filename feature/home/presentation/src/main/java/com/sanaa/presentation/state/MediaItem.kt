package com.sanaa.presentation.state

data class MediaItem(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val rating: String? = null,
    val mediaType: MediaType,
)

enum class MediaType {
    MOVIE, TV_SHOW,
}
