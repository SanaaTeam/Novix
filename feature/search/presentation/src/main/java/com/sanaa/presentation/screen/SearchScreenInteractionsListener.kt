package com.sanaa.presentation.screen

import com.sanaa.presentation.screen.state.RecentViewedUiModel
import search.usecase.search_param.MediaFilters

interface SearchScreenInteractionsListener {
    fun onClearRecentViewClicked()
    fun onClearRecentSearchClicked()
    fun onDeleteRecentSearchItem(id: Int)
    fun onSaveIconClicked()
    fun onTabSelected(index: Int)
    fun onRecentSearchItemClicked(query: String)
    fun onSearchQueryChanged(title: String)
    fun onFilterApplied(filters: MediaFilters?)
    fun onSearchResultMediaClicked(viewed: RecentViewedUiModel)
    fun onRetryClicked()
}