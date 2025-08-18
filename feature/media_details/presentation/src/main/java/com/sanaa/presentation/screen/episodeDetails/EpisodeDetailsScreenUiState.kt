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
    val error: String? = null,
    val tvShowId: Int = 0,
    val trailerUrl: String? = null,
    val showLoginBottomSheet: Boolean = false,
    val showRateBottomSheet: Boolean = false,
    val noInternetConnection: Boolean = false,
    val imdbRating: Int = 0,
    val guestSessionId: String = "",
    val loginPromptType: LoginPromptType? = null,
    val isUserLoggedIn: Boolean = false,
    val snackBarData: SnackData? = null,
) {
    val hasUserSelectedRate: Boolean
        get() = imdbRating > 0
}
