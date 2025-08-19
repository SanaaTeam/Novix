package com.sanaa.presentation.screen.actorGallery

import com.sanaa.presentation.screen.movieDetails.SnackData

data class ActorGalleryScreenUiState(
    val galleryImageUrls: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val noInternetConnection: Boolean = false,
    val snackBarData: SnackData? = null
)