package com.sanaa.presentation.screen

import com.sanaa.presentation.state.CategoryUiState

data class CategoriesScreenUiState(
    val isLoading: Boolean = false,
    val isNoInternet: Boolean = false,
    val tvCategories: List<CategoryUiState> = emptyList(),
    val movieCategories: List<CategoryUiState> = emptyList(),
    val selectedTabIndex: Int = MOVIE_TAB_INDEX
) {
    companion object {
        const val MOVIE_TAB_INDEX = 0
        const val TV_TAB_INDEX = 1
    }
}

