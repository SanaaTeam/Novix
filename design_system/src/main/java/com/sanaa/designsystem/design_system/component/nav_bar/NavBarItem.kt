package com.sanaa.designsystem.design_system.component.nav_bar

import android.graphics.BlurMaskFilter
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun NavBarItem(
    isSelected: Boolean,
    onClick: () -> Unit,
    iconRes: Int,
    selectedIconRes: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(56.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ), contentAlignment = Alignment.BottomCenter
    ) {
        Crossfade(
            targetState = isSelected, animationSpec = tween(50, easing = FastOutSlowInEasing)
        ) { selected ->
            if (selected) {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .height(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        OvalShapeBlur(
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(Theme.colors.primary)
                        )

                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(42.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = selectedIconRes),
                            contentDescription = null,
                            tint = Theme.colors.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = Theme.colors.hint,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun OvalShapeBlur(
    modifier: Modifier = Modifier,
    color: Color = Theme.colors.primary.copy(alpha = 0.4f),
    blurRadius: Float = 150f
) {
    Canvas(
        modifier = modifier
    ) {
        val paint = android.graphics.Paint().apply {
            this.color = color.toArgb()
            maskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL)
        }
        drawIntoCanvas {
            it.nativeCanvas.drawOval(
                0f, 0f, size.width, size.height, paint
            )
        }
    }
}


@Preview
@Composable
private fun PreviewNovixNavBarItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        NavBarItem(
            isSelected = true,
            onClick = {},
            iconRes = R.drawable.icon_home,
            modifier = Modifier.width(60.dp),
            selectedIconRes = R.drawable.icon_home_selected
        )
    }
}