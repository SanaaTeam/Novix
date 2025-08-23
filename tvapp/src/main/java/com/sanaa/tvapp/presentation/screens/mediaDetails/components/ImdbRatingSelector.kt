package com.sanaa.tvapp.presentation.screens.mediaDetails.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import kotlin.math.roundToInt
import com.sanaa.designsystem.R as designSystemResource

@Composable
fun ImdbRatingSelector(
    currentRating: Int,
    modifier: Modifier = Modifier,
    onRatingChanged: (Int) -> Unit = {},
    maxRating: Int = 5,
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
                    .wrapContentWidth()
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
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
                horizontalArrangement = Arrangement.spacedBy(12.dp),
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
            painter = painterResource(id = designSystemResource.drawable.outlined_star),
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            colorFilter = ColorFilter.tint(Theme.colors.statusColors.yellowAccent)
        )

        AnimatedVisibility(isSelected) {
            Image(
                painter = painterResource(id = designSystemResource.drawable.star),
                contentDescription = "Rating $rating",
                modifier = Modifier.size(28.dp),
                colorFilter = ColorFilter.tint(Theme.colors.statusColors.yellowAccent)
            )
        }
    }
}


@PreviewLightDark
@Composable
fun IMDbRatingSelectorPreview() {
    NovixTheme(isSystemInDarkTheme()) {
        var currentRating by remember { mutableIntStateOf(0) }
        Column(modifier = Modifier.padding(16.dp)) {
            ImdbRatingSelector(
                currentRating = currentRating,
                onRatingChanged = { newRating ->
                    currentRating = newRating
                }
            )
        }
    }
}