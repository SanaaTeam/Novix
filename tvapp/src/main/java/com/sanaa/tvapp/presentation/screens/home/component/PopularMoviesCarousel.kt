package com.sanaa.tvapp.presentation.screens.home.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Carousel
import androidx.tv.material3.CarouselState
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.ShapeDefaults
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.tvapp.R
import com.sanaa.tvapp.presentation.api.LocalSafeContentThreshold
import com.sanaa.tvapp.state.MediaItemUiState
import com.sanaa.tvapp.util.modifier.handleDPadKeyEvents
import com.sanaa.tvapp.util.shimmerEffect.PlaceholderWithShimmerEffect
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalTvMaterial3Api::class)
val CarouselSaver = Saver<CarouselState, Int>(
    save = { it.activeItemIndex },
    restore = { CarouselState(it) }
)

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun PopularMoviesCarousel(
    mediaItemUiStates: List<MediaItemUiState>,
    modifier: Modifier = Modifier,
    onShowDetails: (movie: MediaItemUiState) -> Unit,
) {
    val carouselState = rememberSaveable(saver = CarouselSaver) { CarouselState(0) }
    var isFocused by remember { mutableStateOf(false) }

    Carousel(
        modifier = modifier
            .height(316.dp)
            .border(
                width = if (isFocused) 3.dp else 2.dp,
                color = if (isFocused) Theme.colors.primary else Theme.colors.stroke,
                shape = ShapeDefaults.Medium,
            )
            .clip(RoundedCornerShape(12.dp))
            .onFocusChanged {
                isFocused = it.hasFocus
            }
            .handleDPadKeyEvents(onEnter = {
                if (mediaItemUiStates.isNotEmpty()) {
                    onShowDetails(mediaItemUiStates[carouselState.activeItemIndex])
                }
            }),
        itemCount = mediaItemUiStates.size,
        carouselState = carouselState,
        carouselIndicator = {
            CarouselDots(
                totalDots = mediaItemUiStates.size,
                selectedIndex = carouselState.activeItemIndex
            )
        },
        content = { index ->
            val mediaItem = mediaItemUiStates[index]
            CarouselItemBackground(movie = mediaItem, modifier = Modifier.fillMaxSize())
            CarouselItemForeground()
            MediaInfo(mediaItemUiState = mediaItem, isFocused = isFocused)
        },
        autoScrollDurationMillis = 5.seconds.inWholeMilliseconds,
        contentTransformStartToEnd = fadeIn(tween(durationMillis = 500))
            .togetherWith(fadeOut(tween(durationMillis = 500))),
        contentTransformEndToStart = fadeIn(tween(durationMillis = 500))
            .togetherWith(fadeOut(tween(durationMillis = 500))),
    )
}

@Composable
fun MediaInfo(
    mediaItemUiState: MediaItemUiState,
    isFocused: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))


        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = mediaItemUiState.title,
            color = Theme.colors.title,
            style = Theme.textStyle.title.large
        )

        if (mediaItemUiState.overview.isNotEmpty()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(top = 12.dp),
                text = mediaItemUiState.overview,
                color = Theme.colors.body,
                style = Theme.textStyle.label.medium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        }

        AnimatedVisibility(isFocused) { SeeDetailsButton() }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun CarouselItemForeground() {
    val brushColor = listOf(
        Color.Transparent,
        Theme.colors.surfaceHigh.copy(alpha = 0.3f),
        Theme.colors.surfaceHigh.copy(alpha = 0.8f)
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.radialGradient(
                colors = brushColor,
                center = Offset(size.width, size.height - 600.dp.toPx()),
                radius = size.maxDimension - size.minDimension
            ),
            size = size
        )
    }
}

@Composable
private fun CarouselItemBackground(movie: MediaItemUiState, modifier: Modifier = Modifier) {
    movie.imageUrl?.let {
        RemoteBlurredSensitiveImage(
            isBlurEnabled = LocalSafeContentThreshold.current != 0f,
            imageUrl = movie.imageUrl,
            contentDescription = movie.title,
            modifier = modifier
                .drawWithContent {
                    drawContent()
                    drawRect(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.5f)
                            )
                        )
                    )
                },
        ) {
            OnBlurContent(
                hintText = stringResource(com.sanaa.designsystem.R.string.unsuitable_image),
                textStyle = Theme.textStyle.body.small.copy(
                    color = Color(0x99FFFFFF)
                ),
                iconSize = 24.dp,
                icon = painterResource(com.sanaa.designsystem.R.drawable.icon_eye_slash),
            )
        }
    }
}

@Composable
fun BoxScope.CarouselDots(
    totalDots: Int,
    selectedIndex: Int,
) {
    Row(
        modifier = Modifier
            .padding(32.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Theme.colors.surface.copy(alpha = 0.2f))
            .padding(8.dp)
            .align(Alignment.BottomEnd),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalDots) { index ->
            val isSelected = index == selectedIndex
            val animatedBackgroundColor by animateColorAsState(
                targetValue = if (isSelected) Theme.colors.primary else Theme.colors.stroke,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessHigh
                ),
            )

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color = animatedBackgroundColor)
            )
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
private fun SeeDetailsButton() {
    Row(
        modifier = Modifier
            .padding(top = 16.dp)
            .focusable(false)
            .background(Theme.colors.primary, RoundedCornerShape(50.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
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