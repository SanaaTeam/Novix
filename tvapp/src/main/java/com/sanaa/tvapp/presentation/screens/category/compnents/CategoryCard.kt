package com.sanaa.tvapp.presentation.screens.category.compnents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.R
import com.sanaa.tvapp.presentation.screens.category.state.CategoryUiState

val boxOverlayBackgroundColor = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFF0D0608),
        Color(0xCC0D0608),
        Color(0xB20D0608),
        Color(0x000D0608),

        )
)


@Composable
fun CategoryCard(
    category: CategoryUiState,
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier
            .aspectRatio(160 / 68f)
            .border(
                width = 1.dp, color = Theme.colors.stroke, shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))

    ) {
        AsyncImage(
            modifier = Modifier
                .matchParentSize(),
            model = category.imageResourceId,
            contentDescription = category.name,
            contentScale = ContentScale.Crop,
        )

        AppText(
            text = category.name,
            style = Theme.textStyle.label.large,
            color = Theme.colors.onPrimary,
            modifier = Modifier
                .matchParentSize()
                .background(boxOverlayBackgroundColor)
                .padding(
                    horizontal = 8.dp, vertical = 8.dp
                )
                .align(Alignment.TopStart)
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewCategory() {
    NovixTheme(isSystemInDarkTheme()) {
        Row(
            modifier = Modifier
                .background(color = Theme.colors.surface)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            CategoryCard(
                modifier = Modifier.weight(1f),
                category = CategoryUiState(
                    id = 1,
                    imageResourceId = R.drawable.movie_war,
                    name = "War"
                )
            )
            CategoryCard(
                modifier = Modifier.weight(1f),
                category = CategoryUiState(
                    id = 1,
                    imageResourceId = R.drawable.movie_war,
                    name = "War"
                )
            )
        }
    }
}