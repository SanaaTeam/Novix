package com.sanaa.presentation.screen.watchingHistoryScreen

import com.sanaa.presentation.state.MediaTypeUi

sealed interface WatchingMediaHistoryScreenEffect {
    object NavigateBack : WatchingMediaHistoryScreenEffect
    data class NavigateToMediaDetails(val id: Int, val mediaTypeUi: MediaTypeUi) :
        WatchingMediaHistoryScreenEffect
}