package com.sanaa.designsystem.design_system.component.tab

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabIndicatorScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.chips.NovixTaggableChip
import com.sanaa.designsystem.design_system.theme.NovixTheme

@Composable
fun NovixTab(
    categories: List<String>,
    selectedIndex: Int,
    onCategorySelected: (Int) -> Unit,
    divider: @Composable () -> Unit = {},
    containerColor: Color = Color.Transparent,
    edgePadding: Dp = 8.dp,
    indicator: @Composable TabIndicatorScope.() -> Unit = {}

) {
    Column {
        PrimaryScrollableTabRow(
            selectedTabIndex = selectedIndex,
            edgePadding = edgePadding,
            indicator = indicator,
            divider = divider,
            containerColor = containerColor
        ) {
            categories.forEachIndexed { index, title ->
                Tab(
                    selected = selectedIndex == index,
                    onClick = { onCategorySelected(index) },
                    selectedContentColor = Color.Transparent,
                    unselectedContentColor = Color.Transparent
                ) {
                    NovixTaggableChip(
                        text = title,
                        isSelected = selectedIndex == index,
                        onClick = { onCategorySelected(index) },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewNovixTab() {
    val categories = listOf("Category1", "Category2", "Category3")
    NovixTheme(isSystemInDarkTheme()) {
        NovixTab(categories, 1, {})
    }
}
