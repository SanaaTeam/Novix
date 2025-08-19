package com.sanaa.tvapp.presentation.screens.category.compnents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.presentation.screens.category.state.CategoryUiState

@Composable
fun CategoriesGrid(
    categories: List<CategoryUiState>,
    onCategoryClick: (category: CategoryUiState) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = modifier,
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = 12.dp,
            top = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(
            items = categories,
            key = { _, category -> category.id }
        ) { _, category ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onCategoryClick(category) },
                border = CardDefaults.border(
                    border = Border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = Theme.colors.stroke,
                        ),
                        shape = RoundedCornerShape(12.dp),
                    ),
                    focusedBorder = Border(
                        border = BorderStroke(
                            width = 3.dp,
                            color = Theme.colors.primary,
                        ),
                        shape = RoundedCornerShape(12.dp),
                    )
                ),
                shape = CardDefaults.shape(RoundedCornerShape(12.dp)),
                scale = CardDefaults.scale(focusedScale = 1.05f),
            ) {
                CategoryCard(category)
            }
        }
    }
}
