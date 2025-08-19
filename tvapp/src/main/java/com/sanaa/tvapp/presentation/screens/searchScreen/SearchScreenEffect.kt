package com.sanaa.tvapp.presentation.screens.searchScreen

sealed class SearchScreenEffect {
    data class NavigateToMovieDetails(val id:Int):SearchScreenEffect()
    data class NavigateToTvShowDetails(val id:Int):SearchScreenEffect()
    data class NavigateToActorDetails(val id:Int):SearchScreenEffect()
    data object NavigateToLogin:SearchScreenEffect()
}