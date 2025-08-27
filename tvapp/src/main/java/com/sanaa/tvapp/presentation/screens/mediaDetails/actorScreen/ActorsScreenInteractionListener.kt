package com.sanaa.tvapp.presentation.screens.mediaDetails.actorScreen

interface ActorsScreenInteractionListener {
    fun onTvShowClicked(id: Int)
    fun onMovieClicked(id: Int)
    fun onSnackDismissRequested()
    fun onRetryClicked()
    fun onReadMoreClicked()
}