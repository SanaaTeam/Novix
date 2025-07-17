package com.sanaa.presentation.screen.navigation

import androidx.compose.runtime.Composable
import com.sanaa.api.SearchFeatureApi
import com.sanaa.presentation.screen.SearchScreen as SearchScreenUi

class SearchFeatureApiImpl : SearchFeatureApi {
    @Composable
    override fun SearchScreen(NavigateToMediaScreen: (Int) -> Unit) {
        SearchScreenUi(onMediaClick = NavigateToMediaScreen)
    }
}