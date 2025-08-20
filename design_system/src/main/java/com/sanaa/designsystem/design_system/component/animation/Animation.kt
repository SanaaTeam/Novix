package com.sanaa.designsystem.design_system.component.animation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith

val FadeInOut150 = fadeIn(
    animationSpec = tween(durationMillis = 150, delayMillis = 150)
) togetherWith fadeOut(
    animationSpec = tween(durationMillis = 150)
)
val FadeSlideInVertically = fadeIn(
    animationSpec = tween(300)
) + slideInVertically(
    initialOffsetY = { it }
)

val FadeSlideOutVertically = fadeOut(
    animationSpec = tween(200)
) + slideOutVertically(
    targetOffsetY = { it }
)
val FadeSlideInVerticallyFromTop = fadeIn(
    animationSpec = tween(300)
) + slideInVertically(
    initialOffsetY = { -it }
)

val FadeSlideOutVerticallyToTop = fadeOut(
    animationSpec = tween(200)
) + slideOutVertically(
    targetOffsetY = { -it }
)