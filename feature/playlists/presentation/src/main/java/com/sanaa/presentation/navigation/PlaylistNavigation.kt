package com.sanaa.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sanaa.api.PlaylistsFeatureApi
import com.sanaa.presentation.PlaylistScreen


fun NavGraphBuilder.registerPlaylistsScreen(
    onNavigateBack: () -> Unit,
    onPlaylistClicked: (playlistId: String) -> Unit
) {
    composable(route = PlaylistsFeatureApi.route) {
        PlaylistScreen(
            onNavigateBack = onNavigateBack,
            onPlaylistClicked = onPlaylistClicked
        )
    }
}