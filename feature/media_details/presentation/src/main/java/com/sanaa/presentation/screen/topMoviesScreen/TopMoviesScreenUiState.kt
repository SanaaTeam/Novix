package com.sanaa.presentation.screen.topMoviesScreen

import com.sanaa.presentation.model.MovieUiModel
import com.sanaa.presentation.screen.movieDetails.SnackData

data class TopMoviesScreenUiState(
    val topMovies: List<MovieUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showLoginBottomSheet: Boolean = false,
    val noInternetConnection: Boolean = false,
    val userIsLoggedIn: Boolean = false,
    val showSaveToListBottomSheet: Boolean = false,
    val showAddListBottomSheet: Boolean = false,
    val selectedMediaToSave: MovieUiModel? = null,
    val snackBarData: SnackData? = null
)