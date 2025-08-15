package com.sanaa.presentation.modifiers

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp

fun Modifier.fillWidthOfParent(parentPadding: Dp) = layout { measurable, constraints ->
    val newMaxWidth = constraints.maxWidth + 2 * parentPadding.roundToPx()

    val newConstraints = constraints.copy(maxWidth = newMaxWidth)

    val placeable = measurable.measure(newConstraints)

    layout(constraints.maxWidth, placeable.height) {
        placeable.place(-parentPadding.roundToPx(), 0)
    }
}

fun Modifier.shimmerEffect(
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
): Modifier = composed {
    val shimmerColors = listOf(
        Color.Transparent,
        Color.Transparent,
        Color.White.copy(alpha = 0.1f),
        Color.White.copy(alpha = 0.2f),
        Color.White.copy(alpha = 0.3f),
        Color.White.copy(alpha = 0.2f),
        Color.White.copy(alpha = 0.1f),
        Color.Transparent,
        Color.Transparent,
    )

    val transition = rememberInfiniteTransition()


    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Shimmer loading animation",
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
        end = Offset(x = translateAnimation.value, y = angleOfAxisY),
    )

    background(brush)
}