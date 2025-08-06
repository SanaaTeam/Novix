package com.sanaa.designsystem.design_system.component.top_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun TopBarClickableIcon(
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = "",
    tint: androidx.compose.ui.graphics.Color = Theme.colors.title

) {
    Box(
        modifier = modifier
            .size(40.dp)
            .background(
                color = Theme.colors.iconBackgroundLow,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = Theme.colors.stroke,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = tint
        )
    }
}

@PreviewLightDark
@Composable
private fun TopBarClickableIconPreview() {
    NovixTheme(
        isSystemInDarkTheme()
    ) {
        Column(
            modifier = Modifier
                .background(Theme.colors.surface)
                .padding(
                    16.dp
                )
        ) {
            TopBarClickableIcon(
                icon = painterResource(id = R.drawable.icon_plus),
                onClick = {}
            )
        }
    }
}