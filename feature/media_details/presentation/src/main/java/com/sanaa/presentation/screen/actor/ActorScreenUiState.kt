package com.sanaa.presentation.screen.actor

import com.sanaa.presentation.model.ActorUiModel
import com.sanaa.presentation.model.MovieUiModel
import com.sanaa.presentation.model.TvShowUiState
import com.sanaa.presentation.screen.movieDetails.SnackData

data class ActorScreenUiState(
    val actor: ActorUiModel = ActorUiModel(),
    val topMovies: List<MovieUiModel> = emptyList(),
    val topTvShows: List<TvShowUiState> = emptyList(),
    val profileImageUrls: List<String> = emptyList(),
    val galleryImageUrls: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val showLoginBottomSheet: Boolean = false,
    val noInternetConnection: Boolean = false,
    val userIsLoggedIn: Boolean = false,
    val showSaveToListBottomSheet: Boolean = false,
    val showAddListBottomSheet: Boolean = false,
    val selectedMediaToSave: MovieUiModel? = null,
    val snackBarData: SnackData? = null
)