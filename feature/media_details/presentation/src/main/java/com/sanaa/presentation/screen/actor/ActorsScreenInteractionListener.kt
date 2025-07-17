package com.sanaa.presentation.screen.actor

interface ActorsScreenInteractionListener {
    fun onBackClicked()
    fun onReadMoreClicked()
    fun onTopMoviesClicked()
    fun onTopSeriesClicked()
    fun onViewAllGalleryClicked()
    fun onSeriesClicked(id: Int)
    fun onMovieClicked(id: Int)
}