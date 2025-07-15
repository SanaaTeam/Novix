package com.sanaa.novix.navigation

import androidx.compose.runtime.Composable
import com.sanaa.api.SearchFeatureApi
import org.koin.compose.koinInject

@Composable
fun AppNavigation() {
    val searchFeature: SearchFeatureApi = koinInject()
    searchFeature.SearchFeature()
}