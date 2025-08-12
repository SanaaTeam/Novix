package com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper

import com.sanaa.tvapp.presentation.screens.mediaDetails.model.GenreUiModel
import entity.Genre

fun Genre.toUiModel(): GenreUiModel {
    return GenreUiModel(
        id = id,
        name = name
    )
}