package com.sanaa.tvapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.tvapp.presentation.api.LocalSafeContentThreshold
import com.sanaa.tvapp.presentation.screens.navigation.LocalAppNavController
import com.sanaa.tvapp.presentation.screens.navigation.NavBarRoute
import com.sanaa.tvapp.presentation.screens.navigation.TvNavGraph
import com.sanaa.tvapp.presentation.screens.navigation.TvNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        actionBar?.hide()

        splashScreen.setKeepOnScreenCondition {
            !viewModel.state.value.isReady
        }

        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            CompositionLocalProvider(
                LocalSafeContentThreshold provides state.safeContentThreshold
            ) {
                NovixTheme(true) {
                    val navController = rememberNavController()
                    val startDestination = NavBarRoute.Home

                    CompositionLocalProvider(LocalAppNavController provides navController) {
                        TvNavigation {
                            TvNavGraph(
                                navController = navController,
                                startDestination = startDestination
                            )
                        }
                    }
                }
            }
        }
    }
}