package com.sanaa.presentation.state.mapper

import com.sanaa.presentation.state.CategoryUiState
import com.sanaa.presentation.util.getGenreImage
import entity.Genre


fun Genre.toUiState(): CategoryUiState {
    return CategoryUiState(
        id = id,
        name = name,
        imageResourceId = getGenreImage(id)
    )
}

