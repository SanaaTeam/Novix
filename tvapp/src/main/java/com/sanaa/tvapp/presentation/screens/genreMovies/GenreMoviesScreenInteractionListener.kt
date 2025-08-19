package com.sanaa.tvapp.presentation.screens.genreMovies

import com.sanaa.tvapp.presentation.screens.searchScreen.MovieUiModel


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
}