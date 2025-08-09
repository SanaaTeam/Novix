package com.sanaa.tvapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.tvapp.presentation.screens.login.LoginScreenTv
import com.sanaa.tvapp.presentation.screens.mediaDetails.episodeScreen.EpisodeDetailsScreen
import com.sanaa.tvapp.presentation.screens.searchScreen.SearchScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        actionBar?.hide()
        setContent {
            NovixTheme(isSystemInDarkTheme()) {
//                HomeScreen()
//                MovieDetailsScreen()
//                TvShowScreen()
//                EpisodeDetailsScreen()
//                LoginScreenTv()
                SearchScreen()
            }
        }
    }
}