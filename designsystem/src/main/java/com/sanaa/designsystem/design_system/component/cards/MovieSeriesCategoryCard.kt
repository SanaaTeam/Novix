package com.sanaa.designsystem.design_system.component.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun MovieSeriesCategoryCard(
    title: String,
    poster: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {

    val boxOverlayBackgroundColor = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF0D0608),
            Color(0xCC0D0608),
            Color(0xB20D0608),
            Color(0x000D0608),

            )
    )

    Box(
        modifier = modifier
            .height(68.dp)
            .border(
                width = 1.dp, color = Theme.colors.stroke, shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                onClick = onClick, onClickLabel = title
            )
    ) {
        Image(
            painter = poster,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(boxOverlayBackgroundColor)
        ) {
            Text(
                text = title,
                style = Theme.textStyle.label.large,
                color = Theme.colors.onPrimary,
                modifier = Modifier
                    .padding(
                        horizontal = 8.dp, vertical = 8.dp
                    )
                    .align(Alignment.TopStart)
            )
        }
    }

}

@PreviewLightDark
@Composable
private fun PreviewMovieCategorySeriesCard() {
    NovixTheme(isSystemInDarkTheme()) {
        Row(
            modifier = Modifier
                .background(color = Theme.colors.surface)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            MovieSeriesCategoryCard(
                modifier = Modifier.weight(1f),
                title = "Action",
                poster = painterResource(id = R.drawable.category_fantasy)
            )
            MovieSeriesCategoryCard(
                modifier = Modifier.weight(1f),
                title = "Fantasy",
                poster = painterResource(id = R.drawable.category_fantasy)
            )
        }
    }
}