package com.sanaa.tvapp.presentation.screens.mediaDetails.episodeScreen

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.sanaa.tvapp.presentation.screens.login.components.NovixAnimatedSnackBarHost
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.CastSlider
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.DetailsHeaderSection
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.DetailsTopBar
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.DotSeparator
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.IconWithText
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.TrailerAndRateSection
import com.sanaa.tvapp.presentation.screens.navigation.LocalAppNavController
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute
import com.sanaa.tvapp.state.SnackData
import kotlinx.coroutines.flow.collectLatest
import com.sanaa.designsystem.R as designSystemResource

@Composable
fun EpisodeDetailsScreen(
    viewModel: EpisodeDetailsScreenViewModel = hiltViewModel()
) {

    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val navController = LocalAppNavController.current
    var snack by remember { mutableStateOf<SnackData?>(null) }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
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

                is EpisodeDetailsEffects.ShowSuccessSnackBar -> {
                }

                is EpisodeDetailsEffects.ShowErrorSnackBar -> {
                }

                EpisodeDetailsEffects.NavigateToLogin -> {
                }
            }
        }
    }
    Box(modifier = Modifier.systemBarsPadding()) {
        EpisodeDetailsScreenContent(
            interactionListener = viewModel, state = state.value
        )
        NovixAnimatedSnackBarHost(
            data = snack,
            onDismiss = { snack = null }
        )
    }
}

@Composable
private fun EpisodeDetailsScreenContent(
    interactionListener: EpisodeDetailsInteractionListener, state: EpisodeDetailsScreenUiState
) {

    NovixScaffold(
        backgroundShapes = { }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            AnimatedContent(
                targetState = state.isLoading || state.noInternetConnection,
                modifier = Modifier.align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) { shouldShowLoadingOrError ->
                if (shouldShowLoadingOrError) {
                    if (state.noInternetConnection) {
                        NetworkDisconnectionContact(
                            onRetryClick = { interactionListener.onRetryLoadDetails() },
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        LoadingIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(state = rememberScrollState())
                    ) {
                        Column {
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
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(bottom = 8.dp)
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
                                    )
                                }
                            }
                            if (state.guestOfHonor.isNotEmpty()) {
                                CastSlider(
                                    cast = state.guestOfHonor,
                                    title = stringResource(R.string.guest_of_honor),
                                    onActorCardClicked = {}
                                )
                            }
                        }
                        DetailsTopBar()
                    }
                }
            }
        }
    }
}