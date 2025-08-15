package com.sanaa.presentation.screen.watchingHistoryScreen

import com.sanaa.presentation.components.SnackData
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItemUiState
import com.sanaa.presentation.state.MediaTypeUiState

data class WatchingMediaHistoryScreenUiState(
    val selectedMediaTypeUiState: MediaTypeUiState = MediaTypeUiState.MOVIE,
    val movieList: List<MediaItemUiState> = emptyList(),
    val tvShowList: List<MediaItemUiState> = emptyList(),
    val movieGenres: List<GenreUiState> = emptyList(),
    val tvShowGenres: List<GenreUiState> = emptyList(),
    val movieSelectedGenreId: Int? = null,
    val tvShowSelectedGenreId: Int? = null,
    val isLoading: Boolean = false,
    val showRefreshButton: Boolean = false,
    val isNoInternetConnection: Boolean = false,
    val showLoginBottomSheet: Boolean = false,
    val showSaveToListBottomSheet: Boolean = false,
    val showAddListBottomSheet: Boolean = false,
    val selectedMediaToSave: MediaItemUiState? = null,
    val userIsLoggedIn: Boolean = false,
    val showBottomSheet: Boolean = false,
    val snackBarData: SnackData? = null
)