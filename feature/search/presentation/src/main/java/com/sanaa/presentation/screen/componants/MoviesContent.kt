package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.search.presentation.R
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.presentation.screen.componants.cards.MediaPosterCard
import com.sanaa.presentation.screen.componants.cards.SaveIconChip
import com.sanaa.presentation.screen.state.MediaTypeUi
import com.sanaa.presentation.screen.state.MovieUiModel
import com.sanaa.presentation.screen.state.RecentViewedUiModel
import androidx.compose.ui.Alignment
import androidx.paging.LoadState
import com.sanaa.designsystem.design_system.component.indicator.WavyProgressIndicator

@Composable
fun MoviesContent(
    moviesPagingData: LazyPagingItems<MovieUiModel>,
    onMovieClick: (RecentViewedUiModel, MovieUiModel) -> Unit,
    onSaveIconClicked:()-> Unit
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
        items(moviesPagingData.itemCount) { index ->
            val movie = moviesPagingData[index]
            if (movie != null) {
                MediaPosterCard(
                    PosterImage = {
                        RemoteBlurredSensitiveImage(
                            imageUrl = movie.imageUrl,
                            modifier = Modifier.fillMaxWidth(),
                            sensitiveContentThreshold = 0.2f,
                            safeContentThreshold = 0.7f,
                            placeholderContent = {
                                RemoteImagePlaceholder(Modifier.fillMaxSize())
                            },
                            errorContent = {
                                RemoteImagePlaceholder(Modifier.fillMaxSize())
                            },
                            contentDescription = movie.title,
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
                        SaveIconChip(onClick = {
                            onSaveIconClicked()
                        })
                    },
                    onCardClick = {
                        onMovieClick(
                            RecentViewedUiModel(
                                id = movie.id,
                                imageUrl = movie.imageUrl,
                                mediaType = MediaTypeUi.MOVIE
                            ),
                            MovieUiModel(
                                id = movie.id,
                                title = movie.title,
                                imageUrl = movie.imageUrl,
                                rating = movie.rating,
                            )
                        )
                    }
                )
            }
        }

        if (moviesPagingData.loadState.append is LoadState.Loading) {
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