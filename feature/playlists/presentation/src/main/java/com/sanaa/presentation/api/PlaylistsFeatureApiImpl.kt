package com.sanaa.presentation.api

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.sanaa.api.PlaylistsFeatureApi
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.screen.saved.PlaylistScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistsFeatureApiImpl @Inject constructor() : PlaylistsFeatureApi {
    @Composable
    override fun PlaylistsScreenApi() {
        NovixTheme(isSystemInDarkTheme()) {
            PlaylistScreen()
        }
    }

    override fun launchPlaylistScreen(context: Context) {
        val intent = Intent(context, PlaylistActivity::class.java)
        context.startActivity(intent)
    }
}

@AndroidEntryPoint
class PlaylistActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NovixTheme(isSystemInDarkTheme()) {
                PlaylistScreen()
            }
        }
    }
}

