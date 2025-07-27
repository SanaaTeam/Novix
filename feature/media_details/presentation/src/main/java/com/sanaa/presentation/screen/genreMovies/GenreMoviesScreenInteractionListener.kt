package com.sanaa.presentation.screen.genreMovies

interface GenreMoviesScreenInteractionListener {
    fun onSaveIconClick()
    fun onBackClick()
    fun onMovieClick(id: Int)
    fun onBottomSheetDismiss()
    fun onLoginButtonClick()
}