package com.sanaa.presentation.screen.homeScreen

import androidx.paging.PagingData
import com.sanaa.presentation.components.SnackData
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItemUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class HomeScreenUiState(
    val popularMedia: List<MediaItemUiState> = emptyList(),
    val topRatingMedia: List<MediaItemUiState> = emptyList(),
    val continueWatchingMedia: List<MediaItemUiState> = emptyList(),
    val upcomingMovies: Flow<PagingData<MediaItemUiState>> = flowOf(PagingData.empty()),
    val movieGenres: List<GenreUiState> = emptyList(),
    val movieSelectedGenreId: Int? = null,
    val showLoginBottomSheet: Boolean = false,
    val isLoadingGenre: Boolean = false,
    val isLoadingPopular: Boolean = false,
    val isLoadingTopRated: Boolean = false,
    val isLoadingHistory: Boolean = false,
    val isLoadingUpcoming: Boolean = false,
    val isNoInternetConnection: Boolean = false,
    val userIsLoggedIn: Boolean = false,
    val showSaveToListBottomSheet: Boolean = false,
    val selectedMediaToSave: MediaItemUiState? = null,
    val showAddListBottomSheet: Boolean = false,
    val selectedMediaId: Long = 0L,
    val snackBarData: SnackData? = null
)