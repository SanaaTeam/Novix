package com.sanaa.tvapp.presentation.screens.category

import com.sanaa.tvapp.presentation.screens.category.state.CategoryUiState

data class CategoriesScreenUiState(
    val isLoading: Boolean = false,
    val isNoInternet: Boolean = false,
    val tvCategories: List<CategoryUiState> = emptyList(),
    val movieCategories: List<CategoryUiState> = emptyList(),
    val selectedTabIndex: Int = MOVIE_TAB_INDEX,
) {
    companion object {
        const val MOVIE_TAB_INDEX = 0
    }
}

