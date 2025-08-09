package com.sanaa.tvapp.presentation.screens.searchScreen

interface SearchScreenInteractionListener {
    fun onTabSelected(index: Int)
    fun onSearchQueryChanged(query: String)
    fun retrySearch()
    fun onActorClicked(id: Int)
    fun onLoginButtonClick()
}