package com.sanaa.presentation.screen.mediaTabScreen

import com.sanaa.presentation.state.MediaTypeUi

sealed class MediaTabScreenEffect {
    object NavigateBack : MediaTabScreenEffect()
    data class NavigateToMediaDetails(val id: Int, val mediaTypeUi: MediaTypeUi) : MediaTabScreenEffect()
}