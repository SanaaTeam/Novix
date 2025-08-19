package com.sanaa.presentation.screen.tvShow

import com.sanaa.presentation.model.ActorUiModel
import com.sanaa.presentation.model.SeasonUiState
import com.sanaa.presentation.model.TvShowUiState
import com.sanaa.presentation.screen.movieDetails.LoginPromptType
import com.sanaa.presentation.screen.movieDetails.SnackData

data class TvShowScreenUiState(
    val isLoading: Boolean = false,
    val tvShow: TvShowUiState = TvShowUiState(),
    val season: SeasonUiState = SeasonUiState(),
    val cast: List<ActorUiModel> = emptyList(),
    val images: List<String> = emptyList(),
    val showLoginBottomSheet: Boolean = false,
    val showRateBottomSheet: Boolean = false,
    val isLoadingEpisodes: Boolean = false,
    val isError: Boolean = false,
    val selectedSeason: Int = 1,
    val noInternetConnection: Boolean = false,
    val imdbRating: Int = 0,
    val guestSessionId: String = "",
    val isUserLoggedIn: Boolean = false,
    val loginPromptType: LoginPromptType? = null,
    val snackBarData: SnackData? = null,
    val showRateButton : Boolean = true,

    ) {
    val hasUserSelectedRate: Boolean
        get() = imdbRating > 0
}

