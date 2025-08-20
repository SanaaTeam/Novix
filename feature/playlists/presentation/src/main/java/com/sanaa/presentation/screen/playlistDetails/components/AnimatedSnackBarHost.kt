package com.sanaa.presentation.screen.playlistDetails.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.animation.FadeSlideInVerticallyFromTop
import com.sanaa.designsystem.design_system.component.animation.FadeSlideOutVerticallyToTop
import com.sanaa.designsystem.design_system.component.snack_bar.SnackBar
import com.sanaa.presentation.screen.playlist.SnackData
import kotlinx.coroutines.delay

@Composable
fun AnimatedSnackBarHost(
    data: SnackData?,
    onDismiss: () -> Unit,
    durationMillis: Long = 2500
) {
    AnimatedVisibility(
        visible = data != null,
        enter = FadeSlideInVerticallyFromTop,
        exit = FadeSlideOutVerticallyToTop,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .statusBarsPadding()
    ) {
        if (data != null) {
            LaunchedEffect(data) {
                delay(durationMillis)
                onDismiss()
            }
            SnackBar(
                message = data.message,
                isErrorMessage = data.isError,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}