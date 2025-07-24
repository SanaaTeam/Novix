package com.sanaa.presentation.screen.mediaTabScreen

import com.sanaa.presentation.state.MediaType

sealed class MediaTabScreenEffect {
    object NavigateBack : MediaTabScreenEffect()
    data class NavigateToMediaDetails(val id: Int, val mediaType: MediaType) : MediaTabScreenEffect()
}