package com.sanaa.presentation.screen.actorGallery

data class ActorGalleryScreenUiState(
    val galleryImageUrls: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val noInternetConnection: Boolean = false,
)