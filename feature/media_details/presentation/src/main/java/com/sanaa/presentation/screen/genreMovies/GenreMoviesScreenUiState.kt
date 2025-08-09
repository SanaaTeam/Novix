package com.sanaa.presentation.screen.genreMovies

import androidx.paging.PagingData
import com.sanaa.presentation.model.MovieUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class GenreMoviesScreenUiState(
    val title: String? = null,
    val movies: Flow<PagingData<MovieUiModel>> = flowOf(PagingData.empty()),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showBottomSheet: Boolean = false,
    val noInternetConnection: Boolean = false,
    val userIsLoggedIn: Boolean = false,
    val showSaveToListBottomSheet: Boolean = false,
    val selectedMovieToSave: MovieUiModel? = null,
    val showAddListBottomSheet: Boolean = false,
)


