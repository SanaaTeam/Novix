package com.sanaa.tvapp.presentation.screens.mediaDetails.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Icon
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.R


@Composable
fun TrailerAndRateSection(
    modifier: Modifier = Modifier,
    trailerUrl: String? = null,
    onPlayTrailerClicked: () -> Unit = {},
    onRateClicked: () -> Unit = {},
    isFilledStarIcon: Boolean = false,
    isRateButtonVisible: Boolean = true,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = {
                onPlayTrailerClicked()
            },
            enabled = trailerUrl != null,
            colors = ButtonDefaults.colors(
                containerColor = Theme.colors.disable,
                focusedContainerColor = Theme.colors.primary
            ),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .graphicsLayer(scaleX = -1f),
                    painter = painterResource(R.drawable.icon_back_tringle),
                    contentDescription = null,
                    tint = Theme.colors.onPrimary
                )
                Text(
                    text = stringResource(R.string.play_trailer),
                    color = Theme.colors.onPrimary,
                    modifier = Modifier,
                    style = Theme.textStyle.label.large
                )
            }
        }
        if (isRateButtonVisible) {
            Button(
                onClick = onRateClicked,
                colors = ButtonDefaults.colors(
                    containerColor = Theme.colors.disable,
                    focusedContainerColor = Theme.colors.primary
                ),
            ) {
                val icon = if (isFilledStarIcon) R.drawable.star else R.drawable.outlined_star

                Image(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}


@Preview
@Composable
fun TrailerPreview() {
    TrailerAndRateSection()
}