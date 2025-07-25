package com.sanaa.presentation.api

import androidx.compose.runtime.Composable
import com.sanaa.api.SearchFeatureApi
import com.sanaa.api.StartRoute
import com.sanaa.presentation.screen.SearchScreen as SearchScreenUi

class SearchFeatureApiImpl : SearchFeatureApi {
    @Composable
    override fun SearchScreen(onMediaClick: (startRoute: StartRoute, id: Int) -> Unit) {
        SearchScreenUi(onMediaClick = onMediaClick)
    }
}