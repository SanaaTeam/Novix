package com.sanaa.presentation.screen.trendingPeopleScreen

interface TrendingPeopleScreenInteractionListener {
    fun onBackClick()
    fun onActorClick(actorId: Int)
    fun onRetryClick()
    fun onSnackBarDismiss()
}