package com.sanaa.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.sanaa.api.MediaDetailsApi

class MediaDetailsApiImpl : MediaDetailsApi {
    @Composable
    override fun MediaDetailsScreen(mediaId: Int, onBackClick: () -> Unit) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Media Details for ID: $mediaId", fontSize = 24.sp)
            Button(onClick = onBackClick) {
                Text("Go Back")
            }
        }
    }
}