package com.sanaa.tvapp.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.Theme
import kotlin.math.roundToInt

@Composable
fun ImdbRatingSelector(
    currentRating: Int,
    modifier: Modifier = Modifier,
    onRatingChanged: (Int) -> Unit = {},
    maxRating: Int = 10,
) {
    var rowSize by remember { mutableStateOf(IntSize.Zero) }
    val layoutDirection = LocalLayoutDirection.current

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .onSizeChanged { rowSize = it },
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragEnd = { },
                            onHorizontalDrag = { change, _ ->
                                change.consume()
                                val newRating = when (layoutDirection) {
                                    LayoutDirection.Ltr -> {
                                        (change.position.x / rowSize.width * maxRating)
                                    }

                                    LayoutDirection.Rtl -> {
                                        ((rowSize.width - change.position.x) / rowSize.width * maxRating)
                                    }
                                }.coerceIn(0f, maxRating.toFloat())
                                    .roundToInt()
                                    .coerceIn(0, maxRating)

                                if (newRating != currentRating) {
                                    onRatingChanged(newRating)
                                }
                            }
                        )
                    },
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (rating in 1..maxRating) {
                    Star(
                        rating = rating,
                        currentRating = currentRating,
                        onRatingChanged = onRatingChanged
                    )
                }
            }
        }
    }
}

@Composable
private fun Star(
    rating: Int,
    currentRating: Int,
    onRatingChanged: (Int) -> Unit,
) {
    val isSelected = rating <= currentRating
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.2f else 1f,
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        ),
        label = "StarFocusScale"
    )
    Box(
        modifier = Modifier
            .size(36.dp)
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onRatingChanged(rating) }
            ),
        contentAlignment = Alignment.Center
    ) {
        val tint = Theme.colors.statusColors.yellowAccent

        Box(
            modifier = Modifier
                .size(32.dp)
                .scale(scale),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(
                    id = if (isSelected) R.drawable.star else R.drawable.outlined_star
                ),
                contentDescription = "Rating $rating",
                tint = tint,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}