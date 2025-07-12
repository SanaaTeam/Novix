package com.sanaa.presentation.screen

import com.sanaa.presentation.state.RecentViewedUiModel
import usecase.search.MediaFilters

interface SearchScreenInteractionsListener {
    fun onClearRecentViewClicked()
    fun onClearRecentSearchClicked()
    fun onCancelRecentSearchItemClicked(id: Int)
    fun onSaveIconClicked()
    fun onTabSelected(index: Int)
    fun onRecentSearchItemClicked(query: String)
    fun onSearchQueryChanged(title: String)
    fun onFilterApplied(filters: MediaFilters?)
    fun onSearchResultMediaClicked(viewed: RecentViewedUiModel)

}