package com.sanaa.designsystem.design_system.component.top_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    logoLabel: String = stringResource(id = R.string.novix),
    subtitle: String = stringResource(id = R.string.home_bar_subtitle),
) {
    val iconGradient = Brush.verticalGradient(
        colors = listOf(Theme.colors.primary, Theme.colors.primary)
    )
    Row(
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.icon_logo),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .graphicsLayer(alpha = 0.99f)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(iconGradient, blendMode = BlendMode.SrcAtop)
                    }
                },
        )
        Column {
            Text(
                text = logoLabel,
                style = Theme.textStyle.title.medium,
                color = Theme.colors.body
            )
            Text(
                text = subtitle,
                style = Theme.textStyle.label.small,
                color = Theme.colors.hint
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewHomeTopBar() {
    NovixTheme(
        isDarkMode = isSystemInDarkTheme()
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 40.dp)
                .background(Theme.colors.surface)
                .fillMaxWidth()
        ) {
            HomeTopBar()
        }
    }

}