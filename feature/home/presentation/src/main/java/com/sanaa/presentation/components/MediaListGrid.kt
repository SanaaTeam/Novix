package com.sanaa.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.designsystem.design_system.component.poster.MediaPosterCard
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.presentation.providers.LocalSafeContentThreshold

import com.sanaa.presentation.state.MediaItem

@Composable
fun MediaListGrid(
    mediaList: List<MediaItem>,
    modifier: Modifier = Modifier,
    isScrollEnabled: Boolean = true,
    onMediaClick: (MediaItem) -> Unit = {},
    onSaveIconClick: (MediaItem) -> Unit = {},
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 140.dp),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        userScrollEnabled = isScrollEnabled
    ) {
        itemsIndexed(mediaList, key = { index, _ -> index }) { index, media ->
            MediaPosterCard(
                posterImage = {
                    RemoteBlurredSensitiveImage(
                        imageUrl = media.imageUrl.orEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        sensitiveContentThreshold = 0.2f,
                        isBlurEnabled = LocalSafeContentThreshold.current != 0f,
                        safeContentThreshold = LocalSafeContentThreshold.current,
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
                                color = Color(0x99FFFFFF)
                            ),
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