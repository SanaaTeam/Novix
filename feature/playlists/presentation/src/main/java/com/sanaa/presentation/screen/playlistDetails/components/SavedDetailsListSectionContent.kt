package com.sanaa.presentation.screen.playlistDetails.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.feature.playlists.presentation.R
import com.sanaa.presentation.screen.playlistDetails.state.MediaItem

@Composable
fun SavedDetailsListSectionContent(
    mediaList: LazyPagingItems<MediaItem>,
    onMediaClick: (MediaItem) -> Unit,
    modifier: Modifier = Modifier,
    onSaveIconClick: (MediaItem) -> Unit = {},
    isScrollEnabled: Boolean = true,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    safeContentThreshold: Float = 0.5f
) {
    val isListEmpty = mediaList.itemCount == 0

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        when {
            mediaList.loadState.refresh is LoadState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    LoadingIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            isListEmpty -> {
                EmptyItemsScreen(
                    messageText = stringResource(R.string.the_list_is_empty),
                )
            }

            else -> {
                PaginatedMediaListGrid(
                    mediaList = mediaList,
                    onMediaClick = onMediaClick,
                    onSaveIconClick = onSaveIconClick,
                    isScrollEnabled = isScrollEnabled,
                    safeContentThreshold = safeContentThreshold,
                    isDarkTheme = isDarkTheme,
                )

                if (mediaList.loadState.append is LoadState.Loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator()
                    }
                }
            }
        }
    }
}