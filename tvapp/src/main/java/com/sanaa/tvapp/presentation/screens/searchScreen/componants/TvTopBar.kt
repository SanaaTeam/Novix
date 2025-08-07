package com.sanaa.tvapp.presentation.screens.searchScreen.componants

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.R

@Composable
fun TvTopBar(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 36.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TvTabs(
            categories = listOf("Movies", "TV Shows", "Actors"),
            selectedIndex = selectedTabIndex,
            onCategorySelected = onTabSelected
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.icon_logo), contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(Theme.colors.primary)
            )
            Text(
                text = "Novix",
                style = Theme.textStyle.title.medium,
                color = Theme.colors.body
            )
        }
    }

}

@Preview(device = Devices.TV_1080p, showBackground = true)
@Composable
private fun TvTopBarPrev() {
    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        TvTopBar(
            selectedTabIndex = 0,
            onTabSelected = {}
        )

    }
}