package com.sanaa.tvapp.presentation.screens.home

import androidx.paging.PagingData
import com.sanaa.tvapp.state.MediaItemUiState
import com.sanaa.tvapp.state.SnackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class HomeScreenUiState(
    val popularMedia: List<MediaItemUiState> = emptyList(),
    val topRatingMovies: List<MediaItemUiState> = emptyList(),
    val topRatingTvShows: List<MediaItemUiState> = emptyList(),
    val continueWatchingMovies: List<MediaItemUiState> = emptyList(),
    val continueWatchingTvShows: List<MediaItemUiState> = emptyList(),
    val upcomingMovies: Flow<PagingData<MediaItemUiState>> = flowOf(PagingData.empty()),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isNoInternet: Boolean = false,
    val userIsLoggedIn: Boolean = false,
    val snackBarData: SnackData? = null,
    val ratedMovies: List<MediaItemUiState> = emptyList(),
    val ratedTvShows: List<MediaItemUiState> = emptyList(),
    val selectedTab: SelectedHomeTab = SelectedHomeTab.MOVIES,
)

enum class SelectedHomeTab {
    MOVIES, TV_SHOWS
}