package com.sanaa.presentation.screen.state.mapper

import com.sanaa.presentation.screen.state.TvShowUiModel
import entity.TvSeries

fun TvSeries.toUiState(): TvShowUiModel {
    return TvShowUiModel(
        id = this.id,
        title = this.title,
        imageUrl = this.posterImageUrl.orEmpty(),
        rating = ""
    )
}