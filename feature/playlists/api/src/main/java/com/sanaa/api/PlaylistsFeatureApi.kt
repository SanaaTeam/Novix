package com.sanaa.api

import android.content.Context
import androidx.compose.runtime.Composable

interface PlaylistsFeatureApi {
    @Composable
    fun PlaylistsScreenApi()
    fun launchPlaylistScreen(context: Context)
}