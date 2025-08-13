package com.sanaa.presentation.screen.mediaTabScreen.watchingHistoryScreen

import com.sanaa.presentation.state.MediaItemUiState
import com.sanaa.presentation.state.MediaTypeUi

interface WatchingMediaHistoryScreenInteractionListener {
    fun onMediaTabSelection(mediaTypeUi: MediaTypeUi)
    fun onMovieGenreClick(id: Int?)
    fun onTvShowGenreClick(id: Int?)
    fun onMediaClick(id: Int, mediaTypeUi: MediaTypeUi)
    fun onSaveIconClick(media: MediaItemUiState)
    fun onBackClick()
    fun onRetryClick()
    fun onSnackBarDismiss()
}