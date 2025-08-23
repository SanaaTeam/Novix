package com.sanaa.tvapp.presentation.screens.searchScreen.componants

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
import com.sanaa.tvapp.state.SnackData
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun AnimatedSnackBarHost(
    data: SnackData?,
    onDismiss: () -> Unit,
    durationMillis: Duration = 2.5.seconds,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = data != null,
        enter = FadeSlideInVerticallyFromTop,
        exit = FadeSlideOutVerticallyToTop,
        modifier = modifier
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
