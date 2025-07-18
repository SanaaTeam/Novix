package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.chips.CategoryChip
import com.sanaa.designsystem.design_system.theme.NovixTheme

@Composable
fun CategoryTab(
    categories: List<String>,
    selectedIndex: Int,
    onCategorySelected: (Int) -> Unit,
) {

    Column {
        ScrollableTabRow(
            selectedTabIndex = selectedIndex,
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
            edgePadding = 8.dp,
            indicator = {},
            divider = {}
        ) {
            categories.forEachIndexed { index, title ->
                Tab(
                    selected = selectedIndex == index,
                    onClick = { onCategorySelected(index) },
                    selectedContentColor = Color.Transparent,
                    unselectedContentColor = Color.Transparent
                )
                {
                    CategoryChip(
                        text = title,
                        isSelected = selectedIndex == index,
                        onClick = { onCategorySelected(index) },
                        modifier = Modifier.padding(end = 8.dp, start = 8.dp)
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewCategoryTab() {
    val categories = listOf("Category1", "Category2", "Category3")
    NovixTheme(isSystemInDarkTheme()) {
        CategoryTab(categories, 1, {})
    }
}
