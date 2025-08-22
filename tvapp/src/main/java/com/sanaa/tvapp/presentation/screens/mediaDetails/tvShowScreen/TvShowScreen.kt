package com.sanaa.tvapp.presentation.screens.mediaDetails.tvShowScreen

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.R
import com.sanaa.tvapp.presentation.components.LoginDialog
import com.sanaa.tvapp.presentation.components.RateDialog
import com.sanaa.tvapp.presentation.screens.login.LoginActivity
import com.sanaa.tvapp.presentation.screens.login.components.NovixAnimatedSnackBarHost
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.CastSlider
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.DetailsHeaderSection
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.DotSeparator
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.GenresRow
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.IconWithText
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.TrailerAndRateSection
import com.sanaa.tvapp.presentation.screens.mediaDetails.tvShowScreen.components.EpisodesContent
import com.sanaa.tvapp.presentation.screens.mediaDetails.tvShowScreen.components.SeasonTab
import com.sanaa.tvapp.presentation.screens.navigation.LocalAppNavController
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.ActorDetailsRoute
import com.sanaa.tvapp.state.SnackData


@Composable
fun TvShowScreen(
    modifier: Modifier = Modifier,
    viewModel: ShowDetailsScreenViewModel = hiltViewModel(),
) {
    val submitRatingSuccessMsg = stringResource(R.string.submit_rating_successfully)
    val submitRatingFailedMsg = stringResource(R.string.submit_rating_failed)
    var snack by remember { mutableStateOf<SnackData?>(null) }
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val navController = LocalAppNavController.current

    val loginLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            (context as? Activity)?.recreate()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
                is TvShowDetailsScreenEffects.NavigateToActorScreen -> {
                    navController.navigate(ActorDetailsRoute(it.actorId))
                }

                is TvShowDetailsScreenEffects.NavigateToEpisodeDetailsScreen -> {
                    navController.navigate(
                        ScreensRoute.EpisodeDetailsRoute(
                            seriesId = it.seriesId,
                            seasonNumber = it.seasonNumber,
                            episodeNumber = it.episodeNumber
                        )
                    )
                }

                TvShowDetailsScreenEffects.NavigateToLogin -> {
                    val intent = Intent(context, LoginActivity::class.java)
                    loginLauncher.launch(intent)
                }

                is TvShowDetailsScreenEffects.NavigateToMovieCategoriesScreen -> {
                }


                is TvShowDetailsScreenEffects.PlayTrailer -> {
                    val intent = Intent(Intent.ACTION_VIEW, it.trailerUrl?.toUri())
                    context.startActivity(intent)
                }

                TvShowDetailsScreenEffects.ShowErrorSnackBar -> {
                    snack = SnackData(message = submitRatingFailedMsg, isError = true)
                }

                TvShowDetailsScreenEffects.ShowSuccessSnackBar -> {
                    snack = SnackData(message = submitRatingSuccessMsg, isError = false)
                }
            }
        }
    }

    NovixTheme(isDarkMode = true) {

        Box(modifier = modifier.systemBarsPadding()) {

            TvShowScreenContent(
                state = state.value,
                interactionListener = viewModel
            )

            NovixAnimatedSnackBarHost(
                data = snack,
                onDismiss = { snack = null }
            )
        }
    }
}

@Composable
fun TvShowScreenContent(
    state: TvShowDetailsScreenUiState,
    interactionListener: TvShowScreenInteractionListener,
) {

    NovixScaffold(
        backgroundShapes = { },
    ) {
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
                            .verticalScroll(
                                state = rememberScrollState()
                            )
                    ) {
                        Column {
                            DetailsHeaderSection(
                                backgroundImageUrl = state.backgroundImageUrl,
                                title = state.tvShows.title,
                            )
                            {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {

                                    GenresRow(
                                        genres = state.tvShows.genres,
                                        onGenreClicked = interactionListener::onGenreClicked
                                    )
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
                                            state.tvShows.rating.let {
                                                IconWithText(
                                                    iconRes = R.drawable.icon_star,
                                                    text = state.tvShows.rating,
                                                    textColor = Theme.colors.title,
                                                    contentDescription = state.tvShows.rating,
                                                    tint = Theme.colors.statusColors.yellowAccent
                                                )

                                                DotSeparator()
                                            }
                                            state.tvShows.releaseDate.let { releaseDate ->
                                                IconWithText(
                                                    text = releaseDate,
                                                    iconRes = R.drawable.icon_calender,
                                                    contentDescription = releaseDate,
                                                    tint = Theme.colors.hint
                                                )

                                                DotSeparator()
                                            }
                                            IconWithText(
                                                text = state.tvShows.seasonsCount.toString(),
                                                iconRes = R.drawable.icon_seasons,
                                                contentDescription = state.tvShows.seasonsCount.toString(),
                                                tint = Theme.colors.hint
                                            )
                                        }
                                    }
                                    Text(
                                        text = state.tvShows.overview,
                                        style = Theme.textStyle.body.small,
                                        color = Theme.colors.body
                                    )

                                    TrailerAndRateSection(
                                        trailerUrl = state.tvShows.trailerUrl,
                                        onPlayTrailerClicked = interactionListener::onPlayTrailerClicked,
                                        onRateClicked = interactionListener::onRateMovieClick
                                    )
                                }
                            }

                            if (state.cast.isNotEmpty()) {
                                CastSlider(
                                    cast = state.cast,
                                    onActorCardClicked = interactionListener::onActorClicked
                                )
                            }

                            SeasonTab(
                                onClick = interactionListener::onSeasonNumberClicked,
                                seasonCounts = state.tvShows.seasonsCount,
                                currentSeason = state.selectedSeason,
                            )
                            AnimatedContent(state.isLoadingEpisodes) { isLoadingEpisodes ->
                                if (isLoadingEpisodes) {
                                    Box(
                                        modifier = Modifier
                                            .heightIn(min = 300.dp)
                                            .fillMaxWidth(),
                                        contentAlignment = Alignment.Center

                                    ) {
                                        LoadingIndicator()
                                    }
                                } else {
                                    EpisodesContent(
                                        episodes = state.season.episodes,
                                        seriesId = state.tvShows.id,
                                        onEpisodeClick = interactionListener::onEpisodeClicked
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (state.showRateDialog) {
                RateDialog(
                    currentRating = state.rating,
                    onRatingChanged = interactionListener::onRatingChange,
                    onDismissRequest = interactionListener::onDismissRateDialog,
                    onSubmitRating = interactionListener::onSummitRateClick
                )
            }
            if (state.showLoginDialog && !state.isUserLoggedIn) {
                LoginDialog(
                    onDismissRequest = interactionListener::onDismissLoginBottomSheet,
                    onLoginClicked = interactionListener::onLoginButtonClick
                )
            }
        }
    }
}