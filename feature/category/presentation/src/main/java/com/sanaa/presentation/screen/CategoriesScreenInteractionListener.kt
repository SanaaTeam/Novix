package com.sanaa.presentation.screen

import com.sanaa.presentation.state.CategoryUiState

interface CategoriesScreenInteractionListener {
    fun onGenreClicked(category: CategoryUiState)
    fun onTabChanged(tabIndex: Int)
}