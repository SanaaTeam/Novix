package com.sanaa.presentation.screen.actor.componants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.section_header.NovixSectionHeader
import com.sanaa.designsystem.design_system.component.section_header.InlineAction

@Composable
fun <T> MediaSection(
    title: String,
    items: List<T>,
    modifier: Modifier = Modifier,
    onActionClick: () -> Unit = {},
    itemContent: @Composable (T) -> Unit,
) {
    if (items.isEmpty()) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    ) {

        NovixSectionHeader(
            title = title,
            modifier = Modifier.padding(bottom = 12.dp),
            rightContent = {
                InlineAction(
                    onClick = onActionClick
                )
            }
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
        ) {
            itemsIndexed(items) { _, item -> itemContent(item) }
        }
    }
}
