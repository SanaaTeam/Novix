package com.sanaa.tvapp.presentation.screens.home

import androidx.paging.PagingData
import com.sanaa.tvapp.state.GenreUiState
import com.sanaa.tvapp.state.MediaItemUiState
import com.sanaa.tvapp.state.SnackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class HomeScreenUiState(
    val featuredCarousel: FeaturedCarouselState = FeaturedCarouselState(),
    val popularMedia: List<MediaItemUiState> = emptyList(),
    val topRatingMovies: List<MediaItemUiState> = emptyList(),
    val topRatingTvShows: List<MediaItemUiState> = emptyList(),
    val continueWatchingMovies: List<MediaItemUiState> = emptyList(),
    val continueWatchingTvShows: List<MediaItemUiState> = emptyList(),
    val upcomingMovies: Flow<PagingData<MediaItemUiState>> = flowOf(PagingData.empty()),
    val movieGenres: List<GenreUiState> = emptyList(),
    val movieSelectedGenreId: Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showBottomSheet: Boolean = false,
    val isLoadingUpcoming: Boolean = false,
    val isNoInternet: Boolean = false,
    val userIsLoggedIn: Boolean = false,
    val showSaveToListBottomSheet: Boolean = false,
    val selectedMediaId: Long = 0L,
    val snackBarData: SnackData? = null,
    val ratedMovies: List<MediaItemUiState> = emptyList(),
    val ratedTvShows: List<MediaItemUiState> = emptyList(),
)

data class FeaturedCarouselState(
    val imageUrl: String = "",
    val movieTitle: String = "",
    val movieDescription: String = "",
    val movieInfo: String = "",
)