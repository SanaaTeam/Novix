package com.sanaa.tvapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.tvapp.presentation.screens.mediaDetails.tvShowScreen.TvShowScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NovixTheme {
//                SearchScreen()
                TvShowScreen()
            }

        }
    }
}