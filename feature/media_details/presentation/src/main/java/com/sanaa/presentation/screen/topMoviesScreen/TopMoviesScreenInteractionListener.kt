package com.sanaa.presentation.screen.topMoviesScreen

import com.sanaa.presentation.model.MovieUiModel

interface TopMoviesScreenInteractionListener {
    fun onDismissLoginBottomSheet()
    fun onSaveClicked(movie: MovieUiModel)
    fun onRetryClicked()
    fun onDismissSaveToListBottomSheet()
    fun onCreateNewListClick()
    fun onDismissAddListBottomSheet()
    fun onSnackBarDismiss()
    fun onBackClick()
    fun onLoginButtonClick()
    fun onMovieClick(id: Int)
}