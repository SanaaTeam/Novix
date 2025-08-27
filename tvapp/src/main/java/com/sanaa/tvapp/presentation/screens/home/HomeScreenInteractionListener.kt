package com.sanaa.tvapp.presentation.screens.home

import com.sanaa.tvapp.state.MediaTypeUiState

interface HomeScreenInteractionListener {
    fun onMediaClick(id: Int, mediaTypeUiState: MediaTypeUiState)
    fun onTabClick(selectedTab: SelectedHomeTab)
    fun onRetryClick()
    fun onSnackBarDismiss()
    fun onTvShowsRateUpdated()
    fun onMoviesRateUpdated()
}