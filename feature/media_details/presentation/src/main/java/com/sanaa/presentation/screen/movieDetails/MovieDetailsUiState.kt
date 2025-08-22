package com.sanaa.presentation.screen.movieDetails

import androidx.paging.PagingData
import com.sanaa.presentation.model.ActorUiModel
import com.sanaa.presentation.model.MovieUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class MovieDetailsUiState(
    val isLoading: Boolean = true,
    val movieDetails: MovieUiModel = MovieUiModel(),
    val isError: Boolean = false,
    val similarMovies: Flow<PagingData<MovieUiModel>> = flowOf(PagingData.empty()),
    val cast: List<ActorUiModel> = emptyList(),
    val imagesUrls: List<String> = emptyList(),
    val showLoginBottomSheet: Boolean = false,
    val showRateBottomSheet: Boolean = false,
    val noInternetConnection: Boolean = false,
    val isUserLoggedIn: Boolean = false,
    val loginPromptType: LoginPromptType? = null,
    val showSaveToListBottomSheet: Boolean = false,
    val showAddListBottomSheet: Boolean = false,
    val selectedMediaId: Int? = null,
    val snackBarData: SnackData? = null,
    val isRatingSubmitted: Boolean = false,
    val imdbRating: Int = 0,
    val filledStarsCount: Int = 0,
) {
    val hasUserSelectedRate: Boolean
        get() = filledStarsCount > 0 && (imdbRating == 0 || filledStarsCount != imdbRating)
}

data class SnackData(
    val message: String,
    val isError: Boolean
)

enum class LoginPromptType {
    RATE,
    BOOKMARK
}