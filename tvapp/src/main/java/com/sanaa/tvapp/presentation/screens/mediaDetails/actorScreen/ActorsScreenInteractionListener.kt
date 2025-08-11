package com.sanaa.tvapp.presentation.screens.mediaDetails.actorScreen

interface ActorsScreenInteractionListener {
    fun onBackClicked()
    fun onTvShowClicked(id: Int)
    fun onMovieClicked(id: Int)
    fun onRetryClicked()
}