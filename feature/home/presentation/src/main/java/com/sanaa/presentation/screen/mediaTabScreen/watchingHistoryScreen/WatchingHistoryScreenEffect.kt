package com.sanaa.presentation.screen.mediaTabScreen.watchingHistoryScreen

import com.sanaa.presentation.state.MediaTypeUi

sealed interface WatchingHistoryScreenEffect {
    object NavigateBack : WatchingHistoryScreenEffect
    data class NavigateToMediaDetails(val id: Int, val mediaTypeUi: MediaTypeUi) : WatchingHistoryScreenEffect
}