package com.sanaa.tvapp.presentation.screens.searchScreen.mapper

import com.sanaa.tvapp.presentation.screens.searchScreen.MovieUiModel
import entity.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Movie.toUiState(): MovieUiModel {
    return MovieUiModel(
        id = this.id,
        title = this.title,
        imageUrl = this.posterImageUrl,
    )
}

fun List<Movie>.toUiState() = this.map { it.toUiState() }
fun Flow<List<Movie>>.toUiState() = this.map { it.toUiState() }