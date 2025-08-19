package com.sanaa.presentation.screen.watchingHistory

import com.sanaa.presentation.screen.myRating.MediaTypeUi

sealed class WatchingHistoryScreenEffect {
    object NavigateBack : WatchingHistoryScreenEffect()
    data class NavigateToMediaDetails(val id: Int, val mediaTypeUi: MediaTypeUi) : WatchingHistoryScreenEffect()
}
