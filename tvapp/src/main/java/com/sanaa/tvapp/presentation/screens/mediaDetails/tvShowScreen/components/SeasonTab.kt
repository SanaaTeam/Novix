package com.sanaa.tvapp.presentation.screens.mediaDetails.tvShowScreen.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.chips.ToggleableChip
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.R

@Composable
fun SeasonTab(
    currentSeason: Int,
    onClick: (Int) -> Unit,
    seasonCounts: Int,
    modifier: Modifier = Modifier
) {
    AppText(
        text = stringResource(R.string.season),
        style = Theme.textStyle.title.medium,
        color = Theme.colors.title,
        modifier = Modifier.padding(horizontal = 36.dp, vertical = 8.dp)
    )
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = 36.dp)
    ) {
        itemsIndexed(
            items = (1..seasonCounts).toList(),
            key = { _, item -> item }
        ) { index, item ->
            ToggleableChip(
                modifier = Modifier,
                text = stringResource(R.string.season_number, item),
                onClick = {
                    onClick(item)
                    Log.d("test99", "SeasonTab: from itemsIndex with item : $item")
                          },
                isSelected = currentSeason == item,
            )
        }
    }
}