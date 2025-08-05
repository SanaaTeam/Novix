package com.sanaa.presentation.screen.savedDetails

import com.sanaa.presentation.screen.savedDetails.state.MediaItem

interface SavedDetailsInteractionListener {
    fun onMediaClick(mediaId: Int)
    fun onSaveIconClick(mediaItem: MediaItem)
}