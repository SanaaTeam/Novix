package com.sanaa.api

import androidx.compose.runtime.Composable

interface SearchFeatureApi {
    @Composable
    fun SearchScreen(onMediaClick: (startRoute: StartRoute, id: Int) -> Unit)
}