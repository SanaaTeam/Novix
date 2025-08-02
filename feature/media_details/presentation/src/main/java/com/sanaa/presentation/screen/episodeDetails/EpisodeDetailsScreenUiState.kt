package com.sanaa.presentation.screen.episodeDetails

import com.sanaa.presentation.model.ActorUiModel
import com.sanaa.presentation.model.EpisodeUiModel
import com.sanaa.presentation.screen.movieDetails.LoginPromptType


data class EpisodeDetailsScreenUiState(
    val isLoading: Boolean = false,
    val episode: EpisodeUiModel = EpisodeUiModel(),
    val guestOfHonor: List<ActorUiModel> = emptyList(),
    val imagesUrl: List<String> = emptyList(),
    val error: String? = null,
    val seriesId: Int = 0,
    val trailerUrl: String? = null,
    val showLoginBottomSheet: Boolean = false,
    val showRateBottomSheet: Boolean = false,
    val noInternetConnection: Boolean = false,
    val imdbRating: Int = 0,
    val guestSessionId: String = "",
    val loginPromptType: LoginPromptType? = null,
    val isUserLoggedIn: Boolean = false
) {
    val hasUserSelectedRate: Boolean
        get() = imdbRating > 0
}
