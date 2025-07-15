package com.sanaa.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.SearchFeatureApi
import com.sanaa.presentation.screen.SearchScreen
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private const val SEARCH_HOME_ROUTE = "search_home"
private const val MEDIA_DETAILS_ROUTE = "media_details/{mediaId}"

class SearchFeatureApiImpl : SearchFeatureApi, KoinComponent {

    private val mediaDetailsApi: MediaDetailsApi by inject()

    @Composable
    override fun SearchFeature() {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = SEARCH_HOME_ROUTE) {

            composable(SEARCH_HOME_ROUTE) {
                SearchScreen(
                    onMediaClick = { mediaId ->
                        navController.navigate("media_details/$mediaId")
                    }
                )
            }

            composable(
                route = MEDIA_DETAILS_ROUTE,
                arguments = listOf(navArgument("mediaId") { type = NavType.IntType })
            ) { backStackEntry ->
                val mediaId = backStackEntry.arguments?.getInt("mediaId")
                requireNotNull(mediaId)

                mediaDetailsApi.MediaDetailsScreen(
                    mediaId = mediaId,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}