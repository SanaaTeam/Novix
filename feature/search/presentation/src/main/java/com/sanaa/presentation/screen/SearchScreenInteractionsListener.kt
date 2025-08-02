package com.sanaa.presentation.screen

import com.sanaa.presentation.screen.state.RecentViewedUiModel
import usecase.search.search_param.MediaFilters

interface SearchScreenInteractionsListener {
    fun onClearRecentViewClicked()
    fun onClearRecentSearchClicked()
    fun onDeleteRecentSearchItem(id: Int)
    fun onFilterClicked()
    fun onBottomSheetDragged()
    fun onTabSelected(index: Int)
    fun onRecentSearchItemClicked(query: String)
    fun onRecentViewedMediaClicked(viewed: RecentViewedUiModel)
    fun onSearchQueryChanged(query: String)
    fun retrySearch()
    fun onFilterApplied(tabIndex: Int,filters: MediaFilters?)
    fun onActorClicked(id: Int)
    fun onSearchResultMediaClicked(viewed: RecentViewedUiModel)
    fun onLoginButtonClick()
    fun onSaveSeriesClicked()
    fun onSaveMoviesClicked()

}