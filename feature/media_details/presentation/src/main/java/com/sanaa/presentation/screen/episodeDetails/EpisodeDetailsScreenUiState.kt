package com.sanaa.presentation.screen.episodeDetails

import com.sanaa.presentation.model.ActorUiModel
import com.sanaa.presentation.model.EpisodeUiState
import com.sanaa.presentation.screen.movieDetails.LoginPromptType
import com.sanaa.presentation.screen.movieDetails.SnackData

data class EpisodeDetailsScreenUiState(
    val isLoading: Boolean = false,
    val episode: EpisodeUiState = EpisodeUiState(),
    val guestOfHonor: List<ActorUiModel> = emptyList(),
    val imagesUrl: List<String> = emptyList(),
    val tvShowId: Int = 0,
    val trailerUrl: String? = null,
    val showLoginBottomSheet: Boolean = false,
    val noInternetConnection: Boolean = false,
    val guestSessionId: String = "",
    val loginPromptType: LoginPromptType? = null,
    val snackBarData: SnackData? = null,
)