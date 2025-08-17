package com.sanaa.presentation.screen.trendingMoviesScreen

import androidx.paging.PagingData
import com.sanaa.presentation.components.SnackData
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItemUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class TrendingMoviesScreenUiState (
    val mediaList: Flow<PagingData<MediaItemUiState>> = flowOf(PagingData.empty()),
    val genreList: List<GenreUiState> = emptyList(),
    val selectedGenreId: Int? = null,
    val isLoading: Boolean = false,
    val isNoInternetConnection: Boolean = false,
    val userIsLoggedIn: Boolean = false,
    val selectedMediaId: Int? = null,
    val showLoginBottomSheet: Boolean = false,
    val showSaveToListBottomSheet: Boolean = false,
    val showAddListBottomSheet: Boolean = false,
    val snackBarData: SnackData? = null
)