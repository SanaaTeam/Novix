package com.sanaa.presentation.screen.watchingHistoryScreen

import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaTypeUi

interface WatchingMediaHistoryScreenInteractionListener {
    fun onMediaTabSelection(mediaTypeUiState: MediaTypeUi)
    fun onMovieGenreClick(id: Int?)
    fun onTvShowGenreClick(id: Int?)
    fun onMediaClick(id: Int, mediaTypeUiState: MediaTypeUi)
    fun onSaveIconClick(media: MediaItem)
    fun onBackClick()
    fun onRetryClick()
    fun onDismissSaveToListBottomSheet()
    fun onCreateNewListClick()
    fun onDismissAddListBottomSheet()
    fun onLoginButtonClick()
    fun onDismissLoginBottomSheet()
    fun onSnackBarDismiss()
}