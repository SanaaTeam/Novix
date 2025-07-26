package com.sanaa.presentation.api

import androidx.compose.runtime.Composable
import com.sanaa.api.SearchFeatureApi
import com.sanaa.presentation.screen.SearchScreen as SearchScreenUi
import com.sanaa.api.SearchNavigatorApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SearchFeatureApiImpl : SearchFeatureApi, KoinComponent {
    private val navigator: SearchNavigatorApi by inject()

    @Composable
    override fun SearchScreenApi() {
        SearchScreenUi(navigator = navigator)
    }
}