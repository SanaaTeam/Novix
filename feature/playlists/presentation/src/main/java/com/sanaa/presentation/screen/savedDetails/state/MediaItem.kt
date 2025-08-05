package com.sanaa.presentation.screen.savedDetails.state

data class MediaItem(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val isSaved: Boolean? = null,
    val mediaTypeUi: MediaTypeUi,
)

enum class MediaTypeUi {
    ALL, MOVIE, TV_SHOW,
}
