package com.sanaa.presentation.screen.movieDetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
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
import com.sanaa.presentation.modifier.fillWidthOfParent
import com.sanaa.presentation.screen.movieDetails.MovieDetailsScreenInteractionListener
import com.sanaa.presentation.screen.movieDetails.MovieDetailsUiState
import com.sanaa.presentation.screen.tvShow.components.CastComponent
import com.sanaa.presentation.shared_component.OverviewSection
import java.util.Locale

@Composable
fun MovieDetailsGridContent(
    state: MovieDetailsUiState,
    pagedSimilarMovies: LazyPagingItems<MovieUiModel>,
    locale: Locale,
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
        // This is a single item for the entire header section
        item(span = { GridItemSpan(maxLineSpan) }) {
            MovieHeaderSection(state, interactionListener, locale)
        }

        // This is a single item for the overview section
        item(span = { GridItemSpan(maxLineSpan) }) {
            if (state.movieDetails.overview.isNotEmpty()) {
                OverviewSection(
                    overview = state.movieDetails.overview,
                    onReadMore = {},
                    modifier = Modifier.padding(vertical = 16.dp),
                    titleResId = R.string.overview
                )
            }
        }

        // This is a single item for the cast section
        item(span = { GridItemSpan(maxLineSpan) }) {
            if (state.cast.isNotEmpty()) {
                CastComponent(
                    casts = state.cast,
                    onActorClicked = interactionListener::onActorCardClick,
                    modifier = Modifier.fillWidthOfParent(16.dp)
                )
            }
        }

        // Now, add the similar movies section directly here,
        // using a separate item for the header and then items() for the list.
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
                    LoadingIndicator()
                }
            }
        }
    }
}