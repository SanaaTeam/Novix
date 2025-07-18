package com.sanaa.presentation.screen.state.mapper

import com.sanaa.presentation.screen.state.TvShowUiModel
import search.usecase.search_param.SearchTvSeriesOutput

fun SearchTvSeriesOutput.toUiState(): TvShowUiModel {
    return TvShowUiModel(
        id = this.id,
        title = this.title,
        imageUrl = this.posterImageUrl,
        rating = ""
    )
}