package com.sanaa.presentation.screen.watchingHistoryScreen

import com.sanaa.presentation.components.SnackData
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaTypeUi

data class WatchingMediaHistoryScreenUiState(
    val selectedMediaTypeUiState: MediaTypeUi = MediaTypeUi.MOVIE,
    val movieList: List<MediaItem> = emptyList(),
    val tvShowList: List<MediaItem> = emptyList(),
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
    val selectedMediaToSave: MediaItem? = null,
    val userIsLoggedIn: Boolean = false,
    val showBottomSheet: Boolean = false,
    val snackBarData: SnackData? = null
)