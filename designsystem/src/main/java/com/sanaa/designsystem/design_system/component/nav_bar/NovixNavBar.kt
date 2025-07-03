package com.sanaa.designsystem.design_system.component.nav_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme


@Composable
fun NovixNavBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    val strokeColor = Theme.colors.stroke
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Theme.colors.surface)
            .drawBehind {
                drawLine(
                    color = strokeColor,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            }
            .height(70.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        content()
    }
}


@PreviewLightDark
@Composable
private fun PreviewNovixNavBar() {
    NovixTheme(isDarkMode = isSystemInDarkTheme()) {

        var selectedIndex by remember { mutableIntStateOf(0) }
        Box(
            Modifier
                .fillMaxSize()
                .background(color = Theme.colors.surface),
            contentAlignment = Alignment.Center
        ) {
            NovixNavBar {
                NovixNavBarItem(
                    modifier = Modifier.weight(1f),
                    isSelected = selectedIndex == 0,
                    onClick = { selectedIndex = 0 },
                    iconRes = R.drawable.icon_home,
                    selectedIconRes = R.drawable.icon_home_selected,
                )
                NovixNavBarItem(
                    modifier = Modifier.weight(1f),
                    isSelected = selectedIndex == 1,
                    onClick = { selectedIndex = 1 },
                    iconRes = R.drawable.icon_search,
                    selectedIconRes = R.drawable.icon_search_selected
                )
                NovixNavBarItem(
                    modifier = Modifier.weight(1f),
                    isSelected = selectedIndex == 2,
                    onClick = { selectedIndex = 2 },
                    iconRes = R.drawable.icon_category,
                    selectedIconRes = R.drawable.icon_category_selected
                )
                NovixNavBarItem(
                    modifier = Modifier.weight(1f),
                    isSelected = selectedIndex == 3,
                    onClick = { selectedIndex = 3 },
                    iconRes = R.drawable.icon_save,
                    selectedIconRes = R.drawable.icon_save_selected
                )
                NovixNavBarItem(
                    modifier = Modifier.weight(1f),
                    isSelected = selectedIndex == 4,
                    onClick = { selectedIndex = 4 },
                    iconRes = R.drawable.icon_account,
                    selectedIconRes = R.drawable.icon_account_selected
                )
            }
        }
    }

}