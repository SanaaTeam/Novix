package com.sanaa.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sanaa.designsystem.design_system.component.navigation.LocalNavControllerProvider
import com.sanaa.presentation.api.navigationSaved.PlaylistsScreenRoute
import com.sanaa.presentation.api.navigationSaved.SavedDetailsScreenRoute
import com.sanaa.presentation.screen.playlist.PlayListScreenViewModel
import com.sanaa.presentation.screen.playlist.PlaylistScreen
import com.sanaa.presentation.screen.playlistDetails.PlaylistDetailsScreen
import com.sanaa.presentation.screen.playlistDetails.PlaylistDetailsScreenViewModel

@Composable
fun PlaylistNavHost() {

    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavControllerProvider provides navController) {
        NavHost(
            navController = navController,
            startDestination = PlaylistsScreenRoute.route()
        ) {

            composable(route = PlaylistsScreenRoute.PATTERN) {
                val viewModel: PlayListScreenViewModel = hiltViewModel()
                PlaylistScreen(viewModel = viewModel)
            }

            composable(
                route = SavedDetailsScreenRoute.PATTERN,
                arguments = listOf(
                    navArgument(SavedDetailsScreenRoute.ARG_LIST_ID) {
                        type = NavType.IntType
                    },
                    navArgument(SavedDetailsScreenRoute.ARG_LIST_TITLE) {
                        type = NavType.StringType
                    }

                )
            ) {
                val viewModel: PlaylistDetailsScreenViewModel = hiltViewModel()
                PlaylistDetailsScreen(viewModel = viewModel)
            }
        }
    }
}