package com.sanaa.tvapp.presentation.screens.category.state.mapper

import com.sanaa.tvapp.presentation.screens.category.state.CategoryUiState
import com.sanaa.tvapp.presentation.screens.category.util.getGenreImage
import entity.Genre


fun Genre.toUiState(): CategoryUiState {
    return CategoryUiState(
        id = id,
        name = name,
        imageResourceId = getGenreImage(id)
    )
}

