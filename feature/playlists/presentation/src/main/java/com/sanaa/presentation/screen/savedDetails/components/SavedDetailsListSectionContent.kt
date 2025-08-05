package com.sanaa.presentation.screen.savedDetails.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.sanaa.presentation.screen.savedDetails.state.MediaItem

@Composable
fun SavedDetailsListSectionContent(
    mediaList: LazyPagingItems<MediaItem>,
    onMediaClick: (MediaItem) -> Unit,
    modifier: Modifier = Modifier,
    onSaveIconClick: (MediaItem) -> Unit = {},
    isScrollEnabled: Boolean = true,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PaginatedMediaListGrid(
            mediaList = mediaList,
            onMediaClick = onMediaClick,
            onSaveIconClick = onSaveIconClick,
            isScrollEnabled = isScrollEnabled,
        )
    }
}



