package com.sanaa.tvapp.presentation.screens.searchScreen.mapper

import com.sanaa.tvapp.presentation.screens.searchScreen.MovieUiModel
import entity.Movie

fun Movie.toUiState(): MovieUiModel {
    return MovieUiModel(
        id = this.id,
        title = this.title,
        imageUrl = this.posterImageUrl,
    )
}