package com.sanaa.presentation.screen.mediaTabScreen.topRatingScreen

import com.sanaa.presentation.state.MediaItemUiState
import com.sanaa.presentation.state.MediaTypeUiState

interface TopRatedScreenInteractionListener {
    fun onMediaTabSelection(mediaTypeUiState: MediaTypeUiState)
    fun onMovieGenreClick(id: Int?)
    fun onTvShowGenreClick(id: Int?)
    fun onMediaClick(id: Int, mediaTypeUiState: MediaTypeUiState)
    fun onSaveIconClick(media: MediaItemUiState)
    fun onSaveToListSuccess()
    fun onSaveToListFailure()
    fun onBackClick()
    fun onLoginButtonClick()
    fun onDismissBottomSheet()
    fun onDismissSaveToListBottomSheet()
    fun onCreateNewListClick()
    fun onDismissAddListBottomSheet()
    fun onRetryClick()
}