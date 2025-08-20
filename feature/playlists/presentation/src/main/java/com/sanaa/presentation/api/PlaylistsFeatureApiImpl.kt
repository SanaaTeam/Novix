package com.sanaa.presentation.api

import androidx.compose.runtime.Composable
import com.sanaa.api.PlaylistsFeatureApi
import com.sanaa.presentation.playListNavigation.PlaylistNavHost
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistsFeatureApiImpl @Inject constructor() : PlaylistsFeatureApi {
    @Composable
    override fun PlaylistsScreenApi() {
        PlaylistNavHost()
    }
}