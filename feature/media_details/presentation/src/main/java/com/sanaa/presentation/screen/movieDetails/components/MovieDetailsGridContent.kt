package com.sanaa.presentation.screen.movieDetails.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.sanaa.designsystem.design_system.component.button.TextButton
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.presentation.model.MovieUiModel
import com.sanaa.presentation.modifier.fillWidthOfParent
import com.sanaa.presentation.screen.movieDetails.MovieDetailsScreenInteractionListener
import com.sanaa.presentation.screen.movieDetails.MovieDetailsUiState
import com.sanaa.presentation.screen.series.components.CastComponent
import com.sanaa.presentation.shared_component.DotSeparator
import com.sanaa.presentation.shared_component.IconWithText
import com.sanaa.presentation.shared_component.ImageSlider
import com.sanaa.presentation.shared_component.InfoSection
import com.sanaa.presentation.shared_component.OverviewSection
import com.sanaa.presentation.util.toLocalizedDigits
import java.util.Locale
import kotlin.time.Duration.Companion.hours
import com.sanaa.designsystem.R as designR

@Composable
fun MovieDetailsGridContent(
    state: MovieDetailsUiState,
    pagedSimilarMovies: LazyPagingItems<MovieUiModel>,
    locale: Locale,
    interactionListener: MovieDetailsScreenInteractionListener
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 120.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(
            start = 16.dp, end = 16.dp, bottom = 100.dp
        )
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Box(modifier = Modifier.fillWidthOfParent(16.dp)) {
                ImageSlider(
                    images = state.imagesUrls,
                    contentDescription = state.movieDetails.title,
                    modifier = Modifier.fillMaxWidth()
                )

                InfoSection(
                    title = state.movieDetails.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 208.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        state.movieDetails.genres.forEachIndexed { index, genre ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    interactionListener.onGenreClicked(genre)
                                }
                            ) {
                                Text(
                                    text = genre.name,
                                    style = Theme.textStyle.label.small,
                                    color = Theme.colors.body,
                                )
                                if (index < state.movieDetails.genres.lastIndex) {
                                    DotSeparator()
                                }
                            }
                        }
                    }
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            state.movieDetails.rating?.let {
                                IconWithText(
                                    iconRes = designR.drawable.star,
                                    contentDescription = null,
                                    textColor = Theme.colors.title,
                                    text = state.movieDetails.rating,
                                    tint = Theme.colors.statusColors.yellowAccent
                                )
                                DotSeparator()
                            }
                            state.movieDetails.duration?.let { duration ->
                                val hours = duration.inWholeHours
                                val minutes =
                                    (duration - hours.hours).inWholeMinutes

                                val durationText = buildString {
                                    if (hours > 0) append(
                                        "${
                                            hours.toInt().toLocalizedDigits(locale)
                                        }${stringResource(R.string.hours_label)} "
                                    )
                                    if (minutes > 0) append(
                                        "${
                                            minutes.toInt()
                                                .toLocalizedDigits(locale)
                                        }${stringResource(R.string.minutes_label)}"
                                    )
                                }.trim()


                                IconWithText(
                                    iconRes = R.drawable.icon_duration,
                                    contentDescription = null,
                                    text = durationText,
                                    tint = Theme.colors.body
                                )
                                DotSeparator()
                            }

                            IconWithText(
                                iconRes = R.drawable.icon_calender,
                                contentDescription = null,
                                text = state.movieDetails.releaseDate,
                                tint = Theme.colors.body
                            )
                        }
                    }
                    TextButton(
                        text = stringResource(id = R.string.view_reviews),
                        textColor = Theme.colors.primary,
                        onClick = { interactionListener.onShowReviewsClick(state.movieDetails.id) })
                }
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            if (state.movieDetails.overview.isNotEmpty()) {
                OverviewSection(
                    overview = state.movieDetails.overview,
                    onReadMore = { interactionListener.onReadMoreClick() },
                    modifier = Modifier.padding(vertical = 16.dp),
                    titleResId = R.string.overview
                )
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            if (state.cast.isNotEmpty()) CastComponent(
                casts = state.cast,
                onActorClicked = interactionListener::onActorCardClick,
                modifier = Modifier.fillWidthOfParent(16.dp)
            )
        }
        if (pagedSimilarMovies.itemCount > 0) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = stringResource(id = R.string.more_like_this),
                    color = Theme.colors.title,
                    style = Theme.textStyle.title.medium,
                    modifier = Modifier.padding(bottom = 4.dp, top = 16.dp)
                )
            }
            items(pagedSimilarMovies.itemCount) { index ->
                val item = pagedSimilarMovies[index] ?: return@items

                MoreLikeThisCard(
                    movie = item,
                    modifier = Modifier.padding(bottom = 12.dp),
                    onBookmarkClick = { interactionListener.onBookmarkClick(item.id) },
                    onMovieClick = { interactionListener.onSimilarMovieClick(item.id) },
                )
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                if (pagedSimilarMovies.loadState.append is androidx.paging.LoadState.Loading) {
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