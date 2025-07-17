package com.sanaa.presentation.screen.movie_details

import com.sanaa.presentation.module.ActorUiModel
import com.sanaa.presentation.module.MovieUiModel

data class MovieDetailsUiState(
    val isLoading: Boolean = true,
    val movieDetails: MovieUiModel = MovieUiModel(),
    val errorMessage: String? = null,
    val similarMovies: List<MovieUiModel> = emptyList(),
    val cast: List<ActorUiModel> = emptyList(),
    val imagesUrls: List<String> = emptyList()
)