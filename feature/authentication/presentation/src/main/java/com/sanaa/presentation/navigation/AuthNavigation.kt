package com.sanaa.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sanaa.api.AuthenticationFeatureApi
import com.sanaa.presentation.AuthenticationScreen

fun NavGraphBuilder.registerAuthenticationScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    composable(route = AuthenticationFeatureApi.route) {
        AuthenticationScreen(
            onLoginSuccess = onLoginSuccess,
            onNavigateToSignUp = onNavigateToSignUp
        )
    }
}