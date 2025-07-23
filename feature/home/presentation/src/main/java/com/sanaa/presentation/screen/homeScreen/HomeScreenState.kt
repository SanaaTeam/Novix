package com.sanaa.presentation.screen.homeScreen

import com.sanaa.presentation.model.MediaItem

data class HomeScreenState(
    val popularMedia: List<MediaItem>,
    val topRatingMedia: List<MediaItem>,
    val continueWatchingMedia: List<MediaItem>
)
