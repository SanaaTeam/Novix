package com.sanaa.presentation.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.R

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    logoLabel: String = stringResource(id = R.string.novix),
    subtitle: String = stringResource(id = R.string.home_bar_subtitle),
) {

    Row(
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.icon_logo),
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            colorFilter = ColorFilter.tint(Theme.colors.primary)
        )
        Column {
            BasicText(
                text = logoLabel,
                style = Theme.textStyle.title.medium.copy(color = Theme.colors.body),
            )
            BasicText(
                text = subtitle,
                style = Theme.textStyle.label.small.copy(color = Theme.colors.hint),
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