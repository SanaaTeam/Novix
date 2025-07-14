package com.sanaa.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sanaa.api.SavedContentFeatureApi
import com.sanaa.presentation.SavedContentScreen

fun NavGraphBuilder.registerSavedContentScreen(
    onNavigateBack: () -> Unit,
    onMediaClicked: (mediaId: String) -> Unit
) {
    composable(route = SavedContentFeatureApi.route) {
        SavedContentScreen(
            onNavigateBack = onNavigateBack,
            onMediaClicked = onMediaClicked
        )
    }
}