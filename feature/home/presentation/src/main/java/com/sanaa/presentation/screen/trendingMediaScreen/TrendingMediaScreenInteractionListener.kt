package com.sanaa.presentation.screen.trendingMediaScreen

import com.sanaa.presentation.state.MediaItem

interface MediaListScreenInteractionListener {
    fun onGenreClick(id: Int?)
    fun onMediaClick(id: Int)
    fun onSaveIconClick(media: MediaItem)
    fun onBackClick()
    fun onRetryClick()
    fun onLoginButtonClick()
    fun onDismissBottomSheet()
}