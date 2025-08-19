package com.sanaa.presentation.screen.genreMovies

import com.sanaa.presentation.model.MovieUiModel

interface GenreMoviesScreenInteractionListener {
    fun onSaveIconClick(media: MovieUiModel)
    fun onBackClick()
    fun onMovieClick(id: Int)
    fun onBottomSheetDismiss()
    fun onRetryClicked()
    fun onLoginButtonClick()
    fun onDismissSaveToListBottomSheet()
    fun onDismissAddListBottomSheet()
    fun onCreateNewListClick()
    fun onSnackDismissRequested()
}