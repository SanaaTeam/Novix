package com.sanaa.presentation.screen.mediaTabScreen.watchingHistoryScreen

import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaTypeUi

interface WatchingMediaHistoryScreenInteractionListener {
    fun onMediaTabSelection(mediaTypeUi: MediaTypeUi)
    fun onMovieGenreClick(id: Int?)
    fun onTvShowGenreClick(id: Int?)
    fun onMediaClick(id: Int, mediaTypeUi: MediaTypeUi)
    fun onSaveIconClick(media: MediaItem)
    fun onBackClick()
    fun onRetryClick()
}