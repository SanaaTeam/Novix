package com.sanaa.designsystem.design_system.component.chips

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun SaveIconChip(
    modifier: Modifier = Modifier,
    isSaved: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(
                RoundedCornerShape(8.dp)
            )
            .background(
                color = Theme.colors.iconBackground,
            )
            .clickable(
                onClick = onClick
            )
            .border(
                width = 1.dp, color = Theme.colors.stroke,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center,
    ) {
        Crossfade(isSaved) { saved ->
            if (saved) {
                Image(
                    painter = painterResource(R.drawable.icon_saved),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Theme.colors.onPrimary),
                    modifier = Modifier.size(20.dp)

                )
            } else {
                Image(
                    painter = painterResource(R.drawable.icon_unsaved),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Theme.colors.onPrimary),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSaveChip() {
    NovixTheme(isSystemInDarkTheme()) {
        var isSaved by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .background(color = Theme.colors.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SaveIconChip(isSaved = isSaved, onClick = { isSaved = !isSaved })
            SaveIconChip(isSaved = false, onClick = {})
        }
    }
}