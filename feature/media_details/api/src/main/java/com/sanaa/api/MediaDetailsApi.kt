package com.sanaa.api

import androidx.compose.runtime.Composable

interface MediaDetailsApi {
    @Composable
    fun MediaDetailsScreen(mediaId: Int, onBackClick: () -> Unit)
}