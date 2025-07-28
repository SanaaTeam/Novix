package com.sanaa.presentation.components.shimmerEffect

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.modifiers.fillWidthOfParent
import com.sanaa.presentation.modifiers.shimmerEffect
import kotlin.math.absoluteValue

@PreviewLightDark
@Composable
private fun Preview() {
    NovixTheme(isSystemInDarkTheme()) {

        NovixScaffold {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                PopularMediaSectionPlaceholder()

                MediaSliderSectionPlaceholder()
            }
        }
    }
}


@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun PopularMediaSectionPlaceholder(
    modifier: Modifier = Modifier,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val pagerState = rememberPagerState(initialPage = 1, pageCount = { 3 })

    val focusedWidth = 188.dp
    val focusedHeight = 244.dp

    val unfocusedWidth = 132.dp
    val unfocusedHeight = 210.dp

    val horizontalContentPadding = ((screenWidth - focusedWidth) / 2)
    val pageSpacing = (-12).dp

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PlaceholderWithShimmerEffect(
            width = 77.dp,
            height = 30.dp,
            cornerRadius = 8.dp,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            borderColor = Color.Transparent,
        )
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = horizontalContentPadding),
            pageSpacing = pageSpacing,
            modifier = Modifier.fillMaxWidth(),
            userScrollEnabled = false
        )
        { page ->

            val pageOffset =
                ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
                    .coerceIn(0f, 1f)

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
                    .height(focusedHeight)
                    .clip(RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.BottomCenter
            ) {
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
                    PlaceholderWithShimmerEffect(
                        width = animatedWidth,
                        height = animatedHeight,
                    )
                    if (page == pagerState.currentPage) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            PlaceholderWithShimmerEffect(
                                width = 172.dp, height = 44.dp, cornerRadius = 8.dp,
                                borderColor = Color.Transparent
                            )
                            PlaceholderWithShimmerEffect(
                                width = 43.dp, height = 18.dp, cornerRadius = 4.dp,
                                borderColor = Color.Transparent
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x52000000)),
                        )
                    }
                }
            }

        }
    }
}


@Composable
fun MediaSliderSectionPlaceholder(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            PlaceholderWithShimmerEffect(
                width = 166.dp,
                height = 30.dp,
                cornerRadius = 8.dp,
                borderColor = Color.Transparent,
            )
            PlaceholderWithShimmerEffect(
                modifier = Modifier.padding(start = 16.dp),
                width = 46.dp,
                height = 24.dp,
                cornerRadius = 8.dp,
                borderColor = Color.Transparent,
            )

        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            userScrollEnabled = false
        ) {

            item {
                PlaceholderWithShimmerEffect()
            }

            items(9) {
                PlaceholderWithShimmerEffect(width = 74.dp)
            }
        }
    }
}

fun LazyGridScope.upcomingSectionPlaceholder(modifier: Modifier = Modifier) {

    item(span = { GridItemSpan(maxLineSpan) }){
        PlaceholderWithShimmerEffect(
            width = 166.dp,
            height = 30.dp,
            cornerRadius = 8.dp,
            borderColor = Color.Transparent
        )
    }
    item(span = { GridItemSpan(maxLineSpan) }){
        LazyRow(
            modifier = modifier
                .fillMaxWidth().fillWidthOfParent(16.dp)
                .height(62.dp),
            contentPadding = PaddingValues( vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            userScrollEnabled = false
        ) {
            items(10) {
                PlaceholderWithShimmerEffect(
                    width = 64.dp,
                    height = 38.dp,
                    cornerRadius = 8.dp,
                    borderColor = Color.Transparent
                )
            }
        }
    }

    items(10) {
        PlaceholderWithShimmerEffect(
            modifier = Modifier.padding(bottom = 12.dp)
        )
    }
}


@Composable
fun PlaceholderWithShimmerEffect(
    modifier: Modifier = Modifier,
    width: Dp = 158.dp,
    height: Dp = 210.dp,
    cornerRadius: Dp = 10.dp,
    borderColor: Color = Color(0x14FFFFFF),
    borderWidth: Dp = 1.dp
) {
    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        if (isSystemInDarkTheme()) Color(0x3DFFFFFF) else Color(0x3D000000),
                        Color(0x00FFFFFF),
                        if (isSystemInDarkTheme()) Color(0x14000000) else Color(0x14000008),
                    ),
                    start = Offset.Zero,
                    end = Offset.Infinite,
                )
            )
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .shimmerEffect(durationMillis = 2000)
    )
}