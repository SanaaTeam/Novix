package com.sanaa.presentation.screen.state.mapper

import com.sanaa.presentation.filter_bottomsheet.state.GenreUiState
import entity.Genre

fun GenreUiState.toDomain() = Genre(
    id = id,
    name = name
)

fun Genre.toState() = GenreUiState(
    id = id,
    name = name
)