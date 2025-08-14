package com.sanaa.presentation.screen.trendingMoviesScreen

import com.sanaa.presentation.state.MediaItem

interface TrendingMoviesScreenInteractionListener {
    fun onGenreClick(id: Int?)
    fun onMediaClick(id: Int)
    fun onSaveIconClick(media: MediaItem)
    fun onSaveToListSuccess()
    fun onSaveToListFailure()
    fun onBackClick()
    fun onRetryClick()
    fun onLoginButtonClick()
    fun onDismissLoginBottomSheet()
    fun onDismissSaveToListBottomSheet()
    fun onDismissAddListBottomSheet()
    fun onCreateNewListClick()
    fun onSnackBarDismiss()
}