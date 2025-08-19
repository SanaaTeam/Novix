package com.sanaa.tvapp.state.mapper

import com.sanaa.tvapp.state.GenreUiState
import entity.Genre

fun Genre.toState(): GenreUiState {
    return GenreUiState(
        id = id,
        name = name
    )
}