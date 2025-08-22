package com.sanaa.presentation.screen.topShowsScreen

interface TopTvShowsScreenInteractionListener {
    fun onRetryClicked()
    fun onSnackDismissRequested()
    fun onBackClick()
    fun onTvShowClick(id: Int)
}