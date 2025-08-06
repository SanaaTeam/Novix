package com.sanaa.presentation.screen.playlistDetails

import com.sanaa.presentation.screen.playlistDetails.state.MediaItem

interface PlaylistDetailsInteractionListener {
    fun onMediaClick(mediaId: Int)
    fun onSaveIconClick(mediaItem: MediaItem)
    fun onBackClick()
}