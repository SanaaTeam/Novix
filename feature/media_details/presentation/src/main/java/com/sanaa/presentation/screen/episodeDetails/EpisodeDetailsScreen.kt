package com.sanaa.presentation.screen.episodeDetails

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.api.launchAuthActivityForResult
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.BackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.presentation.api.LocalThemeProvider
import com.sanaa.presentation.navigation.ActorScreenRoute
import com.sanaa.presentation.navigation.DetailsApiEntryPoint
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.screen.episodeDetails.components.GuestsOfHonorComponent
import com.sanaa.presentation.screen.movieDetails.LoginPromptType
import com.sanaa.presentation.screen.movieDetails.components.AnimatedSnackBarHost
import com.sanaa.presentation.screen.tvShow.components.TvShowHeaderSection
import com.sanaa.presentation.shared_component.BottomContainer
import com.sanaa.presentation.shared_component.OverviewSection
import com.sanaa.presentation.shared_component.RateBottomSheet
import com.sanaa.presentation.shared_component.RequestToLoginBottomSheet
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.collectLatest
import com.sanaa.designsystem.R as designR

@Composable
fun EpisodeDetailsScreen(
    viewModel: EpisodeDetailsScreenViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val navController = LocalNavControllerProvider.current

    val authApi = EntryPointAccessors.fromApplication(
        context,
        DetailsApiEntryPoint::class.java
    ).authenticationApi()

    val launcher = launchAuthActivityForResult()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                is EpisodeDetailsEffects.NavigateBack -> {
                    navController.popBackStack()
                }

                is EpisodeDetailsEffects.NavigateToActorDetails -> {
                    navController.navigate(ActorScreenRoute(it.actorId))
                }

                is EpisodeDetailsEffects.PlayTrailer -> {
                    val intent = Intent(Intent.ACTION_VIEW, it.trailerUrl?.toUri())
                    context.startActivity(intent)
                }

                EpisodeDetailsEffects.NavigateToLogin -> {
                    launcher.launch(authApi.getLaunchIntent(context))
                }
            }
        }
    }
    EpisodeDetailsScreenContent(
        interactionListener = viewModel, state = state
    )
}

@Composable
private fun EpisodeDetailsScreenContent(
    interactionListener: EpisodeDetailsInteractionListener, state: EpisodeDetailsScreenUiState,
) {
    val scrollState = rememberScrollState()
    val animatedColor by animateColorAsState(
        targetValue = if (scrollState.value > 200) Theme.colors.surface else Color.Transparent,
        animationSpec = tween(durationMillis = 500, easing = EaseInOut),
    )

    NovixScaffold(
        backgroundShapes = { BackgroundShapes() },
        snackBarHost = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                AnimatedSnackBarHost(
                    data = state.snackBarData,
                    onDismiss = interactionListener::onSnackDismissRequested
                )
            }
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            TopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(designR.drawable.icon_back),
                        onClick = interactionListener::onBackClick

                    )
                }, rightContent = {
                    TopBarClickableIcon(
                        icon = painterResource(R.drawable.icon_save), onClick = {
                            interactionListener.onSavedClick(state.tvShowId)
                        })
                }, modifier = Modifier
                    .background(animatedColor)
                    .systemBarsPadding()
                    .zIndex(10f)
            )

            AnimatedContent(
                targetState = Pair(state.isLoading, state.noInternetConnection),
                modifier = Modifier.align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) { (isLoading, noInternetConnection) ->
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            LoadingIndicator()
                        }
                    }

                    noInternetConnection -> {
                        NetworkDisconnectionContact(
                            onRetryClick = { interactionListener.onRetryLoadDetails() },
                            modifier = Modifier.fillMaxSize(),
                            useDarkTheme = LocalThemeProvider.current
                        )
                    }

                    else -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 104.dp)
                                    .align(Alignment.TopCenter)
                            ) {
                                TvShowHeaderSection(
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

                                state.episode.overview?.takeIf { it.isNotBlank() }
                                    ?.let { overviewText ->
                                        OverviewSection(
                                            onReadMore = {},
                                            titleResId = R.string.overview,
                                            overview = overviewText,
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
            }
            BottomContainer(
                trailerUrl = state.trailerUrl,
                modifier = Modifier.align(Alignment.BottomCenter),
                onPlayTrailerClicked = interactionListener::onPlayTrailerClick,
                onSetRateClicked = interactionListener::onRateClicked
            )
            if (state.showRateBottomSheet) {
                RateBottomSheet(
                    isRateSelected = state.hasUserSelectedRate,
                    imdbRating = state.imdbRating,
                    onDismiss = interactionListener::onDismissRateBottomSheet,
                    isVisible = true,
                    onSubmitButtonClick = interactionListener::onSubmitRateBottomSheet,
                    onRatingChanged = interactionListener::onRatingChanged
                )
            }
            if (state.showLoginBottomSheet) {
                val title = when (state.loginPromptType) {
                    LoginPromptType.RATE -> stringResource(R.string.rate_it)
                    LoginPromptType.BOOKMARK -> stringResource(R.string.add_to_list)
                    else -> stringResource(R.string.add_to_list)
                }

                val text = when (state.loginPromptType) {
                    LoginPromptType.RATE -> stringResource(R.string.please_login_to_rate_your_favorite_items)
                    LoginPromptType.BOOKMARK -> stringResource(R.string.request_login)
                    else -> stringResource(R.string.request_login)
                }
                RequestToLoginBottomSheet(
                    isVisible = true,
                    onDismiss = interactionListener::onDismissBottomSheet,
                    onLoginButtonClick = { interactionListener.onLoginButtonClick() },
                    text = text,
                    title = title
                )
            }
        }
    }
}

