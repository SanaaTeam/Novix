package com.sanaa.tvapp.presentation.screens.genreTvShows

interface GenreTvShowsScreenInteractionListener {
    fun onSaveIconClick()
    fun onBackClick()
    fun onTvShowClick(id: Int)
    fun onBottomSheetDismiss()
    fun onRetryClick()
    fun onLoginButtonClick()
}