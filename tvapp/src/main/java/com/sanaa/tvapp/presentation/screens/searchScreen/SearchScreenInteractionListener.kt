package com.sanaa.tvapp.presentation.screens.searchScreen

import com.sanaa.presentation.screen.state.RecentViewedUiModel

interface SearchScreenInteractionListener {
    fun onTabSelected(index: Int)
    fun onSearchQueryChanged(query: String)
    fun retrySearch()
    fun onActorClicked(id: Int)
    fun onLoginButtonClick()
    fun onSearchResultMediaClicked()
}