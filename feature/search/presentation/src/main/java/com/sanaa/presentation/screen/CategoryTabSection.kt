package com.sanaa.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sanaa.designsystem.design_system.component.chips.CategoryChip

@Composable
fun CategoryTabSection(
    categories: List<String>,
    selectedIndex: Int,
    onCategorySelected: (Int) -> Unit
) {

    Column {
        TabRow(
            selectedTabIndex = selectedIndex,
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
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
                        onClick = { onCategorySelected(index) }
                    )
                }

            }
        }
    }
}

