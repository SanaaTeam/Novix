package com.sanaa.presentation.screen.playlistDetails.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.button.PrimaryButton
import com.sanaa.feature.playlists.presentation.R

@Composable
fun RefreshButton(onRetryClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        PrimaryButton(
            text = null,
            icon = painterResource(R.drawable.icon_refresh),
            onClick = onRetryClick,
            modifier = Modifier
                .width(52.dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
        )
    }
}