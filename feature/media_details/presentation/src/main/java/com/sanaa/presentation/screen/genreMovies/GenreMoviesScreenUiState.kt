package com.sanaa.presentation.screen.genreMovies

import androidx.paging.PagingData
import com.sanaa.presentation.model.MovieUiModel
import com.sanaa.presentation.screen.movieDetails.SnackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class GenreMoviesScreenUiState(
    val title: String? = null,
    val movies: Flow<PagingData<MovieUiModel>> = flowOf(PagingData.empty()),
    val isLoading: Boolean = false,
    val showLoginBottomSheet: Boolean = false,
    val noInternetConnection: Boolean = false,
    val userIsLoggedIn: Boolean = false,
    val showSaveToListBottomSheet: Boolean = false,
    val selectedMovieToSave: MovieUiModel? = null,
    val showAddListBottomSheet: Boolean = false,
    val snackBarData: SnackData? = null
)
enum class ScreenState {
    LOADING,
    NO_INTERNET,
    CONTENT
}