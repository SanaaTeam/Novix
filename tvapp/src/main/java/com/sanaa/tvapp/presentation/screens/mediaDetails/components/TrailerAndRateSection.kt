package com.sanaa.tvapp.presentation.screens.mediaDetails.components

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonBorder
import androidx.tv.material3.ButtonColors
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Icon
import com.sanaa.designsystem.design_system.component.button.PrimaryButton
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.designsystem.R as designSystemResource
import com.sanaa.tvapp.R


@Composable
fun TrailerAndRateSection(
    modifier: Modifier = Modifier,
    trailerUrl: String? = null,
    onPlayTrailerClicked: () -> Unit = {},
) {
    var isFocused by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = {
                onPlayTrailerClicked()
            },
            modifier = Modifier
                .onFocusChanged{focusState ->
                    isFocused = focusState.isFocused
                },
            enabled =  trailerUrl != null,
            colors = ButtonDefaults.colors(
                containerColor = Theme.colors.disable,
                focusedContainerColor = Theme.colors.primary
            ),
        ) {
            Row (
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
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
    }
}


@Preview
@Composable
fun TrailerPreview() {
    TrailerAndRateSection()
}