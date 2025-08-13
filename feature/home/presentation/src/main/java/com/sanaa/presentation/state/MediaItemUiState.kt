package com.sanaa.presentation.state

data class MediaItemUiState(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val rating: String? = null,
    val mediaTypeUi: MediaTypeUi,
    val isSaved: Boolean = false,
)

enum class MediaTypeUi {
    MOVIE, TV_SHOW,
}
