package com.sanaa.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.sanaa.designsystem.design_system.component.chips.ToggleableChip
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItemUiState

@Composable
fun PaginatedMediaListSectionContent(
    genres: List<GenreUiState>,
    mediaList: LazyPagingItems<MediaItemUiState>,
    selectedGenreId: Int?,
    onGenreClick: (Int?) -> Unit,
    onMediaClick: (MediaItemUiState) -> Unit,
    modifier: Modifier = Modifier,
    onSaveIconClick: (MediaItemUiState) -> Unit = {},
    isScrollEnabled: Boolean = true,
) {
    Column(
        modifier = modifier.navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                ToggleableChip(
                    text = stringResource(R.string.all),
                    onClick = {
                        onGenreClick(null)
                    },
                    isSelected = selectedGenreId == null,
                )
            }
            items(genres, key = { it.id }) { genre ->
                ToggleableChip(
                    text = genre.name,
                    onClick = { onGenreClick(genre.id) },
                    isSelected = genre.id == selectedGenreId,
                )
            }
        }
        if (mediaList.loadState.refresh is LoadState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize().padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        } else {
            PaginatedMediaListGrid(
                mediaList = mediaList,
                onMediaClick = onMediaClick,
                onSaveIconClick = onSaveIconClick,
                isScrollEnabled = isScrollEnabled,
            )
        }
    }
}