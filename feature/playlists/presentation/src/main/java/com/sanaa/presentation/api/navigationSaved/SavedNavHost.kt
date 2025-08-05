package com.sanaa.presentation.api.navigationSaved

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sanaa.presentation.screen.saved.PlaylistScreen
import com.sanaa.presentation.screen.savedDetails.SavedDetailsScreen

@Composable
fun SavedNavHost() {
    val appNavController = rememberNavController()

    CompositionLocalProvider(LocalNavControllerProvider provides appNavController) {
        NavHost(
            navController = appNavController,
            startDestination = PlaylistScreenRoute,
        ) {
            composable<PlaylistScreenRoute> {
                PlaylistScreen()
            }

            composable<SavedDetailsScreenRoute> {
                SavedDetailsScreen()
            }

        }
    }
}
