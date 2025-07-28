package com.sanaa.presentation.screen.state.mapper

import com.sanaa.presentation.screen.state.MovieUiModel
import entity.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Movie.toUiState(): MovieUiModel {
    return MovieUiModel(
        id = this.id,
        title = this.title,
        imageUrl = this.posterImageUrl,
        rating = ""
    )
}

fun List<Movie>.toUiState() = this.map { it.toUiState() }
fun Flow<List<Movie>>.toUiState() = this.map { it.toUiState() }