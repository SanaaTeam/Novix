package com.sanaa.presentation.screen.watchingHistoryScreen

import com.sanaa.presentation.state.MediaTypeUi

sealed interface WatchingMediaHistoryScreenEffect {
    object NavigateBack : WatchingMediaHistoryScreenEffect
    object NavigateToLogin : WatchingMediaHistoryScreenEffect
    data class ShowError(val message: String) : WatchingMediaHistoryScreenEffect
    data class NavigateToMediaDetails(val id: Int, val mediaTypeUiState: MediaTypeUi) :
        WatchingMediaHistoryScreenEffect
}