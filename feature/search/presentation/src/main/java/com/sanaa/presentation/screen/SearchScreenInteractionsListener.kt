package com.sanaa.presentation.screen

import usecase.search.MediaFilters

interface SearchScreenInteractionsListener {
    fun onClearRecentViewClicked()
    fun onClearRecentSearchClicked()
    fun onCancelRecentSearchItemClicked(searchText: String)
    fun onSaveIconClicked()
    fun onTabSelected(index: Int)
    fun onRecentSearchItemClicked(searchText: String)
    fun onSearchQueryChanged(title: String)
    fun onFilterApplied(filters: MediaFilters?)
}