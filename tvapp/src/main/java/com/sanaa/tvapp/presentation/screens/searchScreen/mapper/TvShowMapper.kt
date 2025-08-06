package com.sanaa.presentation.screen.state.mapper

import com.sanaa.tvapp.presentation.screens.searchScreen.TvShowUiModel
import entity.TvSeries

fun TvSeries.toUiState(): TvShowUiModel {
    return TvShowUiModel(
        id = this.id,
        title = this.title,
        imageUrl = this.posterImageUrl.orEmpty(),
    )
}