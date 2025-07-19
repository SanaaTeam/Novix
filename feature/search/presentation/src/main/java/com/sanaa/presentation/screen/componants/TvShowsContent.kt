package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.presentation.screen.componants.cards.MediaPosterCard
import com.sanaa.presentation.screen.componants.cards.SaveIconChip
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredHaramImageViewer
import com.sanaa.presentation.R
import com.sanaa.presentation.screen.state.MediaTypeUi
import com.sanaa.presentation.screen.state.RecentViewedUiModel
import com.sanaa.presentation.screen.state.TvShowUiModel

@Composable
fun TvShowsContent(
    tvShowsPagingData: LazyPagingItems<TvShowUiModel>,
    onTvShowClick: (RecentViewedUiModel, TvShowUiModel) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 140.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(count = tvShowsPagingData.itemCount) { index ->
            val tvShow = tvShowsPagingData[index]
            if (tvShow != null) {
                MediaPosterCard(
                    boastImage = {
                        RemoteBlurredHaramImageViewer(
                            imageUrl = tvShow.imageUrl,
                            modifier = Modifier.fillMaxWidth(),
                            blurRadius = 150,
                            haramThreshold = 0.2f,
                            nonHaramThreshold = 0.7f,
                            contentDescription = tvShow.title,
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
                                icon = painterResource(com.sanaa.designsystem.R.drawable.icon_eye_slash),
                            )
                        }
                    },
                    topLeftContent = {
                        SaveIconChip(onClick = {})
                    },
                    onCardClick = {
                        onTvShowClick(
                            RecentViewedUiModel(
                                id = tvShow.id,
                                imageUrl = tvShow.imageUrl,
                                mediaType = MediaTypeUi.TV_SERIES
                            ),
                            TvShowUiModel(
                                id = tvShow.id,
                                title = tvShow.title,
                                imageUrl = tvShow.imageUrl,
                                rating = tvShow.rating,
                            )
                        )
                    }
                )
            }
        }

        if (tvShowsPagingData.loadState.append is LoadState.Loading) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    WavyProgressIndicator()
                }
            }
        }
    }
}