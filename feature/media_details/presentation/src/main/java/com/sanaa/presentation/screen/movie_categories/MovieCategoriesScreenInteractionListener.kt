package com.sanaa.presentation.screen.movie_categories

interface MovieCategoriesScreenInteractionListener {
    fun onSaveIconClick()
    fun onBackClick()
    fun onMovieClick(id: Int)
    fun onBottomSheetDismiss()
}