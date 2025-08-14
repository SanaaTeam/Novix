package com.sanaa.presentation.screen.mediaTabScreen.watchingHistoryScreen

import com.sanaa.presentation.state.MediaTypeUiState

sealed interface WatchingMediaHistoryScreenEffect {
    object NavigateBack : WatchingMediaHistoryScreenEffect
    data class NavigateToMediaDetails(val id: Int, val mediaTypeUiState: MediaTypeUiState) : WatchingMediaHistoryScreenEffect
    data class ShowError(val message: String) : WatchingMediaHistoryScreenEffect
}