package com.sanaa.tvapp.presentation.screens.mediaDetails.episodeScreen

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.R
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.CastSlider
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.DetailsHeaderSection
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.DotSeparator
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.IconWithText
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.TrailerAndRateSection
import com.sanaa.tvapp.presentation.screens.navigation.LocalAppNavController
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import com.sanaa.designsystem.R as designSystemResource

@Composable
fun EpisodeDetailsScreen(viewModel: EpisodeDetailsScreenViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    EffectHandler(viewModel.effect)

    EpisodeDetailsScreenContent(
        interactionListener = viewModel,
        state = state.value,
    )
}

@Composable
private fun EffectHandler(
    effect: Flow<EpisodeDetailsEffects>,
) {
    val context = LocalContext.current
    val navController = LocalAppNavController.current

    LaunchedEffect(Unit) {
        effect.collectLatest {
            when (it) {
                is EpisodeDetailsEffects.NavigateToActorDetails -> {
                    navController.navigate(
                        ScreensRoute.ActorDetailsRoute(actorId = it.actorId)
                    )
                }

                is EpisodeDetailsEffects.PlayTrailer -> {
                    val intent = Intent(Intent.ACTION_VIEW, it.trailerUrl?.toUri())
                    context.startActivity(intent)
                }
            }
        }
    }
}

@Composable
private fun EpisodeDetailsScreenContent(
    interactionListener: EpisodeDetailsInteractionListener,
    state: EpisodeDetailsScreenUiState,
) {
    NovixScaffold(
        backgroundShapes = {},
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
    ) {
        AnimatedContent(
            targetState = Pair(state.isLoading, state.noInternetConnection),
            contentAlignment = Alignment.Center
        ) { (isLoading, noInternetConnection) ->
            when {
                isLoading -> {
                    LoadingIndicator(modifier = Modifier.fillMaxSize())
                }

                noInternetConnection -> {
                    NetworkDisconnectionContact(
                        onRetryClick = { interactionListener.onRetryLoadDetails() },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(state = rememberScrollState())
                    ) {
                        DetailsHeaderSection(
                            backgroundImageUrl = state.backgroundImageUrl,
                            title = stringResource(
                                R.string.episode_number,
                                state.episode.number
                            ) + " - ${state.episode.title}",
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    Row(
                                        modifier = Modifier.padding(bottom = 8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        state.episode.rating?.let { rating ->
                                            IconWithText(
                                                iconRes = designSystemResource.drawable.icon_star,
                                                text = rating,
                                                textColor = Theme.colors.title,
                                                contentDescription = rating,
                                                tint = Theme.colors.statusColors.yellowAccent
                                            )

                                            DotSeparator()
                                        }

                                        state.episode.airDate?.let { releaseDate ->
                                            IconWithText(
                                                text = releaseDate,
                                                iconRes = R.drawable.icon_calender,
                                                contentDescription = releaseDate,
                                                tint = Theme.colors.hint
                                            )

                                            DotSeparator()
                                        }

                                        IconWithText(
                                            text = state.episode.seasonNumber.toString(),
                                            iconRes = R.drawable.icon_seasons,
                                            contentDescription = state.episode.seasonNumber.toString(),
                                            tint = Theme.colors.hint
                                        )
                                    }
                                }

                                Text(
                                    text = state.episode.overview.orEmpty(),
                                    style = Theme.textStyle.body.small,
                                    color = Theme.colors.body
                                )

                                TrailerAndRateSection(
                                    trailerUrl = state.trailerUrl,
                                    onPlayTrailerClicked = interactionListener::onPlayTrailerClick,
                                    showRateButton = false
                                )
                            }
                        }

                        if (state.guestOfHonor.isNotEmpty()) {
                            CastSlider(
                                cast = state.guestOfHonor,
                                title = stringResource(R.string.guest_of_honor),
                                onActorCardClicked = interactionListener::onActorClick
                            )
                        }
                    }
                }
            }
        }
    }
}