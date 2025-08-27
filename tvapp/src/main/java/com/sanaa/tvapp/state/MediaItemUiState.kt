package com.sanaa.tvapp.state

data class MediaItemUiState(
    val id: Int,
    val title: String,
    val overview: String,
    val imageUrl: String?,
    val imdbRating: String? = null,
    val userRating: String? = null,
    val mediaTypeUiState: MediaTypeUiState,
)

enum class MediaTypeUiState {
    MOVIE, TV_SHOW,
}
