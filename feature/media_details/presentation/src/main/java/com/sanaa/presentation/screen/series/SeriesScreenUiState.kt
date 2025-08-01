package com.sanaa.presentation.screen.series

import com.sanaa.presentation.model.ActorUiModel
import com.sanaa.presentation.model.SeasonUiModel
import com.sanaa.presentation.model.SeriesUiModel
import com.sanaa.presentation.screen.movieDetails.LoginPromptType

data class SeriesScreenUiState(
    val isLoading: Boolean = false,
    val series: SeriesUiModel = SeriesUiModel(),
    val season: SeasonUiModel = SeasonUiModel(),
    val cast: List<ActorUiModel> = emptyList(),
    val images: List<String> = emptyList(),
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

