package com.sanaa.presentation.screen.tvShow.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.presentation.screen.tvShow.TvShowScreenInteractionListener
import com.sanaa.presentation.screen.tvShow.TvShowScreenUiState
import com.sanaa.presentation.shared_component.OverviewSection


@Composable
fun TvShowDetailContent(
    state: TvShowScreenUiState,
    interactionListener: TvShowScreenInteractionListener,
    scrollState: androidx.compose.foundation.ScrollState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                state = scrollState
            )
    ) {
        Column(
            modifier = Modifier.padding(bottom = 104.dp)
        ) {
            TvShowHeaderSection(
                title = state.tvShow.title,
                rating = state.tvShow.rating,
                season = stringResource(
                    R.string.seasons_count, state.tvShow.seasonsCount
                ),
                airDate = state.tvShow.releaseDate,
                imagesUrl = state.images,
                genres = state.tvShow.genres,
                onReviewClicked = {
                    interactionListener.onViewReviewsClicked(
                        state.tvShow.id
                    )
                },
                onGenreClicked = { genre ->
                    interactionListener.onGenreClicked(
                        genre
                    )
                })

            if (state.tvShow.overview.isNotEmpty()) {
                OverviewSection(
                    onReadMore = {},
                    titleResId = R.string.overview,
                    overview = state.tvShow.overview,
                    modifier = Modifier.padding(
                        start = 16.dp, end = 16.dp, top = 16.dp
                    )
                )
            }

            if (state.cast.isNotEmpty())
                CastComponent(
                    casts = state.cast,
                    onActorClicked = interactionListener::onActorClicked,
                )
            SeasonTab(
                onClick = interactionListener::onSeasonNumberClicked,
                seasonCounts = state.tvShow.seasonsCount,
                currentSeason = state.selectedSeason,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            AnimatedContent(state.isLoadingEpisodes) { isLoadingEpisodes ->
                if (isLoadingEpisodes) {
                    Column(
                        modifier = Modifier
                            .heightIn(min = 300.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center

                    ) {
                        LoadingIndicator()
                    }
                } else {
                    EpisodesContent(
                        episodes = state.season.episodes,
                        tvShowId = state.tvShow.id,
                        onEpisodeClick = interactionListener::onEpisodeClicked
                    )
                }
            }
        }
    }
}