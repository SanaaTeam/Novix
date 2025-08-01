package com.sanaa.presentation.screen.homeScreen.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.component.chips.NovixToggleableChip
import com.sanaa.designsystem.design_system.component.section_header.NovixSectionHeader
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.home.presentation.R
import com.sanaa.image_viewer.component.RemoteBlurredHaramImageViewer
import com.sanaa.presentation.components.RemoteImagePlaceholder
import com.sanaa.presentation.components.cards.MediaPosterCard
import com.sanaa.presentation.components.chips.SaveIconChip
import com.sanaa.presentation.modifiers.fillWidthOfParent
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaTypeUi

fun LazyGridScope.upcomingSection(
    upcomingMovies: LazyPagingItems<MediaItem>,
    movieGenres: List<GenreUiState>,
    movieSelectedGenreId: Int?,
    onGenreClick: (Int?) -> Unit,
    onSaveIconClick: (item: MediaItem) -> Unit,
    onMovieClick: (id: Int, mediaTypeUi: MediaTypeUi) -> Unit,
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        NovixSectionHeader(
            title = stringResource(R.string.up_upcoming),
            modifier = Modifier
                .fillWidthOfParent(16.dp)
                .padding(
                    top = 24.dp, bottom = 12.dp
                ),
        )
    }

    stickyHeader {
        LazyRow(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 8.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillWidthOfParent(16.dp)
                .background(color = Theme.colors.surface)

        ) {
            item {
                NovixToggleableChip(
                    text = stringResource(R.string.all),
                    onClick = {
                        onGenreClick(null)
                    },
                    isSelected = movieSelectedGenreId == null,
                )
            }
            items(movieGenres, key = { it.id }) { genre ->
                NovixToggleableChip(
                    text = genre.name,
                    onClick = { onGenreClick(genre.id) },
                    isSelected = genre.id == movieSelectedGenreId,
                )
            }
        }
    }
    items(upcomingMovies.itemCount) { index ->
        val item = upcomingMovies[index] ?: return@items
        MediaPosterCard(
            onCardClick = {
                onMovieClick(item.id, item.mediaTypeUi)
            },
            posterImage = {
                RemoteBlurredHaramImageViewer(
                    imageUrl = item.imageUrl.orEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    haramThreshold = 0.2f,
                    nonHaramThreshold = 0.7f,
                    contentDescription = item.title,
                    placeholderContent = {
                        RemoteImagePlaceholder(Modifier.fillMaxSize())
                    },
                    errorContent = {
                        RemoteImagePlaceholder(Modifier.fillMaxSize())
                    },
                ) {
                    OnBlurContent(
                        hintText = stringResource(com.sanaa.designsystem.R.string.unsuitable_image),
                        textStyle = Theme.textStyle.body.small.copy(
                            color = Color(0x99FFFFFF)
                        ),
                        iconSize = 24.dp,
                        icon = painterResource(com.sanaa.designsystem.R.drawable.icon_eye_slash),
                    )
                }
            }, topLeftContent = {
                SaveIconChip(onClick = { onSaveIconClick(item) })
            },
            modifier = Modifier.padding(
                bottom = 12.dp
            )
        )
    }

}