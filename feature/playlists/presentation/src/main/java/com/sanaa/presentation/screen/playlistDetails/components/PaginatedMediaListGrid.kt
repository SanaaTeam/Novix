package com.sanaa.presentation.screen.playlistDetails.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.presentation.playListProviders.LocalThemeProvider
import com.sanaa.presentation.screen.playlistDetails.components.chips.SaveIconChip
import com.sanaa.presentation.screen.playlistDetails.state.MediaItem

@Composable
fun PaginatedMediaListGrid(
    mediaList: LazyPagingItems<MediaItem>,
    modifier: Modifier = Modifier,
    isScrollEnabled: Boolean = true,
    onMediaClick: (MediaItem) -> Unit = {},
    onSaveIconClick: (MediaItem) -> Unit = {},
    safeContentThreshold: Float = 0.5f
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 140.dp),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        userScrollEnabled = isScrollEnabled
    ) {
        items(mediaList.itemCount) { index ->
            val media = mediaList[index] ?: return@items
            MediaPosterCard(
                posterImage = {
                    RemoteBlurredSensitiveImage(
                        imageUrl = media.imageUrl.orEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        sensitiveContentThreshold = 0.2f,
                        isBlurEnabled = safeContentThreshold != 0f,
                        safeContentThreshold = safeContentThreshold,
                        contentDescription = media.title,
                        placeholderContent = {
                            RemoteImagePlaceholder(
                                Modifier.fillMaxSize(),
                                isDarkTheme = LocalThemeProvider.current
                            )
                        },
                        errorContent = {
                            RemoteImagePlaceholder(
                                Modifier.fillMaxSize(),
                                isDarkTheme = LocalThemeProvider.current
                            )
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
                topLeftContent = {
                    SaveIconChip(
                        onClick = { onSaveIconClick(media) },
                        isSaved = true
                    )
                },
                onCardClick = { onMediaClick(media) })
        }
    }
}