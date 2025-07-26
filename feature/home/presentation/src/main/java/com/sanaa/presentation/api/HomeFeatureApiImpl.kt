package com.sanaa.presentation.api

import androidx.compose.runtime.Composable
import com.sanaa.api.HomeFeatureApi
import com.sanaa.api.SearchFeatureApi
import com.sanaa.presentation.app.NovixApp
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeFeatureApiImpl : HomeFeatureApi, KoinComponent {

    private val searchFeatureApi: SearchFeatureApi by inject()

    @Composable
    override fun HomeScreenApi() {
        NovixApp()
    }
}