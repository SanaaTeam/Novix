package com.sanaa.presentation.api

import androidx.compose.runtime.Composable
import com.sanaa.api.SearchFeatureApi
import com.sanaa.api.SearchNavigatorApi
import javax.inject.Inject
import javax.inject.Singleton
import com.sanaa.presentation.screen.SearchScreen as SearchScreenUi

@Singleton
class SearchFeatureApiImpl @Inject constructor(
    private val navigator: SearchNavigatorApi
) : SearchFeatureApi {
    @Composable
    override fun SearchScreenApi() {
        SearchScreenUi(navigator = navigator)
    }
}