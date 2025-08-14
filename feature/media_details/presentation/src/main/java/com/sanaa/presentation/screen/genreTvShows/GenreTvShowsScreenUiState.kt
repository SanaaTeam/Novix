package com.sanaa.presentation.screen.genreTvShows

import androidx.paging.PagingData
import com.sanaa.presentation.model.TvShowUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class GenreTvShowsScreenUiState(
    val title: String? = null,
    val tvShows: Flow<PagingData<TvShowUiState>> = flowOf(PagingData.empty()),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showBottomSheet: Boolean = false,
    val noInternetConnection: Boolean = false,
    val userIsLoggedIn: Boolean = false
)



