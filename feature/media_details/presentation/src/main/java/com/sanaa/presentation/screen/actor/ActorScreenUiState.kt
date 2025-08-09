package com.sanaa.presentation.screen.actor

import com.sanaa.presentation.model.ActorUiModel
import com.sanaa.presentation.model.MovieUiModel
import com.sanaa.presentation.model.SeriesUiModel

data class ActorScreenUiState(
    val actor: ActorUiModel = ActorUiModel(),
    val topMovies: List<MovieUiModel> = emptyList(),
    val topTvSeries: List<SeriesUiModel> = emptyList(),
    val profileImageUrls: List<String> = emptyList(),
    val galleryImageUrls: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showLoginBottomSheet: Boolean = false,
    val noInternetConnection: Boolean = false,
    val userIsLoggedIn: Boolean = false,
    val showSaveToListBottomSheet: Boolean = false,
    val showAddListBottomSheet: Boolean = false,
    val selectedMediaToSave: MovieUiModel? = null
)


