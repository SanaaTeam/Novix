package com.sanaa.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sanaa.api.AuthStartRoute
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.screen.login.LoginScreen
import com.sanaa.presentation.screen.welcome.WelcomeScreen
import com.sanaa.presentation.webview.ResetPasswordWebViewScreen
import com.sanaa.presentation.webview.WebViewScreen

@Composable
fun AuthNavHost( startRoute: AuthStartRoute) {
    val navController = rememberNavController()
    val startDestination = when (startRoute) {
        AuthStartRoute.Welcome -> WelcomeRoute()
        AuthStartRoute.Login -> LoginRoute
        AuthStartRoute.SignUp -> SignUpRoute
        AuthStartRoute.ForgetPassword -> ForgetPasswordRoute
    }

    CompositionLocalProvider(LocalNavControllerProvider provides navController) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.background(Theme.colors.surface)
        ) {
            composable(WelcomeRoute::class) {
                WelcomeScreen()
            }

            composable(LoginRoute::class) {
                LoginScreen()
            }

            composable(SignUpRoute::class) { entry ->
                WebViewScreen(url = "https://www.themoviedb.org/signup")
            }

            composable(ForgetPasswordRoute::class) { entry ->
                ResetPasswordWebViewScreen(url = "https://www.themoviedb.org/reset-password")
            }
        }
    }
}