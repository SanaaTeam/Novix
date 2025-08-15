package com.sanaa.presentation.screen.watchingHistoryScreen

import com.sanaa.presentation.state.MediaTypeUiState

sealed interface WatchingMediaHistoryScreenEffect {
    object NavigateBack : WatchingMediaHistoryScreenEffect
    object NavigateToLogin : WatchingMediaHistoryScreenEffect
    data class NavigateToMediaDetails(val id: Int, val mediaTypeUiState: MediaTypeUiState) :
        WatchingMediaHistoryScreenEffect
}