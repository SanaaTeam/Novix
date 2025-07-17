package com.sanaa.presentation.screen.series

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.design_system.component.loading.NovixLoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixBackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.presentation.R
import com.sanaa.presentation.component.OverviewSection
import com.sanaa.presentation.navigation.ActorDetailsScreenRoute
import com.sanaa.presentation.navigation.EpisodeDetailsScreenRoute
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.ReviewsScreenRoute
import com.sanaa.presentation.screen.series.components.BottomContainer
import com.sanaa.presentation.screen.series.components.CastComponent
import com.sanaa.presentation.screen.series.components.EpisodesContent
import com.sanaa.presentation.screen.series.components.SeasonTap
import com.sanaa.presentation.screen.series.components.SeriesHeaderSection
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SeriesScreen(
    seriesId: Int,
    viewModel: SeriesViewModel = koinViewModel(parameters = { parametersOf(seriesId) }),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val navController = LocalNavControllerProvider.current
    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
                is SeriesScreenEffects.NavigateToActorScreen -> {
                    navController.navigate(
                        ActorDetailsScreenRoute(it.actorId).route()
                    )
                }

                is SeriesScreenEffects.NavigateToEpisodeDetailsScreen -> {
                    navController.navigate(
                        EpisodeDetailsScreenRoute(
                            it.seriesId, it.seasonNumber, it.episodeNumber
                        ).route()
                    )
                }

                is SeriesScreenEffects.NavigateToReviewsScreen -> {
                    navController.navigate(
                        ReviewsScreenRoute(it.seriesId).route()
                    )
                }

                is SeriesScreenEffects.NavigateBack -> {
                    navController.popBackStack()
                }

                is SeriesScreenEffects.PlayTrailer -> {
                    val intent = Intent(Intent.ACTION_VIEW, it.trailerUrl?.toUri())
                    context.startActivity(intent)
                }
            }
        }
    }
    SeriesScreenContent(
        interactionListener = viewModel, state = state.value
    )

}

@Composable
fun SeriesScreenContent(
    interactionListener: SeriesScreenInteractionListener, state: SeriesScreenUiState
) {
    NovixScaffold(
        backgroundShapes = { NovixBackgroundShapes() },
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AppTopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(R.drawable.icon_back),
                        onClick = interactionListener::onBackClicked

                    )
                }, rightContent = {
                    TopBarClickableIcon(
                        icon = painterResource(R.drawable.icon_save),
                        onClick = interactionListener::onBackClicked
                    )
                }, modifier = Modifier
                    .systemBarsPadding()
                    .zIndex(10f)
            )

            AnimatedContent(
                state.isLoading,
                modifier = Modifier.align(Alignment.Center),
                contentAlignment = Alignment.Center

            ) {
                if (it) {
                    NovixLoadingIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(
                            state = rememberScrollState()
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(bottom = 112.dp)
                    ) {
                        SeriesHeaderSection(
                            title = state.series.title,
                            rating = state.series.rating,
                            season = stringResource(
                                R.string.seasons_count, state.series.seasonsCount
                            ),
                            airDate = state.series.releaseDate,
                            imagesUrl = state.images,
                            genres = state.series.genres,
                            onReviewClicked = {
                                interactionListener.onViewReviewsClicked(
                                    state.series.id
                                )
                            })
                        state.series.overview?.let {
                            OverviewSection(
                                onReadMore = {},
                                titleResId = R.string.overview,
                                overview = it,
                                modifier = Modifier.padding(
                                    start = 16.dp, end = 16.dp, top = 16.dp
                                )
                            )
                        }
                        CastComponent(
                            cast = state.cast,
                            onActorClicked = interactionListener::onActorClicked,
                        )
                        SeasonTap(
                            onClick = interactionListener::onSeasonNumberClicked,
                            seasonCounts = state.series.seasonsCount,
                            currentSeason = state.selectedSeason,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        AnimatedContent(state.isLoadingEpisodes) {
                            if (it) {
                                Column(
                                    modifier = Modifier
                                        .heightIn(min = 300.dp)
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center

                                ) {
                                    NovixLoadingIndicator()
                                }
                            } else {
                                EpisodesContent(
                                    episodes = state.season.episodes,
                                    seriesId = state.series.id,
                                    onEpisodeClick = interactionListener::onEpisodeClicked
                                )
                            }
                        }
                    }

                }
            }
            BottomContainer(
                modifier = Modifier.align(Alignment.BottomCenter),
                trailerUrl = state.series.trailerUrl,
                onPlayTrailerClicked = interactionListener::onPlayTrailerClicked
            )
        }

    }
}
