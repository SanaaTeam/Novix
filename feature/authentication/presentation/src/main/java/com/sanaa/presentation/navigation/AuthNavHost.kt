package com.sanaa.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.screen.login.LoginScreen
import com.sanaa.presentation.screen.welcome.WelcomeScreen
import com.sanaa.presentation.webview.ResetPasswordWebViewScreen
import com.sanaa.presentation.webview.SignUpWebViewScreen

@Composable
fun AuthNavHost() {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavControllerProvider provides navController) {
        NovixTheme(isDarkMode = isSystemInDarkTheme()) {
            NavHost(
                navController = navController,
                startDestination = WelcomeRoute(),
                modifier = Modifier.background(Theme.colors.surface)
            ) {
                composable(WelcomeRoute::class) {
                    WelcomeScreen()
                }

                composable(LoginRoute::class) {
                    LoginScreen()
                }

                composable(SignUpRoute::class) { entry ->
                    SignUpWebViewScreen(url = "https://www.themoviedb.org/signup")
                }

                composable(ForgetPasswordRoute::class) { entry ->
                    ResetPasswordWebViewScreen(url = "https://www.themoviedb.org/reset-password")
                }
            }
        }
    }
}
