package com.sanaa.tvapp.presentation.screens.mediaDetails.movieScreen

import androidx.paging.PagingData
import com.sanaa.presentation.model.ActorUiModel
import com.sanaa.presentation.model.MovieUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class MovieDetailsScreenUiState(
    val isLoading: Boolean = true,
    val movieDetails: MovieUiModel = MovieUiModel(),
    val errorMessage: String? = null,
    val similarMovies: Flow<PagingData<MovieUiModel>> = flowOf(PagingData.empty()),
    val cast: List<ActorUiModel> = emptyList(),
    val imagesUrls: List<String> = emptyList(),
    val noInternetConnection: Boolean = false,
    val imdbRating: Int = 0,
    val isUserLoggedIn: Boolean = false,
    val isRatingSubmitted: Boolean = false,
)
