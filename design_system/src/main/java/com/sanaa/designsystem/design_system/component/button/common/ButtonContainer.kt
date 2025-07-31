package com.sanaa.designsystem.design_system.component.button.common

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.Theme


@Composable
internal fun ButtonContainer(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
    backgroundColor: Color = Theme.colors.primary,
    icon: Painter? = null,
    iconTint: Color = Theme.colors.onPrimary,
    verticalPadding: Dp = 8.dp,
    horizontalPadding: Dp = 16.dp,
    shape: Shape = RoundedCornerShape(12.dp),
    content: @Composable RowScope.() -> Unit
) {

    val animatedIconTint by animateColorAsState(
        targetValue = if (isEnabled) iconTint else Theme.colors.onPrimaryHint
    )
    val elementsArrangement =
        if (icon != null || isLoading) Arrangement.spacedBy(8.dp) else Arrangement.Center

    Row(
        modifier = modifier
            .height(height = 48.dp)
            .clip(shape)
            .background(
                color = backgroundColor
            )
            .clickable(
                enabled = isEnabled && !isLoading,
                onClick = onClick
            )
            .padding(vertical = verticalPadding, horizontal = horizontalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        content()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = elementsArrangement,
        ) {
            Crossfade(
                targetState = isLoading,
                modifier = Modifier
                    .animateContentSize()
                    .padding(
                        start = 4.dp,
                        end = 4.dp
                    ),
            ) { loading ->
                if (loading) {
                    AnimatedLoadingIndicator(
                        iconTint = iconTint,
                        size = 20.dp,
                    )
                } else if (icon != null) {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        tint = animatedIconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
