package com.sanaa.presentation.screen.watchingHistory.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.chips.ToggleableChip
import com.sanaa.feature.userprofile.presentation.R
import com.sanaa.presentation.model.MediaItemUiModel
import com.sanaa.presentation.screen.watchingHistory.GenreUiState

@Composable
fun MediaListSectionContent(
    genres: List<GenreUiState>,
    mediaList: List<MediaItemUiModel>,
    selectedGenreId: Int?,
    onGenreClick: (Int?) -> Unit,
    onMediaClick: (MediaItemUiModel) -> Unit,
    modifier: Modifier = Modifier,
    onSaveIconClick: (MediaItemUiModel) -> Unit = {},
) {
    Column(
        modifier = modifier,
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
            items(items = genres, key = { it.id }) { genre ->
                ToggleableChip(
                    text = genre.name,
                    onClick = { onGenreClick(genre.id) },
                    isSelected = genre.id == selectedGenreId,
                )
            }
        }
        MediaListGrid(
            mediaList = mediaList,
            onMediaClick = onMediaClick,
            onSaveIconClick = onSaveIconClick,
        )
    }
}
