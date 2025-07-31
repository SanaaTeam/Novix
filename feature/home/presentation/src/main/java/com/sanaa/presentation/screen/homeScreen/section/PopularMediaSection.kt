package com.sanaa.presentation.screen.homeScreen.section

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.component.carousel.NovixCarouselDots
import com.sanaa.designsystem.design_system.component.section_header.NovixSectionHeader
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.home.presentation.R
import com.sanaa.image_viewer.component.RemoteBlurredHaramImageViewer
import com.sanaa.presentation.components.RemoteImagePlaceholder
import com.sanaa.presentation.components.cards.MediaPosterCard
import com.sanaa.presentation.components.chips.SaveIconChip
import com.sanaa.presentation.state.MediaItem
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.absoluteValue

val overLayGradientColor = Brush.verticalGradient(
    colors = listOf(
        Color(0x00000000), Color(0xB2000000), Color(0xCC000000)
    )
)

@Composable
fun PopularMediaSection(
    modifier: Modifier = Modifier,
    mediaItems: List<MediaItem>,
    onMediaClick: (MediaItem) -> Unit,
    onSaveIconClicked: (MediaItem) -> Unit
) {

    val pagerState = rememberPagerState(pageCount = { mediaItems.count() })
    Column(
        modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NovixSectionHeader(title = stringResource(R.string.popular))
        PopularMediaSectionPager(
            state = pagerState,
            mediaItems = mediaItems,
            onMediaClick = onMediaClick,
            onSaveIconClicked = onSaveIconClicked
        )
        NovixCarouselDots(
            totalDots = mediaItems.size,
            selectedIndex = pagerState.currentPage,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onDotClick = {})
    }
}

@Composable
private fun RatingChip(
    rating: String, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.icon_star),
            contentDescription = null,
            modifier = Modifier.size(12.dp),
            colorFilter = ColorFilter.tint(Theme.colors.statusColors.yellowAccent)
        )
        BasicText(
            text = rating, style = Theme.textStyle.label.small.copy(color = Theme.colors.onPrimary)
        )

    }
}


@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
private fun PopularMediaSectionPager(
    state: PagerState,
    mediaItems: List<MediaItem>,
    onMediaClick: (MediaItem) -> Unit,
    onSaveIconClicked: (MediaItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val focusedWidth = 188.dp
    val focusedHeight = 244.dp

    val unfocusedWidth = 132.dp
    val unfocusedHeight = 210.dp

    val horizontalContentPadding = ((screenWidth - focusedWidth) / 2)
    val pageSpacing = (-12).dp
    val layoutDirection = LocalLayoutDirection.current
    val isRtl = layoutDirection == LayoutDirection.Rtl

    HorizontalPager(
        state = state,
        contentPadding = PaddingValues(horizontal = horizontalContentPadding),
        pageSpacing = pageSpacing,
        modifier = modifier.fillMaxWidth(),
    ) { page ->
        val pageOffset =
            ((state.currentPage - page) + state.currentPageOffsetFraction).absoluteValue.coerceIn(
                0f, 1f
            )

        val animatedWidth by animateDpAsState(
            targetValue = lerp(unfocusedWidth, focusedWidth, 1f - pageOffset),
            label = "animatedWidth"
        )
        val animatedHeight by animateDpAsState(
            targetValue = lerp(unfocusedHeight, focusedHeight, 1f - pageOffset),
            label = "animatedHeight"
        )


        val rotation by animateFloatAsState(
            targetValue = when {
                page == state.currentPage -> 0f
                page < state.currentPage -> if (isRtl) 3f else -3f
                else -> if (isRtl) -3f else 3f
            },
            label = "rotation"
        )

        val overLayAnimatedBackgroundHeight by animateDpAsState(
            targetValue = if (page == state.currentPage) focusedHeight / 2 else unfocusedHeight,
            label = "overLayAnimatedBackgroundHeight",

            )

        LaunchedEffect(Unit) {
            if (mediaItems.size > 1) {
                while (currentCoroutineContext().isActive) {
                    delay(4000)
                    val nextPage = if (state.currentPage < mediaItems.size - 1) {
                        state.currentPage + 1
                    } else 0
                    state.animateScroll(nextPage)
                }
            }
        }
        val item = mediaItems[page]
        Box(
            modifier = Modifier
                .width(focusedWidth)
                .height(focusedHeight),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .width(animatedWidth)
                    .height(animatedHeight)
                    .graphicsLayer {
                        rotationZ = rotation
                        clip = true
                        shape = RoundedCornerShape(12.dp)
                    }, contentAlignment = Alignment.BottomCenter
            ) {
                MediaPosterCard(
                    width = animatedWidth,
                    height = animatedHeight,
                    ratio = animatedWidth / animatedHeight,
                    onCardClick = {
                        onMediaClick(item)
                    },
                    topLeftContent = {
                        if (page == state.currentPage) {
                            SaveIconChip(
                                isSaved = false, onClick = {
                                    onSaveIconClicked(item)
                                })
                        }
                    },
                    posterImage = {
                        RemoteBlurredHaramImageViewer(
                            imageUrl = item.imageUrl.orEmpty(),
                            modifier = Modifier,
                            haramThreshold = 0.2f,
                            nonHaramThreshold = 0.7f,
                            placeholderContent = {
                                RemoteImagePlaceholder(Modifier.fillMaxSize())
                            },
                            errorContent = {
                                RemoteImagePlaceholder(Modifier.fillMaxSize())
                            },
                            contentDescription = mediaItems[page].title,
                        ) {
                            OnBlurContent(
                                hintText = stringResource(R.string.unsuitable_image),
                                textStyle = Theme.textStyle.body.small.copy(
                                    color = Color(
                                        0x99FFFFFF
                                    )
                                ),
                                iconSize = 24.dp,
                                icon = painterResource(com.sanaa.designsystem.R.drawable.icon_eye_slash),
                            )
                        }
                    })
                Column(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 12.dp, bottomEnd = 12.dp
                            )
                        )
                        .fillMaxWidth()
                        .height(overLayAnimatedBackgroundHeight)
                        .align(Alignment.BottomCenter)
                        .background(brush = overLayGradientColor),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedVisibility(
                        visible = state.currentPage == page,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            BasicText(
                                text = item.title,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 2,
                                style = Theme.textStyle.label.medium.copy(color = Theme.colors.onPrimary)
                            )
                            RatingChip(
                                rating = item.rating.toString()
                            )
                        }
                    }
                }
            }

        }
    }
}

suspend fun PagerState.animateScroll(page: Int) {
    animateScrollToPage(
        page = page,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        )
    )
}

@PreviewLightDark
@Composable
fun PopularMediaSectionPreview(modifier: Modifier = Modifier) {
    NovixTheme(isSystemInDarkTheme()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PopularMediaSection(
                mediaItems = demoMediaList,
                onMediaClick = {},
                onSaveIconClicked = {})
        }
    }

}