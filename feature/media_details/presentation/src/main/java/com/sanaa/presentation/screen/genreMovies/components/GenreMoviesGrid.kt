package com.sanaa.presentation.screen.genreMovies.components

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
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.poster.MediaPosterCard
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.presentation.api.LocalSafeContentThreshold
import com.sanaa.presentation.model.MovieUiModel
import com.sanaa.presentation.screen.genreMovies.GenreMoviesGridInteractionListener
import com.sanaa.presentation.shared_component.RemoteImagePlaceholder
import com.sanaa.designsystem.R as designR

@Composable
fun GenreMoviesGrid(
    pagedMovies: LazyPagingItems<MovieUiModel>,
   interactionListener: GenreMoviesGridInteractionListener,
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 140.dp),
        contentPadding = PaddingValues(
            start = 16.dp, end = 16.dp, bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(pagedMovies.itemCount) { index ->
            val movie = pagedMovies[index] ?: return@items
            MediaPosterCard(
                posterImage = {
                    RemoteBlurredSensitiveImage(
                        imageUrl = movie.posterUrl.orEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        sensitiveContentThreshold = 0.2f,
                        isBlurEnabled = LocalSafeContentThreshold.current != 0f,
                        safeContentThreshold = LocalSafeContentThreshold.current,
                        contentDescription = movie.title,
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
                            icon = painterResource(designR.drawable.icon_eye_slash),
                        )
                    }
                },
                topLeftContent = {
                    SaveIconChip(onClick = {
                        interactionListener.onSaveIconClick(movie)
                    })
                },
                onCardClick = { interactionListener.onMovieClick(movie.id) }
            )
        }

        if (pagedMovies.loadState.append is LoadState.Loading) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
            }
        }
    }
}