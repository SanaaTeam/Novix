package com.sanaa.presentation.screen.mediaWithTypeTabScreen

import com.sanaa.presentation.state.MediaType

sealed class MediaWithTypeTabScreenEffect {
    object NavigateBack : MediaWithTypeTabScreenEffect()
    data class NavigateToMediaDetails(val id: Int, val mediaType: MediaType) : MediaWithTypeTabScreenEffect()
}