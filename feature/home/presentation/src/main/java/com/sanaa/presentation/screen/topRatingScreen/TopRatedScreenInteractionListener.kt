package com.sanaa.presentation.screen.topRatingScreen

import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaTypeUi

interface TopRatedScreenInteractionListener {
    fun onMediaTabSelection(mediaTypeUiState: MediaTypeUi)
    fun onMovieGenreClick(id: Int?)
    fun onTvShowGenreClick(id: Int?)
    fun onMediaClick(id: Int, mediaTypeUiState: MediaTypeUi)
    fun onSaveIconClick(media: MediaItem)
    fun onSaveToListSuccess()
    fun onSaveToListFailure()
    fun onBackClick()
    fun onLoginButtonClick()
    fun onDismissLoginBottomSheet()
    fun onDismissSaveToListBottomSheet()
    fun onCreateNewListClick()
    fun onDismissAddListBottomSheet()
    fun onRetryClick()
    fun onSnackBarDismiss()
}