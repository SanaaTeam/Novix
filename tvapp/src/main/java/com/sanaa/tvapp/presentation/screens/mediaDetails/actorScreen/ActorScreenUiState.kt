package com.sanaa.tvapp.presentation.screens.mediaDetails.actorScreen

import com.sanaa.tvapp.presentation.screens.mediaDetails.model.ActorUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.MovieDetailsUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.TvShowDetailsUiModel
import com.sanaa.tvapp.state.SnackData

data class ActorScreenUiState(
    val actor: ActorUiModel = ActorUiModel(),
    val topMovies: List<MovieDetailsUiModel> = emptyList(),
    val topTvShows: List<TvShowDetailsUiModel> = emptyList(),
    val profileImageUrls: List<String> = emptyList(),
    val galleryImageUrls: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showLoginBottomSheet: Boolean = false,
    val noInternetConnection: Boolean = false,
    val userIsLoggedIn: Boolean = false,
    val snackBarData: SnackData? = null,
)


