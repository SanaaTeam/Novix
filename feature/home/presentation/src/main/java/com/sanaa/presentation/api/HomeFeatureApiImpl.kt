package com.sanaa.presentation.api

import androidx.compose.runtime.Composable
import com.sanaa.api.HomeFeatureApi
import com.sanaa.api.MediaDetailsApi
import com.sanaa.presentation.screen.homeScreen.HomeScreen
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeFeatureApiImpl @Inject constructor(
    private val mediaDetailsApi: MediaDetailsApi,
) : HomeFeatureApi {
    @Composable
    override fun HomeScreenApi() {
        HomeScreen(mediaDetailsApi = mediaDetailsApi)
    }
}