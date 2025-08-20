package com.sanaa.presentation.screen.genreTvShows

interface GenreTvShowsScreenInteractionListener {
    fun onSaveIconClick()
    fun onBackClick()
    fun onTvShowClick(id: Int)
    fun onBottomSheetDismiss()
    fun onRetryClick()
    fun onLoginButtonClick()
    fun onSnackDismissRequested()
}