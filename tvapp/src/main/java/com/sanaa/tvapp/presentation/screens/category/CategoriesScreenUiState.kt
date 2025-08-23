package com.sanaa.tvapp.presentation.screens.category

import com.sanaa.tvapp.presentation.screens.category.state.CategoryUiState
import com.sanaa.tvapp.state.SnackData

data class CategoriesScreenUiState(
    val isLoading: Boolean = false,
    val isNoInternet: Boolean = false,
    val tvCategories: List<CategoryUiState> = emptyList(),
    val movieCategories: List<CategoryUiState> = emptyList(),
    val selectedTabIndex: Int = MOVIE_TAB_INDEX,
    val snackBarData: SnackData? = null
) {
    companion object {
        const val MOVIE_TAB_INDEX = 0
    }
}

