package com.sanaa.presentation.model

import entity.Genre

data class GenreUiState(
    val id: Int,
    val name: String,
)

fun Genre.toState(): GenreUiState {
    return GenreUiState(
        id = id,
        name = name,
    )
}