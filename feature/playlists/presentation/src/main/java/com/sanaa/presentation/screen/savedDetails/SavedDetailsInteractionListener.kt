package com.sanaa.presentation.screen.savedDetails

import com.sanaa.presentation.screen.savedDetails.state.MediaItem
import com.sanaa.presentation.screen.savedDetails.state.MediaTypeUi

interface SavedDetailsInteractionListener {
    fun onMediaClick(mediaId: Int, mediaType: MediaTypeUi)
    fun onSaveIconClick(mediaItem: MediaItem)
    fun onMediaTypeClick(mediaType: MediaTypeUi)
}