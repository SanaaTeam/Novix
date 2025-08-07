package com.sanaa.presentation.screen.mediaTabScreen.watchingHistoryScreen

import com.sanaa.presentation.state.MediaTypeUi

sealed interface WatchingMediaHistoryScreenEffect {
    object NavigateBack : WatchingMediaHistoryScreenEffect
    data class NavigateToMediaDetails(val id: Int, val mediaTypeUi: MediaTypeUi) : WatchingMediaHistoryScreenEffect
    data class ShowError(val message: String) : WatchingMediaHistoryScreenEffect
}