package com.sanaa.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sanaa.api.OnboardingFeatureApi
import com.sanaa.presentation.OnboardingScreen

fun NavGraphBuilder.registerOnboardingScreen(
    onNavigateComplete: () -> Unit
) {
    composable(route = OnboardingFeatureApi.route) {
        OnboardingScreen(onNavigate = onNavigateComplete)
    }
}