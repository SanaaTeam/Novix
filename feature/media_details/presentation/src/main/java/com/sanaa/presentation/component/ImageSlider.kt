package com.sanaa.presentation.component

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sanaa.designsystem.design_system.component.carousel.NovixCarouselDots
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import kotlinx.coroutines.delay

@Composable
fun ImageSlider(
    images: List<String>,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onDotClick: (Int) -> Unit = {},
) {
    val pagerState = rememberPagerState(pageCount = { images.size })

    LaunchedEffect(pagerState) {
        while (images.size > 1) {
            delay(4000)
            val nextPage = if (pagerState.currentPage < images.size - 1) {
                pagerState.currentPage + 1
            } else 0
            pagerState.animateScrollToPage(
                page = nextPage, animationSpec = tween(
                    durationMillis = 600, easing = FastOutSlowInEasing
                )
            )
        }
    }

    Box(modifier = modifier.height(252.dp)) {
        HorizontalPager(
            state = pagerState, modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = contentDescription,
//                placeholder = TODO,
//                error = TODO,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        AnimatedVisibility(
            visible = images.size > 1, modifier = Modifier.align(Alignment.TopCenter)
        ) {
            NovixCarouselDots(
                onDotClick = onDotClick,
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
            ),
            contentDescription = "Squid game"
        )
    }
}