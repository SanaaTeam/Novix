package com.sanaa.presentation.state

data class MediaItemUiState(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val rating: String? = null,
    val mediaTypeUiState: MediaTypeUiState,
    val isSaved: Boolean = false,
)

enum class MediaTypeUiState {
    MOVIE, TV_SHOW,
}
