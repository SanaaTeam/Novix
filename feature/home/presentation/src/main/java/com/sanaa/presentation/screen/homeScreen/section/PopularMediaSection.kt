package com.sanaa.presentation.screen.homeScreen.section

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.sanaa.designsystem.design_system.component.carousel.NovixCarouselDots
import com.sanaa.designsystem.design_system.component.section_header.NovixSectionHeader
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.components.cards.MediaPosterCard
import com.sanaa.presentation.components.chips.MediaRatingChip
import com.sanaa.presentation.components.chips.SaveIconChip
import kotlin.math.absoluteValue

@SuppressLint("RestrictedApi")
@Composable
fun PopularMediaSection(modifier: Modifier = Modifier) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 10 })

    val focusedWidth = 188.dp
    val focusedHeight = 244.dp

    val unfocusedWidth = 132.dp
    val unfocusedHeight = 210.dp

    val horizontalContentPadding = ((screenWidth - focusedWidth) / 2)
    val pageSpacing = -12.dp

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NovixSectionHeader(title = "Popular")
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = horizontalContentPadding),
            pageSpacing = pageSpacing,
            modifier = Modifier.fillMaxWidth(),
        )
        { page ->

            val pageOffset =
                ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
                    .coerceIn(0f, 1f)

            // Animate width & height instead of scale
            val animatedWidth by animateDpAsState(
                targetValue = lerp(unfocusedWidth, focusedWidth, 1f - pageOffset),
                label = "animatedWidth"
            )
            val animatedHeight by animateDpAsState(
                targetValue = lerp(unfocusedHeight, focusedHeight, 1f - pageOffset),
                label = "animatedHeight"
            )

            val rotation by animateFloatAsState(
                targetValue =
                    if (page == pagerState.currentPage) 0f
                    else if (page < pagerState.currentPage) -3f
                    else 3f,
                label = "rotation"
            )

            Box(
                modifier = Modifier
                    .width(focusedWidth)
                    .height(focusedHeight),
                contentAlignment = Alignment.BottomCenter
            ){
                Box(
                    modifier = Modifier
                        .width(animatedWidth)
                        .height(animatedHeight)
                        .graphicsLayer {
                            rotationZ = rotation
                        },
                    contentAlignment = Alignment.BottomCenter
                )
                {
                    MediaPosterCard(
                        width = animatedWidth,
                        height = animatedHeight,
                        ratio = animatedWidth / animatedHeight,
                        onCardClick = {},
                        topLeftContent = {
                            SaveIconChip(
                                isSaved = false,
                                onClick = {}
                            )
                        },
                        posterImage = {
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                painter = painterResource(com.sanaa.presentation.R.drawable.freman),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "Media Title",
                            style = Theme.textStyle.label.medium,
                            color = Theme.colors.title,
                        )
                        MediaRatingChip(
                            rating = "9.9"
                        )
                    }
                }
            }


        }
        NovixCarouselDots(
            totalDots = 10,
            selectedIndex = pagerState.currentPage,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onDotClick = {}
        )
    }
}

@PreviewLightDark
@Composable
fun PopularMediaSectionPreview(modifier: Modifier = Modifier) {
    NovixTheme(isSystemInDarkTheme()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PopularMediaSection()
        }
    }

}