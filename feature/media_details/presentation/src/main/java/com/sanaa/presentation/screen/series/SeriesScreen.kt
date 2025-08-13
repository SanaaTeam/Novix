package com.sanaa.presentation.screen.series

import android.app.Activity
import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
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
import com.sanaa.presentation.navigation.ActorDetailsScreenRoute
import com.sanaa.presentation.navigation.DetailsApiEntryPoint
import com.sanaa.presentation.navigation.EpisodeDetailsScreenRoute
import com.sanaa.presentation.navigation.GenreTvShowsScreenRoute
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.MediaTypeParam
import com.sanaa.presentation.navigation.ReviewsScreenRoute
import com.sanaa.presentation.screen.movieDetails.LoginPromptType
import com.sanaa.presentation.screen.movieDetails.SnackData
import com.sanaa.presentation.screen.series.components.CastComponent
import com.sanaa.presentation.screen.series.components.EpisodesContent
import com.sanaa.presentation.screen.series.components.SeasonTab
import com.sanaa.presentation.screen.series.components.SeriesHeaderSection
import com.sanaa.presentation.shared_component.BottomContainer
import com.sanaa.presentation.shared_component.NovixAnimatedSnackBarHost
import com.sanaa.presentation.shared_component.OverviewSection
import com.sanaa.presentation.shared_component.RateBottomSheet
import com.sanaa.presentation.shared_component.RequestToLoginBottomSheet
import dagger.hilt.android.EntryPointAccessors
import com.sanaa.designsystem.R as designR

@Composable
fun SeriesScreen(
    viewModel: SeriesViewModel = hiltViewModel()
) {
    val submitRatingSuccessMsg = stringResource(R.string.submit_rating_successfully)
    val submitRatingFailedMsg = stringResource(R.string.submit_rating_failed)
    var snack by remember { mutableStateOf<SnackData?>(null) }
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val navController = LocalNavControllerProvider.current

    val authApi = EntryPointAccessors.fromApplication(
        context,
        DetailsApiEntryPoint::class.java
    ).authenticationApi()

    val launcher = launchAuthActivityForResult()


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
                        ReviewsScreenRoute(it.seriesId, MediaTypeParam.SERIES).route()
                    )
                }

                is SeriesScreenEffects.NavigateBack -> {
                    if (!navController.popBackStack()) {
                        (navController.context as Activity).finish()
                    }
                }

                is SeriesScreenEffects.PlayTrailer -> {
                    val intent = Intent(Intent.ACTION_VIEW, it.trailerUrl?.toUri())
                    context.startActivity(intent)
                }

                is SeriesScreenEffects.NavigateToMovieCategoriesScreen -> {
                    navController.navigate(
                        GenreTvShowsScreenRoute(it.category.id, it.category.name).route()
                    )
                }

                is SeriesScreenEffects.ShowSuccessSnackBar -> {
                    snack = SnackData(message = submitRatingSuccessMsg, isError = false)
                }

                is SeriesScreenEffects.ShowErrorSnackBar -> {
                    snack = SnackData(submitRatingFailedMsg, isError = true)
                }

                SeriesScreenEffects.NavigateToLogin -> {
                    // Launch authentication activity
                    launcher.launch(authApi.getLaunchIntent(context))
                }
            }
        }
    }

    Box {
        SeriesScreenContent(
            interactionListener = viewModel, state = state.value
        )

        NovixAnimatedSnackBarHost(
            data = snack,
            onDismiss = { snack = null }
        )
    }
}

@Composable
fun SeriesScreenContent(
    interactionListener: SeriesScreenInteractionListener, state: SeriesScreenUiState
) {

    val scrollState = rememberScrollState()
    val animatedColor by animateColorAsState(
        targetValue = if (scrollState.value > 200) Theme.colors.surface else Color.Transparent,
        animationSpec = tween(durationMillis = 500, easing = EaseInOut),
    )

    NovixScaffold(
        backgroundShapes = { BackgroundShapes() },
    ) {
        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxSize()
        ) {
            TopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(designR.drawable.icon_back),
                        onClick = interactionListener::onBackClicked

                    )
                }, rightContent = {
                    TopBarClickableIcon(
                        icon = painterResource(R.drawable.icon_save),
                        onClick = interactionListener::onSaveSeriesClicked
                    )
                }, modifier = Modifier
                    .background(animatedColor)
                    .systemBarsPadding()
                    .zIndex(10f)
            )

            AnimatedContent(
                targetState = state.isLoading || state.noInternetConnection,
                modifier = Modifier.align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) { shouldShowLoadingOrError ->
                if (shouldShowLoadingOrError) {
                    if (state.noInternetConnection) {
                        NetworkDisconnectionContact(
                            onRetryClick = { interactionListener.onRetryLoadDetails() },
                            modifier = Modifier.fillMaxSize(),
                            useDarkTheme = LocalThemeProvider.current
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
                                state = scrollState
                            )
                    ) {
                        Column(
                            modifier = Modifier.padding(bottom = 104.dp)
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
                                },
                                onGenreClicked = { genre ->
                                    interactionListener.onGenreClicked(
                                        genre
                                    )
                                })

                            if (state.series.overview.isNotEmpty()) {
                                OverviewSection(
                                    onReadMore = {},
                                    titleResId = R.string.overview,
                                    overview = state.series.overview,
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
                                seasonCounts = state.series.seasonsCount,
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
                                        seriesId = state.series.id,
                                        onEpisodeClick = interactionListener::onEpisodeClicked
                                    )
                                }
                            }
                        }
                    }
                }
            }
            BottomContainer(
                modifier = Modifier.align(Alignment.BottomCenter),
                trailerUrl = state.series.trailerUrl,
                showRatingButton = !state.hasUserSelectedRate,
                onPlayTrailerClicked = interactionListener::onPlayTrailerClicked,
                onSetRateClicked = interactionListener::onRateClicked
            )
        }
        if (state.showRateBottomSheet) {
            val localRating = remember(state.imdbRating) { androidx.compose.runtime.mutableStateOf(state.imdbRating) }
            RateBottomSheet(
                isRateSelected = localRating.value > 0,
                imdbRating = localRating.value,
                onDismiss = interactionListener::onDismissAnyBottomSheet,
                isVisible = true,
                onSubmitButtonClick = {
                    interactionListener.onRatingChanged(localRating.value)
                    interactionListener.onSubmitRateBottomSheet()
                },
                onRatingChanged = { new -> localRating.value = new }
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
                onDismiss = interactionListener::onDismissAnyBottomSheet,
                onLoginButtonClick = { interactionListener.onLoginButtonClick() },
                isVisible = true,
                text = text,
                title = title
            )
        }
    }
}