package com.sanaa.presentation.screen.playlistDetails

import com.sanaa.presentation.screen.playlistDetails.state.MediaItem
import com.sanaa.presentation.screen.playlistDetails.state.MediaTypeUi

interface PlaylistDetailsInteractionListener {
    fun onMediaClick(mediaId: Int, mediaType: MediaTypeUi)
    fun onSaveIconClick(mediaItem: MediaItem)
    fun onBackClick()
    fun onDeleteListClicked()
    fun onDismissBottomSheet()
    fun onListDeletedSuccessfully()

}