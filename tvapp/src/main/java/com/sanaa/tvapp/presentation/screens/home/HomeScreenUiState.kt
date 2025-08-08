package com.sanaa.tvapp.presentation.screens.home

import androidx.paging.PagingData
import com.sanaa.tvapp.state.GenreUiState
import com.sanaa.tvapp.state.MediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class HomeScreenUiState(
    val featuredCarousel: FeaturedCarouselState = FeaturedCarouselState(),
    val popularMedia: List<MediaItem> = emptyList(),
    val topRatingMedia: List<MediaItem> = emptyList(),
    val continueWatchingMedia: List<MediaItem> = emptyList(),
    val upcomingMovies: Flow<PagingData<MediaItem>> = flowOf(PagingData.empty()),
    val movieGenres: List<GenreUiState> = emptyList(),
    val movieSelectedGenreId: Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showBottomSheet: Boolean = false,
    val isLoadingUpcoming: Boolean = false,
    val isNoInternet: Boolean = false,
    val userIsLoggedIn: Boolean = false
)

data class FeaturedCarouselState(
    val imageUrl: String = "",
    val movieTitle: String = "",
    val movieDescription: String = "",
    val movieInfo: String = "",
)
