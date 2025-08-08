package com.sanaa.tvapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.tvapp.presentation.screens.navigation.TvNavGraph
import com.sanaa.tvapp.presentation.screens.navigation.TvNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NovixTheme {
            val navController = rememberNavController()
            TvNavigation(navController = navController) {
                TvNavGraph(navController = navController)
            }
            }
        }
    }
}