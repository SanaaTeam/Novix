package com.sanaa.tvapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.tvapp.presentation.screens.home.HomeScreen
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.tvapp.presentation.screens.login.LoginScreenTv
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        actionBar?.hide()
        setContent {
            NovixTheme {
                LoginScreenTv()
            }
        }
    }
}