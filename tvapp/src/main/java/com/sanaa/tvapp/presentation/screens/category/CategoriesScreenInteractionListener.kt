package com.sanaa.tvapp.presentation.screens.category

import com.sanaa.tvapp.presentation.screens.category.state.CategoryUiState

interface CategoriesScreenInteractionListener {
    fun onGenreClicked(category: CategoryUiState)
    fun onTabChanged(tabIndex: Int)
    fun onRetryClick()
}