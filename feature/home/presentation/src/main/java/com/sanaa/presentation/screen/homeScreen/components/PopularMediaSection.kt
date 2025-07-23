package com.sanaa.presentation.screen.homeScreen.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.fontscaling.MathUtils.lerp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.section_header.NovixSectionHeader
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.components.cards.MediaPosterCard
import com.sanaa.presentation.components.chips.MediaRatingChip
import com.sanaa.presentation.components.chips.SaveIconChip
import kotlin.math.absoluteValue

@Composable
fun PopularMediaSection(modifier: Modifier = Modifier) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 10 })

    // Card base size from MediaPosterCard (fixed at 158.dp width)
    val cardWidth = 158.dp
    val cardHeight = 210.dp

    val pageSpacing = 0.dp

    // Ensure focused item is truly centered, and spacing looks accurate
    val horizontalContentPadding = ((screenWidth - cardWidth) / 2)

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
        ) { page ->

            val pageOffset =
                ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

            val scale by animateFloatAsState(
                targetValue = lerp(0.84f, 1f, 1f - pageOffset.coerceIn(0f, 1f)),
                label = "scale"
            )
            val rotation by animateFloatAsState(
                targetValue =
                    if (page == pagerState.currentPage) {
                        0f
                    } else if (page < pagerState.currentPage) {
                        -3f
                    } else {
                        3f
                    },
                label = "rotate"
            )


            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        rotationZ = rotation
                    }
            )
            {
                MediaPosterCard(
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
                            painter = painterResource(R.drawable.icon_placeholder_light),
                            contentDescription = null
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