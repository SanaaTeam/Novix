package com.sanaa.presentation.screen.trendingTvShowScreen

import androidx.paging.PagingData
import com.sanaa.presentation.components.SnackData
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class TrendingTvShowsScreenUiState(
    val mediaList: Flow<PagingData<MediaItem>> = flowOf(PagingData.empty()),
    val genreList: List<GenreUiState> = emptyList(),
    val selectedGenreId: Int? = null,
    val isLoading: Boolean = false,
    val isNoInternetConnection: Boolean = false,
    val snackBarData: SnackData? = null
)