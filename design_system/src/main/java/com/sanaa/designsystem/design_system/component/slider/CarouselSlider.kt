package com.sanaa.designsystem.design_system.component.slider

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.carousel.CarouselDefaults
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarouselSlider(
    modifier: Modifier = Modifier,
    itemsCount: Int,
    preferredItemWidth: Dp,
    minSmallItemWidth: Dp = CarouselDefaults.MinSmallItemSize,
    maxSmallItemWidth: Dp = CarouselDefaults.MaxSmallItemSize,
    itemSpacing: Dp = 8.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    content: @Composable (index: Int,isFocused: Boolean) -> Unit,
) {

    val carouselState = rememberCarouselState { itemsCount }
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
        content(index,isFocused)    }
}