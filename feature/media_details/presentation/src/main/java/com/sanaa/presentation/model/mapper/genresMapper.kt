package com.sanaa.presentation.model.mapper

import com.sanaa.presentation.model.GenreUiModel
import entity.Genre


fun Genre.toState(): GenreUiModel {
    return GenreUiModel(
        id = id,
        name = name
    )
}