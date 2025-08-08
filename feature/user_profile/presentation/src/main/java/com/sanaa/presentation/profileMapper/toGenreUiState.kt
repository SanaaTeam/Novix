package com.sanaa.presentation.profileMapper

import com.sanaa.presentation.screen.watchingHistory.GenreState
import entity.Genre


fun Genre.toGenreUiState(): GenreState {
    return GenreState(
        id = id,
        name = name
    )
}