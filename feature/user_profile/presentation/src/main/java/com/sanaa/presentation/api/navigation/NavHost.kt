package com.sanaa.presentation.api.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sanaa.presentation.screen.changePassword.ChangePasswordWebView
import com.sanaa.presentation.screen.myAccount.MyAccountScreen
import com.sanaa.presentation.screen.myRating.MyRatingScreen
import com.sanaa.presentation.screen.WatchingHistoryScreen
@Composable
fun AccountNavHost(
    resetKey: Any = Unit,
) {
    key(resetKey) {
        val appNavController = rememberNavController()
        CompositionLocalProvider(LocalNavControllerProvider provides appNavController) {
            NavHost(
                navController = appNavController,
                startDestination = MyAccountScreenRoute,
            ) {
                composable<MyAccountScreenRoute> {
                    MyAccountScreen()
                }

                composable<MyRatingScreenRoute> {
                    MyRatingScreen()
                }
                composable(ChangePasswordScreenRoute::class) {
                    ChangePasswordWebView(url = "https://www.themoviedb.org/settings/profile")
                }

            }
        }
        composable<WatchingHistoryScreenRoute> {
            WatchingHistoryScreen()
        }

    }
}
    }
}