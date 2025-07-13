package com.sanaa.presentation.screen

import usecase.search.MediaFilters

interface SearchScreenInteractionsListener {
    fun onClearRecentViewClicked()
    fun onClearRecentSearchClicked()
    fun onTabSelected(index: Int)
    fun onRecentSearchItemClicked()
    fun onSearchQueryChanged(title: String)
    fun onFilterApplied(filters: MediaFilters?)
}