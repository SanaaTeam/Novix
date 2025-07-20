package com.sanaa.presentation.screen.series.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.button.NovixPrimaryButton
import com.sanaa.presentation.R

val boxContainerGradient = Brush.verticalGradient(
    colors = listOf( Color(0x00000000), Color(0xFF0D0608))
)

@Composable
fun BottomContainer(
    modifier: Modifier = Modifier,
    trailerUrl: String? = null,
    onPlayTrailerClicked: () -> Unit = {},
    onSetRateClicked: () -> Unit = {}
) {
    Box(
        modifier = modifier
    ) {
        Box(
            Modifier
                .height(112.dp)
                .fillMaxWidth()
                .background(
                    brush = boxContainerGradient
                )
        )
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NovixPrimaryButton(
                text = null,
                onClick = onSetRateClicked,
                icon = painterResource(R.drawable.icon_star_outlined)
            )
            NovixPrimaryButton(
                text = stringResource(R.string.play_trailer),
                isEnabled = trailerUrl != null,
                modifier = Modifier.weight(1f),
                onClick = onPlayTrailerClicked,
            )
        }
    }
}