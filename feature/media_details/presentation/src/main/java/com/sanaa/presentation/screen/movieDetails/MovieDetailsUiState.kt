package com.sanaa.presentation.screen.movieDetails

import androidx.paging.PagingData
import com.sanaa.presentation.model.ActorUiModel
import com.sanaa.presentation.model.MovieUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class MovieDetailsUiState(
    val isLoading: Boolean = true,
    val movieDetails: MovieUiModel = MovieUiModel(),
    val errorMessage: String? = null,
    val similarMovies: Flow<PagingData<MovieUiModel>> = flowOf(PagingData.empty()),
    val cast: List<ActorUiModel> = emptyList(),
    val imagesUrls: List<String> = emptyList(),
    val showLoginBottomSheet: Boolean = false,
    val showRateBottomSheet: Boolean = false,
    val noInternetConnection: Boolean = false,
    val imdbRating: Int = 0,
    val guestSessionId: String = "",
    val isUserLoggedIn: Boolean = false,
    val isRatingSubmitted: Boolean = false,
    val loginPromptType: LoginPromptType? = null,
    val showSaveToListBottomSheet: Boolean = false,
    val showAddListBottomSheet: Boolean = false,
    val selectedMediaId: Int? = null,
    val snackBarData: SnackData? = null,
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
