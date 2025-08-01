package com.sanaa.presentation.shared_component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.component.carousel.NovixCarouselDots
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.image_viewer.component.RemoteBlurredHaramImageViewer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged


val boxContainerGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF0D0608), Color(0x00000000))
)

@Composable
fun ImageSlider(
    images: List<String>,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { images.size })

    var isUserInteracting by remember { mutableStateOf(false) }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { isScrolling ->
                isUserInteracting = isScrolling
            }
    }

    LaunchedEffect(pagerState, images.size) {
        if (images.isNotEmpty()) {
            while (images.size > 1) {
                delay(4000)

                if (!isUserInteracting) {
                    val nextPage = if (pagerState.currentPage < images.size - 1) {
                        pagerState.currentPage + 1
                    } else 0
                    pagerState.animateScrollToPage(
                        page = nextPage,
                        animationSpec = tween(
                            durationMillis = 600,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            }
        }
    }

    Box(
        modifier = modifier
            .height(252.dp).clip(
                shape = RoundedCornerShape(
                    bottomStart = 12.dp, bottomEnd = 12.dp
                )
            )

    ) {
        if (images.isNotEmpty()) {
            HorizontalPager(
                state = pagerState, modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) { page ->
                RemoteBlurredHaramImageViewer(
                    imageUrl = images[page], contentDescription = contentDescription,
                    modifier = Modifier.fillMaxWidth(),
                    haramThreshold = 0.2f,
                    nonHaramThreshold = 0.7f,
                    placeholderContent = {
                        RemoteImagePlaceholder(Modifier.fillMaxSize())
                    },
                    errorContent = {
                        RemoteImagePlaceholder(Modifier.fillMaxSize())
                    },
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
            }
        } else {
            RemoteImagePlaceholder(Modifier.fillMaxSize())
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(boxContainerGradient)
        )
        AnimatedVisibility(
            visible = images.size > 1, modifier = Modifier.align(Alignment.TopCenter)
        ) {
            NovixCarouselDots(
                totalDots = images.size,
                selectedIndex = pagerState.currentPage,
                modifier = Modifier
                    .padding(top = 188.dp)
                    .background(
                        color = Theme.colors.iconBackgroundLow, shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        width = 1.dp, color = Theme.colors.stroke, shape = RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 4.dp, horizontal = 12.dp)
            )
        }
    }
}

@PreviewLightDark
@Composable
fun ImageSliderPreview() {
    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        ImageSlider(
            images = listOf(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEkpuNPSGlz986aRRvgDTy3QWCzIqwYv7lA8Yf7WXZn4MDkHUfnXMjXkrPtAjWsBaGl3I&usqp=CAU",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEkpuNPSGlz986aRRvgDTy3QWCzIqwYv7lA8Yf7WXZn4MDkHUfnXMjXkrPtAjWsBaGl3I&usqp=CAU",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEkpuNPSGlz986aRRvgDTy3QWCzIqwYv7lA8Yf7WXZn4MDkHUfnXMjXkrPtAjWsBaGl3I&usqp=CAU",
            ), contentDescription = "Squid game"
        )
    }
}