package com.sanaa.tvapp.presentation.screens.mediaDetails.episodeScreen

interface EpisodeDetailsInteractionListener {
    fun onPlayTrailerClick()
    fun onRetryLoadDetails()
    fun onActorClick(id: Int)
}