package com.sanaa.presentation.screen.trendingMediaScreen

import com.sanaa.presentation.state.MediaItemUiState

interface MediaListScreenInteractionListener {
    fun onGenreClick(id: Int?)
    fun onMediaClick(id: Int)
    fun onSaveIconClick(media: MediaItemUiState)
    fun onSaveToListSuccess()
    fun onSaveToListFailure()
    fun onBackClick()
    fun onRetryClick()
    fun onLoginButtonClick()
    fun onDismissLoginBottomSheet()
    fun onDismissSaveToListBottomSheet()
    fun onDismissAddListBottomSheet()
    fun onCreateNewListClick()
}