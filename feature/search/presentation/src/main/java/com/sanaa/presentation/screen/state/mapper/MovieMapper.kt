package com.sanaa.presentation.screen.state.mapper

import com.sanaa.presentation.screen.state.MovieUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import search.usecase.search_param.SearchMovieOutput

fun SearchMovieOutput.toUiState(): MovieUiModel {
    return MovieUiModel(
        id = this.id,
        title = this.title,
        imageUrl = this.posterImageUrl,
        rating = ""
    )
}

fun List<SearchMovieOutput>.toUiState() = this.map { it.toUiState() }
fun Flow<List<SearchMovieOutput>>.toUiState() = this.map { it.toUiState() }