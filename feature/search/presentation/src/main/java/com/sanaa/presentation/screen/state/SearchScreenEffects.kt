package com.sanaa.presentation.screen.state

import com.sanaa.api.StartRoute

sealed class SearchScreenEffects{
    data class NavigateToMediaDetails(val startRoute: StartRoute, val id: Int) : SearchScreenEffects()
}