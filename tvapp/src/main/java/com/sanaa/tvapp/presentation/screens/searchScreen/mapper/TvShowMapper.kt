package com.sanaa.tvapp.presentation.screens.searchScreen.mapper

import com.sanaa.tvapp.presentation.screens.searchScreen.TvShowUiModel
import entity.TvShow

fun TvShow.toUiState(): TvShowUiModel {
    return TvShowUiModel(
        id = this.id,
        title = this.title,
        imageUrl = this.posterImageUrl,
    )
}