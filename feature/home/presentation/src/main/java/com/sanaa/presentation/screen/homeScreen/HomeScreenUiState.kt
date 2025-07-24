package com.sanaa.presentation.screen.homeScreen

import com.sanaa.presentation.model.MediaItem

data class HomeScreenUiState(
    val popularMedia: List<MediaItem> = emptyList(),
    val topRatingMedia: List<MediaItem> = emptyList(),
    val continueWatchingMedia: List<MediaItem> = emptyList(),
    val isLoading : Boolean = false,
    val errorMessage: String? = null
)
