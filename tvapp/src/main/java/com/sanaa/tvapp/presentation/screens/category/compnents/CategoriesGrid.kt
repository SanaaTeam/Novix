package com.sanaa.tvapp.presentation.screens.category.compnents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
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
import com.sanaa.tvapp.presentation.screens.category.CategoriesScreenInteractionListener
import com.sanaa.tvapp.presentation.screens.category.state.CategoryUiState
import com.sanaa.tvapp.presentation.screens.home.component.GenreTaps

@Composable
fun CategoriesGrid(
    categories: List<CategoryUiState>,
    onCategoryClick: (category: CategoryUiState) -> Unit,
    interactionListener: CategoriesScreenInteractionListener,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = modifier.padding(top = 24.dp),
        contentPadding = PaddingValues(
            start = 24.dp,
            end = 24.dp,
            bottom = 12.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            GenreTaps(
                modifier = Modifier,
                onFocus = { interactionListener.onTabChanged(it) }
            )
        }

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
