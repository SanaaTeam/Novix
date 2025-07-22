package com.sanaa.presentation.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredHaramImageViewer
import com.sanaa.presentation.components.cards.MediaPosterCard
import com.sanaa.presentation.components.chips.SaveIconChip
import com.sanaa.presentation.model.MediaItem
import com.sanaa.presentation.model.MediaType

@Composable
fun MediaListGrid(
    mediaList: List<MediaItem>,
    modifier: Modifier = Modifier,
    onMediaClick: (MediaItem) -> Unit = {},
    onSaveIconClick: (MediaItem) -> Unit = {},
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 140.dp),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items (items = mediaList, key = { item -> item.id }) { media ->
            MediaPosterCard(
                posterImage = {
                    RemoteBlurredHaramImageViewer(
                        imageUrl = media.imageUrl.orEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        blurRadius = 150,
                        haramThreshold = 0.2f,
                        nonHaramThreshold = 0.7f,
                        contentDescription = media.title,
                        placeholderContent = {
                            RemoteImagePlaceholder(Modifier.fillMaxSize())
                        },
                        errorContent = {
                            RemoteImagePlaceholder(Modifier.fillMaxSize())
                        },
                    ) {
                        OnBlurContent(
                            hintText = stringResource(R.string.unsuitable_image),
                            textStyle = Theme.textStyle.body.small.copy(
                                color = Color(0x99FFFFFF)),
                            iconSize = 24.dp,
                            icon = painterResource(R.drawable.icon_eye_slash),
                        )
                    }
                },
                topLeftContent = { SaveIconChip(onClick = { onSaveIconClick(media) }) },
                onCardClick = { onMediaClick(media) })
        }
    }
}

@PreviewLightDark
@Composable
fun MediaListGridPreview(modifier: Modifier = Modifier) {
    NovixTheme (isDarkMode = isSystemInDarkTheme()) {
        MediaListGrid(
            mediaList = listOf(
                MediaItem(
                    id = 1,
                    title = "Media 1",
                    imageUrl = "https://example.com/image1.jpg",
                    mediaType = MediaType.MOVIE
                ),
                MediaItem(
                    id = 2,
                    title = "Media 2",
                    imageUrl = "https://example.com/image2.jpg",
                    mediaType = MediaType.TV_SHOW
                ),
                MediaItem(
                    id = 3,
                    title = "Media 3",
                    imageUrl = "https://example.com/image3.jpg",
                    mediaType = MediaType.MOVIE
                )
            ),
            modifier = modifier,
            onMediaClick = {},
            onSaveIconClick = {}
        )
    }
}
