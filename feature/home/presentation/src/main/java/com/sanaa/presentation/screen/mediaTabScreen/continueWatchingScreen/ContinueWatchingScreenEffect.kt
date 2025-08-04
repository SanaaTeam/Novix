package com.sanaa.presentation.screen.mediaTabScreen.continueWatchingScreen

import com.sanaa.presentation.state.MediaTypeUi

sealed class ContinueWatchingScreenEffect {
    object NavigateBack : ContinueWatchingScreenEffect()
    data class NavigateToMediaDetails(val id: Int, val mediaTypeUi: MediaTypeUi) : ContinueWatchingScreenEffect()
}