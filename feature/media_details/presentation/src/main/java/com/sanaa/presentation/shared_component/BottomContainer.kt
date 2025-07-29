package com.sanaa.presentation.shared_component

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
import androidx.compose.ui.zIndex
import com.sanaa.designsystem.design_system.component.button.NovixPrimaryButton
import com.sanaa.feature.mediadetails.presentation.R



@Composable
fun BottomContainer(
    modifier: Modifier = Modifier,
    trailerUrl: String? = null,
    onPlayTrailerClicked: () -> Unit = {},
    onSetRateClicked: () -> Unit = {}
) {
        Row(
            modifier = modifier
                .zIndex(1f)
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xFF0D0608))
                    )
                )
                .padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 24.dp),
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
