package com.sanaa.presentation.api

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.sanaa.api.PlaylistsFeatureApi

class PlaylistsFeatureApiImpl : PlaylistsFeatureApi {
    @Composable
    override fun PlaylistsScreenApi() {
        PlaylistsScreenPlaceholder()
    }
}

@Composable
private fun PlaylistsScreenPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Playlists (Category) Screen", fontSize = 24.sp)
    }
}