package com.sanaa.presentation.screen

import com.sanaa.presentation.screen.state.RecentViewedUiModel

interface SearchScreenInteractionsListener:
    LoginBottomSheetListener,
    SaveListListener,
    RecentViewListener
{
    fun onTabSelected(index: Int)
    fun onSearchQueryChanged(query: String)
    fun retrySearch()
    fun onActorClicked(id: Int)
    fun onSearchResultMediaClicked(viewed: RecentViewedUiModel)
    fun onSnackBarDismiss()
}

interface LoginBottomSheetListener{
    fun onLoginButtonClick()
    fun onLoginBottomSheetDismiss()
}

interface SaveListListener{
    fun onDismissSaveToListBottomSheet()
    fun onDismissAddListBottomSheet()
    fun onCreateNewListClick()
    fun onSaveToListSuccess()
    fun onSaveToListFailure()
}
interface RecentViewListener{
    fun onClearRecentViewClicked()
    fun onClearRecentSearchClicked()
    fun onDeleteRecentSearchItem(id: Int)
    fun onRecentSearchItemClicked(query: String)
    fun onRecentViewedMediaClicked(viewed: RecentViewedUiModel)
    fun onSaveIconClick(id: Int)
}