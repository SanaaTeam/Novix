package com.sanaa.presentation.model

import entity.Genre

data class GenreUiModel(
    val id: Int = 0,
    val name: String
)

fun Genre.toUiModel(): GenreUiModel {
    return GenreUiModel(
        id = id,
        name = name.orEmpty()
    )
}