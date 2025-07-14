package com.sanaa.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.sanaa.api.AuthenticationFeatureApi
import com.sanaa.api.HomeFeatureApi
import com.sanaa.api.MediaDetailsFeatureApi
import com.sanaa.api.OnboardingFeatureApi
import com.sanaa.api.PlaylistsFeatureApi
import com.sanaa.api.SavedContentFeatureApi
import com.sanaa.api.SearchFeatureApi
import com.sanaa.api.UserProfileFeatureApi
import com.sanaa.presentation.navigation.registerAuthenticationScreen
import com.sanaa.presentation.navigation.registerHomeScreen
import com.sanaa.presentation.navigation.registerMediaDetailsScreen
import com.sanaa.presentation.navigation.registerOnboardingScreen
import com.sanaa.presentation.navigation.registerPlaylistsScreen
import com.sanaa.presentation.navigation.registerSavedContentScreen
import com.sanaa.presentation.navigation.registerSearchScreen
import com.sanaa.presentation.navigation.registerUserProfileScreen


@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = HomeFeatureApi.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        registerOnboardingScreen(
            onNavigateComplete = {
                navController.navigate(AuthenticationFeatureApi.route) {
                    popUpTo(OnboardingFeatureApi.route) { inclusive = true }
                }
            }
        )

        registerAuthenticationScreen(
            onLoginSuccess = {
                navController.navigate(HomeFeatureApi.route) {
                    popUpTo(AuthenticationFeatureApi.route) { inclusive = true }
                }
            },
            onNavigateToSignUp = { }
        )

        registerHomeScreen(
            onMediaClicked = { mediaId ->
                navController.navigate(MediaDetailsFeatureApi.buildRoute(mediaId))
            },
            onNavigateToSearch = {
                navController.navigate(SearchFeatureApi.route)
            },
            onNavigateToPlaylists = {
                navController.navigate(PlaylistsFeatureApi.route)
            },
            onNavigateToUserProfile = {
                navController.navigate(UserProfileFeatureApi.route)
            }
        )

        registerSearchScreen(
            onNavigateBack = { navController.popBackStack() },
            onMediaClicked = { mediaId ->
                navController.navigate(MediaDetailsFeatureApi.buildRoute(mediaId))
            }
        )

        registerMediaDetailsScreen(
            onNavigateBack = { navController.popBackStack() },
            onActorClicked = { actorId ->

            }
        )

        registerPlaylistsScreen(
            onNavigateBack = { navController.popBackStack() },
            onPlaylistClicked = { playlistId ->

            }
        )

        registerSavedContentScreen(
            onNavigateBack = { navController.popBackStack() },
            onMediaClicked = { mediaId ->
                navController.navigate(MediaDetailsFeatureApi.buildRoute(mediaId))
            }
        )

        registerUserProfileScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToSavedContent = {
                navController.navigate(SavedContentFeatureApi.route)
            },
            onLogout = {
                navController.navigate(AuthenticationFeatureApi.route) {
                    popUpTo(HomeFeatureApi.route) { inclusive = true }
                }
            }
        )
    }
}