package com.sanaa.presentation.screen.mediaTabScreen.topRatingScreen

import androidx.paging.PagingData
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaTypeUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class TopRatedMediaScreenUiState(
    val selectedMediaTypeUi: MediaTypeUi = MediaTypeUi.MOVIE,
    val movieList: Flow<PagingData<MediaItem>> = flowOf(PagingData.empty()),
    val tvShowList: Flow<PagingData<MediaItem>> = flowOf(PagingData.empty()),
    val movieGenres: List<GenreUiState> = emptyList(),
    val tvShowGenres: List<GenreUiState> = emptyList(),
    val movieSelectedGenreId: Int? = null,
    val tvShowSelectedGenreId: Int? = null,
    val isLoading: Boolean = false,
    val isNoInternetConnection: Boolean = false,
    val showLoginBottomSheet: Boolean = false,
    val userIsLoggedIn: Boolean = false
)