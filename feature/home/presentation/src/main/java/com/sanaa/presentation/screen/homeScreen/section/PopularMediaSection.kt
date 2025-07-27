package com.sanaa.presentation.screen.homeScreen.section

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
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
import kotlin.math.absoluteValue

val bottomGradientColor = Brush.verticalGradient(
    colors = listOf(
        Color(0x00000000), Color(0xB2000000), Color(0xCC000000)
    )
)

@SuppressLint("RestrictedApi", "ConfigurationScreenWidthHeight")
@Composable
fun PopularMediaSection(
    modifier: Modifier = Modifier,
    mediaItems: List<MediaItem>,
    onMediaClick: (MediaItem) -> Unit,
    onSaveIconClicked: (MediaItem) -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 10 })

    val focusedWidth = 188.dp
    val focusedHeight = 244.dp

    val unfocusedWidth = 132.dp
    val unfocusedHeight = 210.dp

    val horizontalContentPadding = ((screenWidth - focusedWidth) / 2)
    val pageSpacing = (-12).dp

    Column(
        modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NovixSectionHeader(title = stringResource(R.string.popular))
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = horizontalContentPadding),
            pageSpacing = pageSpacing,
            modifier = Modifier.fillMaxWidth(),
        ) { page ->
            val pageOffset =
                ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue.coerceIn(
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
                targetValue = if (page == pagerState.currentPage) 0f
                else if (page < pagerState.currentPage) -3f
                else 3f, label = "rotation"
            )

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
                        }, contentAlignment = Alignment.BottomCenter
                ) {
                    MediaPosterCard(
                        width = animatedWidth,
                        height = animatedHeight,
                        ratio = animatedWidth / animatedHeight,
                        onCardClick = {
                            onMediaClick(mediaItems[page])
                        },
                        topLeftContent = {
                            if (page == pagerState.currentPage) {
                                SaveIconChip(
                                    isSaved = false, onClick = {
                                        onSaveIconClicked(mediaItems[page])
                                    })
                            }
                        },
                        posterImage = {
                            RemoteBlurredHaramImageViewer(
                                imageUrl = mediaItems[page].imageUrl.orEmpty(),
                                modifier = Modifier,
                                blurRadius = 150,
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
                                        color = Color(0x99FFFFFF)
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
                            .height(
                                if (page == pagerState.currentPage) focusedHeight / 2 else unfocusedHeight
                            )
                            .align(Alignment.BottomCenter)
                            .background(brush = bottomGradientColor),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AnimatedVisibility(
                            visible = pagerState.currentPage == page,
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
                                    text = mediaItems[page].title,
                                    style = Theme.textStyle.label.medium.copy(color = Theme.colors.onPrimary)
                                )
                                RatingChip(
                                    rating = mediaItems[page].rating.toString()
                                )
                            }
                        }
                    }
                }

            }
        }
        NovixCarouselDots(
            totalDots = 10,
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

@PreviewLightDark
@Composable
fun PopularMediaSectionPreview(modifier: Modifier = Modifier) {
    NovixTheme(isSystemInDarkTheme()) {
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PopularMediaSection(
                mediaItems = demoMediaList,
                onMediaClick = {},
                onSaveIconClicked = {})
        }
    }

}