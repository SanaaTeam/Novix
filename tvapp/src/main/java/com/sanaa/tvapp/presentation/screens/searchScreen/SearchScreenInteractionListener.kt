package com.sanaa.tvapp.presentation.screens.searchScreen

interface SearchScreenInteractionListener {
    fun onTabSelected(index: Int)
    fun onSearchQueryChanged(query: String)
    fun onRetryClicked()
    fun onActorClicked(id: Int)
    fun onMovieClicked(id:Int)
    fun onTvShowClicked(id:Int)
    fun onSnackBarDismiss()
}