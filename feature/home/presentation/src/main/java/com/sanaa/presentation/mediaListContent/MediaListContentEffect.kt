package com.sanaa.presentation.mediaListContent

import com.sanaa.presentation.model.MediaType


sealed class MediaListContentEffect {
    data class NavigateToMediaDetails(val id: Int, val mediaType: MediaType) : MediaListContentEffect()
}