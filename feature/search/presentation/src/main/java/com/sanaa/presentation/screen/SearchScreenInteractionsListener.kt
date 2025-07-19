package com.sanaa.presentation.screen

import com.sanaa.presentation.screen.state.RecentViewedUiModel
import search.usecase.search_param.MediaFilters

interface SearchScreenInteractionsListener {
    fun onClearRecentViewClicked()
    fun onClearRecentSearchClicked()
    fun onDeleteRecentSearchItem(id: Int)
    fun onFilterClicked()
    fun onBottomSheetDragged()
    fun onTabSelected(index: Int)
    fun onRecentSearchItemClicked(query: String)
    fun onSearchQueryChanged(query: String)
    fun retrySearch()
    fun onFilterApplied(filters: MediaFilters?)
    fun onSearchResultMediaClicked(viewed: RecentViewedUiModel)
}