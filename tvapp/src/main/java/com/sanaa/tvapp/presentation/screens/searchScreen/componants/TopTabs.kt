package com.sanaa.tvapp.presentation.screens.searchScreen.componants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TvTabs(
    categories: List<String>,
    selectedIndex: Int,
    onCategorySelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
    ) {
        categories.forEachIndexed { index, title ->
            ToggleableChipTv(
                text = title,
                isSelected = selectedIndex == index,
                onClick = { onCategorySelected(index) }
            )
        }
    }
}

@Composable
@androidx.compose.ui.tooling.preview.Preview(name = "TvTabs - First Selected", showBackground = true)
fun PreviewTvTabs_FirstSelected() {
    TvTabs(
        categories = listOf("Movies", "TV Shows", "Actors"),
        selectedIndex = 0,
        onCategorySelected = {}
    )
}

@Composable
@androidx.compose.ui.tooling.preview.Preview(name = "TvTabs - Second Selected", showBackground = true)
fun PreviewTvTabs_SecondSelected() {
    TvTabs(
        categories = listOf("Movies", "TV Shows", "Actors"),
        selectedIndex = 1,
        onCategorySelected = {}
    )
}

@Composable
@androidx.compose.ui.tooling.preview.Preview(name = "TvTabs - Third Selected", showBackground = true)
fun PreviewTvTabs_ThirdSelected() {
    TvTabs(
        categories = listOf("Movies", "TV Shows", "Actors"),
        selectedIndex = 2,
        onCategorySelected = {}
    )
}

@Composable
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
fun PreviewTvTabs_Interactive() {
    var selectedIndex by remember { mutableStateOf(0) }

    TvTabs(
        categories = listOf("Movies", "TV Shows", "Actors"),
        selectedIndex = selectedIndex,
        onCategorySelected = { selectedIndex = it }
    )
}