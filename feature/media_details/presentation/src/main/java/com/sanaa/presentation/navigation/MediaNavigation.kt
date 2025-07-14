package com.sanaa.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sanaa.api.MediaDetailsFeatureApi
import com.sanaa.presentation.MediaDetailsScreen

fun NavGraphBuilder.registerMediaDetailsScreen(
    onNavigateBack: () -> Unit,
    onActorClicked: (actorId: String) -> Unit
) {
    composable(
        route = MediaDetailsFeatureApi.route,
        arguments = MediaDetailsFeatureApi.arguments
    ) { backStackEntry ->
        val mediaId = backStackEntry.arguments?.getString(MediaDetailsFeatureApi.ARG_MEDIA_ID)
            ?: "ID_NOT_FOUND"

        MediaDetailsScreen(
            mediaId = mediaId,
            onNavigateBack = onNavigateBack,
            onActorClicked = onActorClicked
        )
    }
}