package com.sanaa.presentation.screen.homeScreen

import androidx.paging.PagingData
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


data class HomeScreenUiState(
    val popularMedia: List<MediaItem> = emptyList(),
    val topRatingMedia: List<MediaItem> = emptyList(),
    val continueWatchingMedia: List<MediaItem> = emptyList(),
    val upcomingMovies: Flow<PagingData<MediaItem>> = flowOf(PagingData.empty()),
    val movieGenres: List<GenreUiState> = emptyList(),
    val movieSelectedGenreId: Int? = null,
    val showBottomSheet: Boolean = false,
    val isLoadingGenre: Boolean = false,
    val isLoadingPopular: Boolean = false,
    val isLoadingTopRated: Boolean = false,
    val isLoadingHistory: Boolean = false,
    val isLoadingUpcoming: Boolean = false,
    val isNoInternetConnection: Boolean = false,
    val userIsLoggedIn: Boolean = false
)
