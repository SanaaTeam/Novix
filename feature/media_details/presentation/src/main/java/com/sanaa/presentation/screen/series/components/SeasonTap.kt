package com.sanaa.presentation.screen.series.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.chips.ToggleableChip
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R

@Composable
fun SeasonTap(
    currentSeason: Int,
    onClick: (Int) -> Unit,
    seasonCounts: Int,
    modifier: Modifier = Modifier
) {

    AppText(
        text = stringResource(R.string.season),
        style = Theme.textStyle.title.medium,
        color = Theme.colors.title,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
    LazyRow(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        itemsIndexed(
            items = (1..seasonCounts).toList(),
            key = { _, item -> item }
        ) { index, item ->
            ToggleableChip(
                text = stringResource(R.string.season_number, item),
                onClick = { onClick(item) },
                isSelected = currentSeason == item,
            )
        }
    }
}