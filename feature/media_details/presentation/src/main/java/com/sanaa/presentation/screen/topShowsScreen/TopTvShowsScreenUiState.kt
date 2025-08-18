package com.sanaa.presentation.screen.topShowsScreen

import com.sanaa.presentation.model.TvShowUiState

data class TopTvShowsScreenUiState(
    val topTvShows: List<TvShowUiState> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showLoginBottomSheet: Boolean = false,
    val noInternetConnection: Boolean = false,
)