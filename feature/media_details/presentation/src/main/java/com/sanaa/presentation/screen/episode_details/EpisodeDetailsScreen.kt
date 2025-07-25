package com.sanaa.presentation.screen.episode_details

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
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
import com.sanaa.designsystem.design_system.component.top_bar.NovixTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.presentation.component.OverviewSection
import com.sanaa.presentation.component.RequestToLoginBottomSheet
import com.sanaa.presentation.navigation.ActorDetailsScreenRoute
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.screen.episode_details.components.GuestsOfHonorComponent
import com.sanaa.presentation.screen.series.components.BottomContainer
import com.sanaa.presentation.screen.series.components.SeriesHeaderSection
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EpisodeDetailsScreen(
    seriesId: Int,
    seasonNumber: Int,
    episodeNumber: Int,
    viewModel: EpisodeDetailsScreenViewModel = koinViewModel(parameters = {
        parametersOf(
            seriesId, seasonNumber, episodeNumber
        )
    }),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val navController = LocalNavControllerProvider.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                is EpisodeDetailsEffects.NavigateBack -> {
                    navController.popBackStack()
                }

                is EpisodeDetailsEffects.NavigateToActorDetails -> {
                    navController.navigate(
                        ActorDetailsScreenRoute(it.actorId).route()
                    )
                }

                is EpisodeDetailsEffects.PlayTrailer -> {
                    val intent = Intent(Intent.ACTION_VIEW, it.trailerUrl?.toUri())
                    context.startActivity(intent)
                }
            }
        }
    }

    EpisodeDetailsScreenContent(
        interactionListener = viewModel, state = state.value
    )

}

@Composable
private fun EpisodeDetailsScreenContent(
    interactionListener: EpisodeDetailsInteractionListener, state: EpisodeDetailsScreenUiState
) {
    NovixScaffold(
        backgroundShapes = { NovixBackgroundShapes() }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            NovixTopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(R.drawable.icon_back),
                        onClick = interactionListener::onBackClick

                    )
                }, rightContent = {
                    TopBarClickableIcon(
                        icon = painterResource(R.drawable.icon_save), onClick = {
                            interactionListener.onSavedClick(state.seriesId)
                        })
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
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 112.dp)
                                .align(Alignment.TopCenter)
                        ) {
                            SeriesHeaderSection(
                                title = stringResource(
                                    R.string.episode_number, state.episode.number
                                ) + " - ${state.episode.title}",
                                rating = state.episode.rating,
                                season = stringResource(
                                    R.string.season_number, state.episode.seasonNumber
                                ),
                                airDate = state.episode.airDate,
                                imagesUrl = state.imagesUrl,
                                genres = emptyList(),
                                showReviews = false,
                                onGenreClicked = {}
                            )

                            state.episode.overview?.let {
                                OverviewSection(
                                    onReadMore = {},
                                    titleResId = R.string.overview,
                                    overview = it,
                                    modifier = Modifier.padding(
                                        start = 16.dp, end = 16.dp, top = 16.dp
                                    )
                                )
                            }
                            if (state.guestOfHonor.isNotEmpty())
                                GuestsOfHonorComponent(
                                    guests = state.guestOfHonor,
                                    onActorClick = interactionListener::onCastClick,
                                    modifier = Modifier.padding(top = 16.dp)
                                )
                        }
                    }
                }
            }
            BottomContainer(
                trailerUrl = state.trailerUrl,
                modifier = Modifier.align(Alignment.BottomCenter),
                onPlayTrailerClicked = interactionListener::onPlayTrailerClick,
                onSetRateClicked = interactionListener::onRateClicked
            )
            // TODO USE REAL RATE BOTTOM SHEET AFTER LOGIN FEATURE DONE
            if (state.showLoginBottomSheet) {
                RequestToLoginBottomSheet(
                    isVisible = state.showLoginBottomSheet,
                    onDismiss = interactionListener::onDismissBottomSheet,
                )
            }
        }
    }
}

