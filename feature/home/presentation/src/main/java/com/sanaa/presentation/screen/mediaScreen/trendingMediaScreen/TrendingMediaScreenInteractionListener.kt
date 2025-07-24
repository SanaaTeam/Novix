package com.sanaa.presentation.screen.mediaScreen.trendingMediaScreen

import com.sanaa.presentation.state.MediaItem

interface MediaListScreenInteractionListener {
    fun onGenreClick(id: Int?)
    fun onMediaClick(id: Int)
    fun onSaveIconClick(media: MediaItem)
    fun onBackClick()
}