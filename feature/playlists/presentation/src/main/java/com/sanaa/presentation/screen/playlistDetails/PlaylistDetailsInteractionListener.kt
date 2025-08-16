package com.sanaa.presentation.screen.playlistDetails

import com.sanaa.presentation.screen.playlistDetails.state.MediaItem
import com.sanaa.presentation.screen.playlistDetails.state.MediaTypeUi

interface PlaylistDetailsInteractionListener {
    fun onMediaClick(mediaId: Int, mediaType: MediaTypeUi)
    fun onDeleteIconClick(mediaItem: MediaItem)
    fun onBackClick()
    fun onDeleteListClick()
    fun onDeleteListConfirmed()
    fun onDismissBottomSheet()
    fun onListDeletedSuccessfully()
    fun onSnackBarDismiss()
}