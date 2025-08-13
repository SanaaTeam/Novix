package com.sanaa.presentation.screen.trendingTvShowScreen

interface TrendingTvShowsScreenInteractionListener {
    fun onGenreClick(id: Int?)
    fun onMediaClick(id: Int)
    fun onBackClick()
    fun onRetryClick()
    fun onSnackBarDismiss()
}