package com.sanaa.presentation.screen.movie_details

sealed class MovieDetailsUiEffect {
    data class ShowErrorMessage(val message: String) : MovieDetailsUiEffect()
    object NavigateBack : MovieDetailsUiEffect()
    object OpenTrailer : MovieDetailsUiEffect()
}