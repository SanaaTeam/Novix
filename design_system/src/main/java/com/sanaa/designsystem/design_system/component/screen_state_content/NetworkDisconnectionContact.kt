package com.sanaa.designsystem.design_system.component.screen_state_content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.button.OutlinedButton
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme


@Composable
fun NetworkDisconnectionContact(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val iconPainter = if (isSystemInDarkTheme()) {
                painterResource(id = R.drawable.disconnect_dark_icon)
            } else {
                painterResource(id = R.drawable.disconnect_light_icon)
            }

            Image(
                painter =  iconPainter,
                contentDescription = "disconnect",
                modifier = Modifier.size(82.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.offline_title),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = Theme.colors.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.offline_subtitle),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = Theme.colors.body,
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                text = stringResource(id = R.string.offline_note),
                onClick = onRetryClick,
            )
        }

}

@PreviewLightDark()
@Composable
fun DisconnectContactPreview() {
    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        NetworkDisconnectionContact(onRetryClick = {},modifier = Modifier.background(color = Theme.colors.surface))
    }
}