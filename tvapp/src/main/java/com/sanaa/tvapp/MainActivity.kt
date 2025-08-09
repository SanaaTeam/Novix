package com.sanaa.tvapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.tvapp.presentation.screens.mediaDetails.tvShowScreen.TvShowScreen
import com.sanaa.tvapp.presentation.screens.home.HomeScreen
import com.sanaa.tvapp.presentation.screens.mediaDetails.movieScreen.MovieDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    private val testDetailsVM :MovieDetailsViewModel by viewModels()

    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        actionBar?.hide()
        setContent {
            val state = testDetailsVM.state.collectAsState()
            Log.d("test99", "onCreate: ${state.value}")

            NovixTheme(true) {
                HomeScreen()
            }
        }
    }
}