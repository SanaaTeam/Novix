package com.sanaa.presentation.screen.playlistDetails.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.presentation.screen.playlistDetails.state.MediaItem

@Composable
fun SavedDetailsListSectionContent(
    mediaList: LazyPagingItems<MediaItem>,
    onMediaClick: (MediaItem) -> Unit,
    modifier: Modifier = Modifier,
    onSaveIconClick: (MediaItem) -> Unit = {},
    isScrollEnabled: Boolean = true,
) {
    when (mediaList.loadState.refresh) {
        is LoadState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }
        else -> {
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
    }
}



