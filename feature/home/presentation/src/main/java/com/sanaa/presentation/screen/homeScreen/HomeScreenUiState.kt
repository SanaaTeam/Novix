package com.sanaa.presentation.screen.homeScreen

import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItem


data class HomeScreenUiState(
    val popularMedia: List<MediaItem> = emptyList(),
    val topRatingMedia: List<MediaItem> = emptyList(),
    val continueWatchingMedia: List<MediaItem> = emptyList(),
    val upcomingMovies: List<MediaItem> = emptyList(),
    val movieGenres: List<GenreUiState> = emptyList(),
    val movieSelectedGenreId: Int? = null,
    val isLoading : Boolean = false,
    val errorMessage: String? = null,
    val showBottomSheet: Boolean = false,
    val isLoadingUpcoming : Boolean = false,
)
