package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixBackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme


@Composable
fun DisconnectScreen(
    onRetryClick: () -> Unit
) {
    NovixScaffold(
        backgroundShapes = { NovixBackgroundShapes() }
    ) { innerPadding ->
        DisconnectScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            onRetryClick = onRetryClick
        )
    }
}

@Composable
fun DisconnectScreen(
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit
) {
    Box(
        modifier = modifier
            .statusBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
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
}

@PreviewLightDark()
@Composable
fun DisconnectScreenPreview() {
    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        DisconnectScreen(onRetryClick = {})
    }
}