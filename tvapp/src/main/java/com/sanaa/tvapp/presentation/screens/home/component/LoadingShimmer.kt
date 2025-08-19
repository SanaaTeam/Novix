package com.sanaa.tvapp.presentation.screens.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sanaa.tvapp.util.shimmerEffect.PlaceholderWithShimmerEffect


@Composable
fun HomeScreenLoading(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        FeaturedCarouselShimmer()

        repeat(3) {
            TitleShimmer()
            ImageListShimmer()
        }
    }
}

@Composable
fun ImageListShimmer() {
    Row {
        repeat(10) {
            PlaceholderWithShimmerEffect(
                modifier = Modifier.padding(end = 20.dp, bottom = 24.dp),
                width = 153.dp,
                height = 231.dp,
                borderColor = Color.Transparent,
            )
        }
    }
}