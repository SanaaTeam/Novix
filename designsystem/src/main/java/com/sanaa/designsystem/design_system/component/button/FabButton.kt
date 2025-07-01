package com.sanaa.designsystem.design_system.component.button

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.button.common.AnimatedLoadingIndicator
import com.sanaa.designsystem.design_system.component.modifiers.dropShadow
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun FabButton(
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    backgroundColor: Color = Theme.colors.primary,
    iconTint: Color = Theme.colors.onPrimary,
) {
    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isEnabled) backgroundColor else Theme.colors.disable,
    )
    val animatedIconTint by animateColorAsState(
        targetValue = if (isEnabled) iconTint else Theme.colors.onPrimaryHint
    )
    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .background(color = animatedBackgroundColor)
            .then(
                if (isEnabled) Modifier.dropShadow(
                    color = Color(0x1FC65A42),
                    blur = 12.dp,
                    offsetX = 0.dp,
                    offsetY = 4.dp
                )
                else Modifier
            )
            .size(size = 56.dp)
            .clickable(
                enabled = isEnabled && !isLoading,
                onClick = onClick
            )
            .padding(all = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Crossfade(targetState = isLoading) { loading ->
            if (loading) {
                AnimatedLoadingIndicator(
                    iconTint = iconTint,
                )
            } else {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = animatedIconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewFabButton() {
    NovixTheme(
        isSystemInDarkTheme()
    ) {
        Column(
            modifier = Modifier
                .background(color = Theme.colors.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FabButton(
                icon = painterResource(id = R.drawable.icon_plus),
                onClick = {},
                isLoading = true
            )
            FabButton(
                icon = painterResource(id = R.drawable.icon_plus),
                onClick = {},
                isLoading = false
            )
            FabButton(
                icon = painterResource(id = R.drawable.icon_plus),
                onClick = {},
                isLoading = false,
                isEnabled = false
            )
        }
    }
}