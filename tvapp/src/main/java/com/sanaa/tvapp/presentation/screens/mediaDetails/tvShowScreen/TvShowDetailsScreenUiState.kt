package com.sanaa.tvapp.presentation.screens.mediaDetails.tvShowScreen

import com.sanaa.tvapp.presentation.screens.mediaDetails.model.ActorUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.SeasonUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.TvShowDetailsUiModel

data class TvShowDetailsScreenUiState(
    val isLoading: Boolean = false,
    val tvShows: TvShowDetailsUiModel = TvShowDetailsUiModel(),
    val season: SeasonUiModel = SeasonUiModel(),
    val cast: List<ActorUiModel> = emptyList(),
    val backgroundImageUrl: String = "",
    val showLoginBottomSheet: Boolean = false,
    val showRateBottomSheet: Boolean = false,
    val isLoadingEpisodes: Boolean = false,
    val error: String? = null,
    val selectedSeason: Int = 1,
    val noInternetConnection: Boolean = false,
    val imdbRating: Int = 0,
    val guestSessionId: String = "",
    val isUserLoggedIn: Boolean = false,
    val loginPromptType: LoginPromptType? = null
) {
    val hasUserSelectedRate: Boolean
        get() = imdbRating > 0
}

data class SnackData(
    val message: String,
    val isError: Boolean
)

enum class LoginPromptType {
    RATE,
    BOOKMARK
}