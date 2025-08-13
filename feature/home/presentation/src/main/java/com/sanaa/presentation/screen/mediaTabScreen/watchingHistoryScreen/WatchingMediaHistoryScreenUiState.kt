package com.sanaa.presentation.screen.mediaTabScreen.watchingHistoryScreen

import com.sanaa.presentation.components.SnackData
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItemUiState
import com.sanaa.presentation.state.MediaTypeUi

data class WatchingMediaHistoryScreenUiState(
    val selectedMediaTypeUi: MediaTypeUi = MediaTypeUi.MOVIE,
    val movieList: List<MediaItemUiState> = emptyList(),
    val tvShowList: List<MediaItemUiState> = emptyList(),
    val movieGenres: List<GenreUiState> = emptyList(),
    val tvShowGenres: List<GenreUiState> = emptyList(),
    val movieSelectedGenreId: Int? = null,
    val tvShowSelectedGenreId: Int? = null,
    val isLoading: Boolean = false,
    val showRefreshButton: Boolean = false,
    val isNoInternetConnection: Boolean = false,
    val showBottomSheet: Boolean = false,
    val snackBarData: SnackData? = null
)