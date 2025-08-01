package com.sanaa.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.snack_bar.NovixSnackBar
import kotlinx.coroutines.delay

@Composable
fun NovixAnimatedSnackBarHost(
    data: SnackData?,
    onDismiss: () -> Unit,
    durationMillis: Long = 2500
) {
    AnimatedVisibility(
        visible = data != null,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
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
            NovixSnackBar(
                message = data.message,
                isErrorMessage = data.isError,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

data class SnackData(
    val message: String,
    val isError: Boolean
)
