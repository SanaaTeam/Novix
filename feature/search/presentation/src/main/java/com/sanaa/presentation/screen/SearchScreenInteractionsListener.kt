package com.sanaa.presentation.screen

import android.media.browse.MediaBrowser.MediaItem
import com.sanaa.presentation.screen.state.MovieUiModel
import com.sanaa.presentation.screen.state.RecentViewedUiModel

interface SearchScreenInteractionsListener {
    fun onClearRecentViewClicked()
    fun onClearRecentSearchClicked()
    fun onDeleteRecentSearchItem(id: Int)
    fun onTabSelected(index: Int)
    fun onRecentSearchItemClicked(query: String)
    fun onRecentViewedMediaClicked(viewed: RecentViewedUiModel)
    fun onSearchQueryChanged(query: String)
    fun retrySearch()
    fun onActorClicked(id: Int)
    fun onSearchResultMediaClicked(viewed: RecentViewedUiModel)
    fun onLoginButtonClick()
    fun onSaveMoviesClicked()
    fun onBottomSheetDismiss()
    fun onSaveIconClick(media: MovieUiModel)
    fun onDismissSaveToListBottomSheet()
    fun onCreateNewListClick()
    fun onDismissAddListBottomSheet()
    fun onSnackBarDismiss()
    fun onSaveToListSuccess()
    fun onSaveToListFailure()
}