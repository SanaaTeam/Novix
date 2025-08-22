package com.sanaa.presentation.screen.topShowsScreen

import com.sanaa.presentation.model.TvShowUiState
import com.sanaa.presentation.screen.movieDetails.SnackData

data class TopTvShowsScreenUiState(
    val topTvShows: List<TvShowUiState> = emptyList(),
    val isLoading: Boolean = false,
    val noInternetConnection: Boolean = false,
    val snackBarData: SnackData? = null
)