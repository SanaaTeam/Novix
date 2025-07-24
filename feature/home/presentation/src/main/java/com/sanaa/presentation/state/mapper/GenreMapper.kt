package com.sanaa.presentation.state.mapper

import com.sanaa.presentation.state.GenreUiState
import entity.Genre

fun Genre.toState(): GenreUiState {
    return GenreUiState(
        id = id,
        name = name
    )
}