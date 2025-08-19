package com.sanaa.presentation.screen.playlist.componants

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.snack_bar.SnackBar
import com.sanaa.presentation.screen.playlist.SnackData
import kotlinx.coroutines.delay

@Composable
fun AnimatedSnackBarHost(
    data: SnackData?,
    onDismiss: () -> Unit,
    durationMillis: Long = 2500
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AnimatedVisibility(
            visible = data != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
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
}
