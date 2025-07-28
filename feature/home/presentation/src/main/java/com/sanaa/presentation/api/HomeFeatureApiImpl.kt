package com.sanaa.presentation.api

import androidx.compose.runtime.Composable
import com.sanaa.api.HomeFeatureApi
import com.sanaa.api.MediaDetailsApi
import com.sanaa.presentation.screen.homeScreen.HomeScreen
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class HomeFeatureApiImpl @Inject constructor(
    private val mediaDetailsApi: MediaDetailsApi,
) : HomeFeatureApi {
    @Composable
    override fun HomeScreenApi() {
        HomeScreen(mediaDetailsApi = mediaDetailsApi)
    }
}