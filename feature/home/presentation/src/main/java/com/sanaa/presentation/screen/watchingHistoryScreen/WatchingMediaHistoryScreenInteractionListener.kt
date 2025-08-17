package com.sanaa.presentation.screen.watchingHistoryScreen

import com.sanaa.presentation.state.MediaItemUiState
import com.sanaa.presentation.state.MediaTypeUiState

interface WatchingMediaHistoryScreenInteractionListener {
    fun onMediaTabSelection(mediaTypeUiState: MediaTypeUiState)
    fun onMovieGenreClick(id: Int?)
    fun onTvShowGenreClick(id: Int?)
    fun onMediaClick(id: Int, mediaTypeUiState: MediaTypeUiState)
    fun onSaveIconClick(media: MediaItemUiState)
    fun onBackClick()
    fun onRetryClick()
    fun onDismissSaveToListBottomSheet()
    fun onCreateNewListClick()
    fun onDismissAddListBottomSheet()
    fun onLoginButtonClick()
    fun onDismissLoginBottomSheet()
    fun onSnackBarDismiss()
}