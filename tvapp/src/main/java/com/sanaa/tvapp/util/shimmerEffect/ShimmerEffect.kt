package com.sanaa.tvapp.util.shimmerEffect

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sanaa.tvapp.util.modifier.shimmerEffect


@Composable
fun PlaceholderWithShimmerEffect(
    modifier: Modifier = Modifier,
    width: Dp = Dp.Unspecified,
    height: Dp = Dp.Unspecified,
    cornerRadius: Dp = 10.dp,
    borderColor: Color = Color(0x14FFFFFF),
    borderWidth: Dp = 1.dp,
) {
    Box(
        modifier = modifier
            .then(if (width == Dp.Unspecified) Modifier.fillMaxWidth() else Modifier.width(width))
            .then(if (height == Dp.Unspecified) Modifier.fillMaxHeight() else Modifier.height(height))
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        if (isSystemInDarkTheme()) Color(0x3DFFFFFF) else Color(0x3D000000),
                        Color(0x00FFFFFF),
                        if (isSystemInDarkTheme()) Color(0x14000000) else Color(0x14000008),
                    ),
                    start = Offset.Zero,
                    end = Offset.Infinite,
                )
            )
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .shimmerEffect(durationMillis = 2000)
    )
}