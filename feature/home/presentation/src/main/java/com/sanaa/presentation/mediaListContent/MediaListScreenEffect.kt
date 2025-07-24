package com.sanaa.presentation.mediaListContent

import com.sanaa.presentation.model.MediaType


sealed class MediaListScreenEffect {
    object NavigateBack : MediaListScreenEffect()
    data class NavigateToMediaDetails(val id: Int, val mediaType: MediaType) : MediaListScreenEffect()
}