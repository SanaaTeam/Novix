package com.sanaa.tvapp.presentation.screens.home.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredHaramImageViewer
import com.sanaa.tvapp.R
import com.sanaa.tvapp.presentation.screens.home.FeaturedCarouselState
import com.sanaa.tvapp.util.shimmerEffect.PlaceholderWithShimmerEffect

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun FeaturedCarousel(
    featuredCarouselState: FeaturedCarouselState,
    onSeeDetails: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val brushColor = listOf(Theme.colors.surfaceHigh.copy(0.0f), Theme.colors.surfaceHigh)

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        interactionSource = interactionSource,
        onClick = {},
        colors = CardDefaults.colors(),
        border = CardDefaults.border(
            border = Border.None,
            focusedBorder = Border(
                border = BorderStroke(
                    width = 3.dp,
                    color = Theme.colors.primary,
                ),
                shape = RoundedCornerShape(12.dp),
            ),
        ),
        scale = CardDefaults.scale(focusedScale = 1.01f),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            RemoteBlurredHaramImageViewer(
                modifier = Modifier.height(316.dp),
                imageUrl = featuredCarouselState.imageUrl,
                contentDescription = featuredCarouselState.movieDescription,
            )

            Canvas(modifier = Modifier.matchParentSize()) {
                drawRect(
                    brush = Brush.radialGradient(
                        colors = brushColor,
                        center = Offset(size.width, size.height - 300.dp.toPx()),
                        radius = size.maxDimension - size.minDimension
                    ),
                    size = size
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(32.dp)
            ) {
                Text(
                    modifier = Modifier,
                    text = featuredCarouselState.movieInfo,
                    color = Theme.colors.body,
                    style = Theme.textStyle.label.medium
                )

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = featuredCarouselState.movieTitle,
                    color = Theme.colors.title,
                    style = Theme.textStyle.title.large
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(top = 12.dp),
                    text = featuredCarouselState.movieDescription,
                    color = Theme.colors.body,
                    style = Theme.textStyle.label.medium
                )

                AnimatedVisibility(isFocused) {
                    Button(onSeeDetails)
                }
            }
        }
    }
}

@Composable
fun FeaturedCarouselShimmer() {
    PlaceholderWithShimmerEffect(
        modifier = Modifier,
        height = 316.dp,
    )
}


@Composable
@OptIn(ExperimentalTvMaterial3Api::class)
private fun Button(onSeeDetails: () -> Unit) {
    Button(
        modifier = Modifier
            .padding(top = 24.dp)
            .focusable(false),
        colors = ButtonDefaults.colors(
            containerColor = Theme.colors.primary
        ),
        onClick = { onSeeDetails() }
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(R.drawable.play),
            tint = Theme.colors.onPrimary,
            contentDescription = stringResource(R.string.see_detail)
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = stringResource(R.string.see_detail),
            color = Theme.colors.onPrimary,
            style = Theme.textStyle.label.large
        )
    }
}

