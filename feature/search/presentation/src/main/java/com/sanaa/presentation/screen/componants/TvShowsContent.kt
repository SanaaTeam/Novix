package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.sanaa.designsystem.design_system.component.cards.MovieSeriesPosterCard
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.image_viewer.component.RemoteCensoredImageViewer
import com.sanaa.presentation.R
import com.sanaa.presentation.screen.state.MediaTypeUi
import com.sanaa.presentation.screen.state.RecentViewedUiModel
import com.sanaa.presentation.screen.state.TvShowUiModel

@Composable
fun TvShowsContent(
    tvShowsPagingData: LazyPagingItems<TvShowUiModel>,
    onTvShowClick: (RecentViewedUiModel) -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val placeholderResId = if (isDarkTheme) {
        R.drawable.movie_placeholder_dark
    } else {
        R.drawable.movie_placeholder_light
    }


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
                MovieSeriesPosterCard(
                    boastImage = {
                        RemoteCensoredImageViewer(
                            imageUrl = tvShow.imageUrl,
                            modifier = Modifier.fillMaxWidth(),
                            blurRadius = 150,
                            sfwThreshold = 0.75f,
                            nsfwThreshold = 0.15f,
                            contentDescription = tvShow.title,
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(placeholderResId),
                            error = painterResource(placeholderResId),
                        )
                    },
                    topLeftContent = {
                        SaveIconChip(onClick = {})
                    },
                    onCardClick = {
                        onTvShowClick(
                            RecentViewedUiModel(
                                id = tvShow.id,
                                imageUrl = tvShow.imageUrl,
                                mediaType = MediaTypeUi.TV_SERIES.name
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
