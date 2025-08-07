package com.sanaa.presentation.profileMapper

import com.sanaa.presentation.screen.watchingHistory.GenreUiState
import entity.Genre


fun Genre.toGenreUiState(): GenreUiState {
    return GenreUiState(
        id = id,
        name = name
    )
}