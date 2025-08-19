package com.sanaa.tvapp.presentation.screens.home.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.util.shimmerEffect.PlaceholderWithShimmerEffect

@Composable
fun Title(title: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = title,
        color = Theme.colors.title,
        style = Theme.textStyle.title.large
    )
}

@Composable
fun TitleShimmer() {
    PlaceholderWithShimmerEffect(
        modifier = Modifier.padding(bottom = 16.dp, top = 16.dp),
        width = 200.dp,
        height = 24.dp,
        borderColor = Color.Transparent,
    )
}