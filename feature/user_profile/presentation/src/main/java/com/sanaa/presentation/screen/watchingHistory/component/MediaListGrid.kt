package com.sanaa.presentation.screen.watchingHistory.component

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
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.designsystem.design_system.component.poster.MediaPosterCard
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.presentation.model.MediaItemUiModel
import com.sanaa.presentation.screen.myRating.component.RemoteImagePlaceholder

@Composable
fun MediaListGrid(
    mediaList: List<MediaItemUiModel>,
    modifier: Modifier = Modifier,
    onMediaClick: (MediaItemUiModel) -> Unit = {},
    onSaveIconClick: (MediaItemUiModel) -> Unit = {},
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 140.dp),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            count = mediaList.size,
        ) { index ->
            val media = mediaList[index]
            media?.let {
                MediaPosterCard(
                    posterImage = {
                        RemoteBlurredSensitiveImage(
                            imageUrl = it.imageUrl.orEmpty(),
                            modifier = Modifier.fillMaxWidth(),
                            sensitiveContentThreshold = 0.2f,
                               contentDescription = "poster",
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
                    topLeftContent = {
                        SaveIconChip(
                            isSaved = it.isSaved,
                            onClick = { onSaveIconClick(it) }
                        )
                    },
                    onCardClick = { onMediaClick(it) }
                )
            }
        }
    }
}