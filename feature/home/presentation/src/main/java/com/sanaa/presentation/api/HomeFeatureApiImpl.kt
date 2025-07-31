package com.sanaa.presentation.api

import androidx.compose.runtime.Composable
import com.sanaa.api.HomeFeatureApi
import com.sanaa.presentation.app.NovixApp
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeFeatureApiImpl @Inject constructor() : HomeFeatureApi {
    @Composable
    override fun HomeScreenApi() {
        NovixApp()
    }
}