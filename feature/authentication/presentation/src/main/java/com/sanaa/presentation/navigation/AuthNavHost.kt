package com.sanaa.presentation.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.webview.ForgetPasswordWebViewRoute
import com.sanaa.presentation.webview.SignUpWebViewRoute
import com.sanaa.presentation.webview.SignUpWebViewScreen
import com.sanaa.presentation.webview.ResetPasswordWebViewScreen

@Composable
fun AuthNavHost() {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavControllerProvider provides navController) {
        NovixTheme(isDarkMode = isSystemInDarkTheme()) {
            NavHost(
                navController = navController,
                startDestination = "signup?url=https://www.themoviedb.org/signup"
            ) {
                composable(
                    route = "signup?url={url}",
                    arguments = listOf(
                        navArgument("url") {
                            type = NavType.StringType
                            defaultValue = "https://www.themoviedb.org/signup"
                        }
                    )
                ) { entry ->
                    val url = entry.arguments?.getString("url") ?: "https://www.themoviedb.org/signup"
                    SignUpWebViewScreen(url = url)
                }

                composable(
                    route = "forget_password?url={url}",
                    arguments = listOf(
                        navArgument("url") {
                            type = NavType.StringType
                            defaultValue = "https://www.themoviedb.org/reset-password"
                        }
                    )
                ) { entry ->
                    val url = entry.arguments?.getString("url") ?: "https://www.themoviedb.org/reset-password"
                    ResetPasswordWebViewScreen(url = url)
                }
            }
        }
    }
}
