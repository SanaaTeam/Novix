package com.sanaa.presentation.filter_bottomsheet.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import kotlin.math.roundToInt

@Composable
fun IMDbRatingSelector(
    title: String,
    currentRating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxRating: Int = 10
) {
    var rowSize by remember { mutableStateOf(IntSize.Zero) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = Theme.textStyle.title.small,
            color = Theme.colors.title
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .onSizeChanged { rowSize = it }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {  },
                        onHorizontalDrag = { change, _ ->
                            change.consume()
                            val newRating = (change.position.x / rowSize.width * maxRating)
                                .coerceIn(0f, maxRating.toFloat())
                                .roundToInt()
                                .coerceIn(0, maxRating)

                            if (newRating != currentRating) {
                                onRatingChanged(newRating)
                            }
                        }
                    )
                },
            horizontalArrangement = Arrangement.SpaceBetween
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

@Composable
private fun Star(
    rating: Int,
    currentRating: Int,
    onRatingChanged: (Int) -> Unit
) {
    val isSelected = rating <= currentRating

    val filledStarColor by animateColorAsState(
        targetValue = if (isSelected) Theme.colors.statusColors.yellowAccent else Color.Transparent,
        label = "starFillColorAnimation",
        animationSpec = tween(durationMillis = 400)
    )

    Box(
        modifier = Modifier
            .size(28.dp)
            .pointerInput(Unit) {
                detectTapGestures {
                    onRatingChanged(rating)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.outlined_star),
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            colorFilter = ColorFilter.tint(Theme.colors.statusColors.yellowAccent)
        )

        Image(
            painter = painterResource(id = R.drawable.star),
            contentDescription = "Rating $rating",
            modifier = Modifier.size(28.dp),
            colorFilter = ColorFilter.tint(filledStarColor)
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun IMDbRatingSelectorPreview() {
    var currentRating by remember { mutableIntStateOf(0) }
        Column(modifier = Modifier.padding(16.dp)) {
            IMDbRatingSelector(
                title = "IMDb rating",
                currentRating = currentRating,
                onRatingChanged = { newRating ->
                    currentRating = newRating
                }
            )
        }
}
