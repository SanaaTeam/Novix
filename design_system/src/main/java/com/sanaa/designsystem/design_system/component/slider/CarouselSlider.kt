package com.sanaa.designsystem.design_system.component.slider

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.carousel.CarouselDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> CarouselSlider(
    modifier: Modifier = Modifier,
    items: List<T>,
    preferredItemWidth: Dp,
    minSmallItemWidth: Dp = CarouselDefaults.MinSmallItemSize,
    maxSmallItemWidth: Dp = CarouselDefaults.MaxSmallItemSize,
    itemSpacing: Dp = 8.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    maskClipShape: Shape = RoundedCornerShape(12.dp),
    content: @Composable (item: T, isFocused: Boolean) -> Unit,
) {

    val carouselState = rememberCarouselState { items.count() }
    HorizontalMultiBrowseCarousel(
        state = carouselState,
        preferredItemWidth = preferredItemWidth,
        minSmallItemWidth = minSmallItemWidth,
        maxSmallItemWidth = maxSmallItemWidth,
        itemSpacing = itemSpacing,
        contentPadding = contentPadding,
        modifier = modifier.fillMaxWidth()
    ) { index ->
        val isFocused = carouselState.currentItem == index
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .maskClip(maskClipShape),
            contentAlignment = Alignment.Center
        ) {
            content(items[index], isFocused)
        }
    }
}