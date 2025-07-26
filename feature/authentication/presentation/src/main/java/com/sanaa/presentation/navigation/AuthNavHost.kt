package com.sanaa.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.screen.login.LoginScreen
import com.sanaa.presentation.screen.welcome.WelcomeScreen
import com.sanaa.presentation.webview.ResetPasswordWebViewScreen
import com.sanaa.presentation.webview.WebViewScreen

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
                    WebViewScreen(url = "https://www.themoviedb.org/signup")
                }

                composable(ApproveAccessToken::class) {
                    val requestToken = it.toRoute<ApproveAccessToken>().requestToken
                    WebViewScreen(url = "https://www.themoviedb.org/auth/access?request_token=$requestToken"){
                        // TODO Navigate to the home screen
                    }
                }

                composable(ForgetPasswordRoute::class) { entry ->
                    ResetPasswordWebViewScreen(url = "https://www.themoviedb.org/reset-password")
                }
            }
        }
    }
}
