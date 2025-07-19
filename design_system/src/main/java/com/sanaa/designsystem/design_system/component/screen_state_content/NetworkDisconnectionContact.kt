package com.sanaa.designsystem.design_system.component.screen_state_content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme


@Composable
fun NetworkDisconnectionContact(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
    errorTitle: String = stringResource(id = R.string.offline_title),
    errorMessage: String = stringResource(id = R.string.offline_subtitle)
) {
    ErrorStateContent(
        onRetryClick = onRetryClick,
        modifier = modifier,
        errorTitle = errorTitle,
        errorMessage = errorMessage
    ) {
        val iconPainter = if (isSystemInDarkTheme()) {
            painterResource(id = R.drawable.disconnect_dark_icon)
        } else {
            painterResource(id = R.drawable.disconnect_light_icon)
        }

        Image(
            painter = iconPainter,
            contentDescription = "disconnect",
            modifier = Modifier.size(82.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@PreviewLightDark()
@Composable
fun DisconnectContactPreview() {
    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        NetworkDisconnectionContact(
            onRetryClick = {},
            modifier = Modifier.background(color = Theme.colors.surface)
        )
    }
}