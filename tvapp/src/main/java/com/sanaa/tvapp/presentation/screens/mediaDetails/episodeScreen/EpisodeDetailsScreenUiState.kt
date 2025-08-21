package com.sanaa.tvapp.presentation.screens.mediaDetails.episodeScreen

import com.sanaa.tvapp.presentation.screens.mediaDetails.model.ActorUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.EpisodeUiModel


data class EpisodeDetailsScreenUiState(
    val isLoading: Boolean = false,
    val episode: EpisodeUiModel = EpisodeUiModel(),
    val guestOfHonor: List<ActorUiModel> = emptyList(),
    val backgroundImageUrl: String = "",
    val error: String? = null,
    val seriesId: Int = 0,
    val trailerUrl: String? = null,
    val showLoginBottomSheet: Boolean = false,
    val showRateBottomSheet: Boolean = false,
    val noInternetConnection: Boolean = false,
    val imdbRating: Int = 0,
    val guestSessionId: String = "",
    val isUserLoggedIn: Boolean = false
)