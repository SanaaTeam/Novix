package com.sanaa.presentation.screen.movieDetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.presentation.model.MovieUiModel
import com.sanaa.presentation.screen.movieDetails.MovieDetailsScreenInteractionListener

@Composable
fun SimilarMoviesSection(
    pagedSimilarMovies: LazyPagingItems<MovieUiModel>,
    interactionListener: MovieDetailsScreenInteractionListener,
    lazyState: LazyGridState
) {
    LazyVerticalGrid(
        state = lazyState,
        columns = GridCells.Adaptive(minSize = 120.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(
            start = 16.dp, end = 16.dp, bottom = 100.dp
        ),
        modifier = Modifier.fillMaxSize()
    ) {

        if (pagedSimilarMovies.itemCount > 0) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                AppText(
                    text = stringResource(id = R.string.more_like_this),
                    color = Theme.colors.title,
                    style = Theme.textStyle.title.medium,
                    modifier = Modifier.padding(bottom = 4.dp, top = 16.dp)
                )
            }

            items(pagedSimilarMovies.itemCount) { index ->
                val movie = pagedSimilarMovies[index]
                if (movie != null) {
                    MoreLikeThisCard(
                        movie = movie,
                        modifier = Modifier.padding(bottom = 12.dp),
                        interactionListener = interactionListener
                    )
                }
            }

            if (pagedSimilarMovies.loadState.append is LoadState.Loading) {
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
}