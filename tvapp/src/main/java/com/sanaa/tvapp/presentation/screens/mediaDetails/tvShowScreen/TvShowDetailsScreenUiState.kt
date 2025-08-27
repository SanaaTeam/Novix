package com.sanaa.tvapp.presentation.screens.mediaDetails.tvShowScreen

import com.sanaa.tvapp.presentation.screens.mediaDetails.model.ActorUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.SeasonUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.TvShowDetailsUiModel
import com.sanaa.tvapp.state.SnackData


data class TvShowDetailsScreenUiState(
    val isLoading: Boolean = false,
    val tvShows: TvShowDetailsUiModel = TvShowDetailsUiModel(),
    val season: SeasonUiModel = SeasonUiModel(),
    val cast: List<ActorUiModel> = emptyList(),
    val backgroundImageUrl: String = "",
    val isLoadingEpisodes: Boolean = false,
    val error: String? = null,
    val selectedSeason: Int = 1,
    val noInternetConnection: Boolean = false,
    val isUserLoggedIn: Boolean = false,
    val showLoginDialog: Boolean = false,
    val showRateDialog: Boolean = false,
    val snackBarData: SnackData? = null,
    val isRatingSubmitted: Boolean = false,
    val imdbRating: Int = 0,
    val filledStarsCount: Int = 0,
) {
    val hasUserSelectedRate: Boolean
        get() = filledStarsCount > 0 && (imdbRating == 0 || filledStarsCount != imdbRating)
}
