package com.sanaa.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sanaa.api.HomeFeatureApi
import com.sanaa.presentation.HomeScreen

fun NavGraphBuilder.registerHomeScreen(
    onMediaClicked: (mediaId: String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToPlaylists: () -> Unit,
    onNavigateToUserProfile: () -> Unit
) {
    composable(route = HomeFeatureApi.route) {
        HomeScreen(
            onMediaClicked = onMediaClicked,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToPlaylists = onNavigateToPlaylists,
            onNavigateToUserProfile = onNavigateToUserProfile
        )
    }
}