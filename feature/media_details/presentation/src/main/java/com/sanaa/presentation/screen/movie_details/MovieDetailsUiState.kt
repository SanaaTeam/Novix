package com.sanaa.presentation.screen.movie_details

import com.sanaa.presentation.model.MovieDetailsUiModel

data class MovieDetailsUiState(
    val isLoading: Boolean = true,
    val data: MovieDetailsUiModel? = null,
    val errorMessage: String? = null
)