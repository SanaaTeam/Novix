package com.sanaa.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.chips.NovixToggleableChip
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaType

@Composable
fun MediaListSectionContent(
    genres: List<GenreUiState>,
    mediaList: List<MediaItem>,
    selectedGenreId: Int?,
    onGenreClick: (Int?) -> Unit,
    onMediaClick: (MediaItem) -> Unit,
    modifier: Modifier = Modifier,
    onSaveIconClick: (MediaItem) -> Unit = {},
    isScrollEnabled: Boolean = true,
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
                NovixToggleableChip(
                    text = stringResource(R.string.all),
                    onClick = {
                        onGenreClick(null)
                    },
                    isSelected = selectedGenreId == null,
                )
            }
            items(genres, key = { it.id }) { genre ->
                NovixToggleableChip(
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
            isScrollEnabled = isScrollEnabled,
        )
    }
}

@PreviewLightDark
@Composable
private fun MediaListSectionContentPreview() {

    val genres = listOf(
        GenreUiState(id = 1, name = "Action"),
        GenreUiState(id = 2, name = "Comedy"),
        GenreUiState(id = 3, name = "Drama"),
    )
    val mediaList = listOf(
        MediaItem(
            id = 1,
            title = "Movie 1",
            imageUrl = "",
            rating = 8.5f,
            mediaType = MediaType.MOVIE
        ),
        MediaItem(
            id = 2,
            title = "Movie 2",
            imageUrl = "",
            rating = 7.0f,
            mediaType = MediaType.MOVIE
        ),
        MediaItem(
            id = 3,
            title = "Tv Show 3",
            imageUrl = "",
            rating = 9.0f,
            mediaType = MediaType.TV_SHOW
        )
    )

    var selectedGenreId by remember { mutableStateOf<Int?>(null) }

    NovixTheme(isSystemInDarkTheme()) {
        MediaListSectionContent(
            genres = genres,
            mediaList = mediaList,
            selectedGenreId = selectedGenreId,
            onMediaClick = { },
            onGenreClick = { selectedGenreId = it },
            modifier = Modifier
                .background(Theme.colors.surface)
                .padding(vertical = 16.dp)
        )
    }
}