package com.sanaa.presentation.screen.topMoviesScreen

import com.sanaa.presentation.model.MovieUiModel

interface TopMoviesScreenInteractionListener {
    fun onDismissBottomSheet()
    fun onSaveClicked(movie: MovieUiModel)
    fun onRetryClicked()
    fun onDismissSaveToListBottomSheet()
    fun onCreateNewListClick()
    fun onDismissAddListBottomSheet()
    fun onSnackBarDismiss()
}