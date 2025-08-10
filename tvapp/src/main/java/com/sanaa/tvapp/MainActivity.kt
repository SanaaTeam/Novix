package com.sanaa.tvapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.tvapp.presentation.screens.navigation.LocalAppNavController
import com.sanaa.tvapp.presentation.screens.navigation.NavBarRoute
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute
import com.sanaa.tvapp.presentation.screens.navigation.TvNavGraph
import com.sanaa.tvapp.presentation.screens.navigation.TvNavigation
import com.sanaa.tvapp.presentation.screens.home.HomeScreen
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import usecase.CheckIfUserIsLoggedInUseCase


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        actionBar?.hide()
        setContent {
            NovixTheme(isSystemInDarkTheme()) {
                val navController = rememberNavController()

                val isLoggedIn by checkIfUserIsLoggedInUseCase.isLoggedIn().collectAsState(initial = false)

                val startDestination = if (isLoggedIn) {
                    NavBarRoute.Home
                } else {
                    ScreensRoute.Login
                }

                CompositionLocalProvider(LocalAppNavController provides navController) {
                    TvNavigation {
                        TvNavGraph(navController = navController, startDestination = startDestination)
                    }
                }
            }
        }
    }
}