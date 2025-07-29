package com.sanaa.presentation.screen.movieDetails

import com.sanaa.presentation.model.ActorUiModel
import com.sanaa.presentation.model.MovieUiModel

data class MovieDetailsUiState(
    val isLoading: Boolean = true,
    val movieDetails: MovieUiModel = MovieUiModel(),
    val errorMessage: String? = null,
    val similarMovies: List<MovieUiModel> = emptyList(),
    val cast: List<ActorUiModel> = emptyList(),
    val imagesUrls: List<String> = emptyList(),
    val showLoginBottomSheet: Boolean = false,
    val noInternetConnection: Boolean = false,
)