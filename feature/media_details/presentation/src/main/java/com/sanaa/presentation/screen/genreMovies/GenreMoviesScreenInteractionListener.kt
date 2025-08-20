package com.sanaa.presentation.screen.genreMovies

import com.sanaa.presentation.model.MovieUiModel

interface GenreMoviesScreenInteractionListener:GenreMoviesGridInteractionListener {
    fun onRetryClicked()
    fun onBackClick()
    fun onBottomSheetDismiss()
    fun onLoginButtonClick()
    fun onDismissSaveToListBottomSheet()
    fun onDismissAddListBottomSheet()
    fun onCreateNewListClick()
    fun onSnackDismissRequested()
}
interface GenreMoviesGridInteractionListener {
    fun onMovieClick(id: Int)
    fun onSaveIconClick(media: MovieUiModel)
}