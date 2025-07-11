package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
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
            .background(Theme.colors.surface)
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
                onClick = onRetryClick,
                modifier = Modifier
                    .size(width = 72.dp, height = 48.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Theme.colors.stroke),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Theme.colors.surface,
                    contentColor = Theme.colors.body
                ),
                contentPadding = PaddingValues(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.offline_note),
                        style = MaterialTheme.typography.labelLarge,
                        color = Theme.colors.primary,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 800, widthDp = 360)
@Composable
fun DisconnectScreenPreview() {
    NovixTheme {
        DisconnectScreen(onRetryClick = {})
    }
}

@Preview(showBackground = true, heightDp = 800, widthDp = 360)
@Composable
fun DisconnectScreenDarkPreview() {
    NovixTheme(isDarkMode = true) {
        DisconnectScreen(onRetryClick = {})
    }
}
