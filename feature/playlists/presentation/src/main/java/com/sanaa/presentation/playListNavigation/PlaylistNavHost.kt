package com.sanaa.presentation.playListNavigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sanaa.designsystem.design_system.component.api.LocalBottomBarVisibility
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.PlayListViewModel
import com.sanaa.presentation.playListProviders.LocalNavControllerProvider
import com.sanaa.presentation.playListProviders.LocalThemeProvider
import com.sanaa.presentation.screen.playlist.PlayListScreenViewModel
import com.sanaa.presentation.screen.playlist.PlaylistScreen
import com.sanaa.presentation.screen.playlistDetails.PlaylistDetailsScreen
import com.sanaa.presentation.screen.playlistDetails.PlaylistDetailsScreenViewModel

@Composable
fun PlaylistNavHost(
    viewModel: PlayListViewModel = hiltViewModel()
) {

    val state = viewModel.state.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val bottomBarVisible = LocalBottomBarVisibility.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(navBackStackEntry) {
        bottomBarVisible.value =
            navBackStackEntry?.destination?.route == PlaylistsScreenRoute.route()
    }

    NovixTheme(
        isDarkMode = state.value.isDarkMode
    ) {
        CompositionLocalProvider(
            LocalNavControllerProvider provides navController,
            LocalThemeProvider provides state.value.isDarkMode
        ) {
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
}