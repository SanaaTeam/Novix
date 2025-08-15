package com.sanaa.tvapp.state

data class MediaItem(
    val id: Int,
    val title: String,
    val overview: String,
    val imageUrl: String?,
    val rating: String? = null,
    val mediaTypeUi: MediaTypeUi,
    val isSaved: Boolean = false,
)

enum class MediaTypeUi {
    MOVIE, TV_SHOW,
}
