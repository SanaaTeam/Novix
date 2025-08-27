package com.sanaa.tvapp.presentation.screens.mediaDetails.movieScreen

import androidx.paging.PagingData
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.ActorUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.MovieDetailsUiModel
import com.sanaa.tvapp.state.SnackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class
MovieDetailsScreenUiState(
    val isLoading: Boolean = true,
    val movieDetails: MovieDetailsUiModel = MovieDetailsUiModel(),
    val similarMovies: Flow<PagingData<MovieDetailsUiModel>> = flowOf(PagingData.empty()),
    val cast: List<ActorUiModel> = emptyList(),
    val imagesUrls: List<String> = emptyList(),
    val noInternetConnection: Boolean = false,
    val isUserLoggedIn: Boolean = false,
    val showRateDialog: Boolean = false,
    val showLoginDialog: Boolean = false,
    val snackBarData: SnackData? = null,
    val isRatingSubmitted: Boolean = false,
    val imdbRating: Int = 0,
    val filledStarsCount: Int = 0,
) {
    val hasUserSelectedRate: Boolean
        get() = filledStarsCount > 0 && (imdbRating == 0 || filledStarsCount != imdbRating)
}