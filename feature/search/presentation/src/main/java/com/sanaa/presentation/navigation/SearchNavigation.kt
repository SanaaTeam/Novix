package com.sanaa.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sanaa.api.SearchFeatureApi
import com.sanaa.presentation.screen.SearchScreen

fun NavGraphBuilder.registerSearchScreen(
    onNavigateBack: () -> Unit,
    onMediaClicked: (mediaId: String) -> Unit
) {
    composable(route = SearchFeatureApi.route) {
        SearchScreen(
            onNavigateBack = onNavigateBack,
            onMediaClicked = onMediaClicked
        )
    }
}