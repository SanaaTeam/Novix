package com.sanaa.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sanaa.api.UserProfileFeatureApi
import com.sanaa.presentation.UserProfileScreen

fun NavGraphBuilder.registerUserProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSavedContent: () -> Unit,
    onLogout: () -> Unit
) {
    composable(route = UserProfileFeatureApi.route) {
        UserProfileScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToSavedContent = onNavigateToSavedContent,
            onLogout = onLogout
        )
    }
}