package com.sanaa.presentation.screen.tvShow

import android.app.Activity
import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
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
import com.sanaa.presentation.navigation.EpisodeDetailsScreenRoute
import com.sanaa.presentation.navigation.GenreTvShowsScreenRoute
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.MediaTypeParam
import com.sanaa.presentation.navigation.ReviewsScreenRoute
import com.sanaa.presentation.screen.movieDetails.LoginPromptType
import com.sanaa.presentation.screen.movieDetails.SnackData
import com.sanaa.presentation.screen.tvShow.components.TvShowDetailContent
import com.sanaa.presentation.shared_component.BottomContainer
import com.sanaa.presentation.shared_component.NovixAnimatedSnackBarHost
import com.sanaa.presentation.shared_component.RateBottomSheet
import com.sanaa.presentation.shared_component.RequestToLoginBottomSheet
import dagger.hilt.android.EntryPointAccessors
import com.sanaa.designsystem.R as designR


@Composable
fun TvShowScreen(
    viewModel: TvShowScreenViewModel = hiltViewModel()
) {
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
                is TvShowScreenEffects.NavigateToActorScreen -> {
                    navController.navigate(
                        ActorScreenRoute(it.actorId).route()
                    )
                }

                is TvShowScreenEffects.NavigateToEpisodeDetailsScreen -> {
                    navController.navigate(
                        EpisodeDetailsScreenRoute(
                            it.tvShowId, it.seasonNumber, it.episodeNumber
                        ).route()
                    )
                }

                is TvShowScreenEffects.NavigateToReviewsScreen -> {
                    navController.navigate(
                        ReviewsScreenRoute(it.tvShowId, MediaTypeParam.TV_SHOW).route()
                    )
                }

                is TvShowScreenEffects.NavigateBack -> {
                    if (!navController.popBackStack()) {
                        (navController.context as Activity).finish()
                    }
                }

                is TvShowScreenEffects.PlayTrailer -> {
                    val intent = Intent(Intent.ACTION_VIEW, it.trailerUrl?.toUri())
                    context.startActivity(intent)
                }

                is TvShowScreenEffects.NavigateToMovieCategoriesScreen -> {
                    navController.navigate(
                        GenreTvShowsScreenRoute(it.category.id, it.category.name).route()
                    )
                }


                TvShowScreenEffects.NavigateToLogin -> {
                    // Launch authentication activity
                    launcher.launch(authApi.getLaunchIntent(context))
                }

                is TvShowScreenEffects.ShowErrorSnackBar -> TODO()
            }
        }
    }

    TvShowScreenContent(
        interactionListener = viewModel, state = state.value
    )
}

@Composable
private fun TvShowScreenContent(
    interactionListener: TvShowScreenInteractionListener,
    state: TvShowScreenUiState
) {

    val scrollState = rememberScrollState()
    val animatedColor by animateColorAsState(
        targetValue = if (scrollState.value > 200) Theme.colors.surface else Color.Transparent,
        animationSpec = tween(durationMillis = 500, easing = EaseInOut),
        label = "TopBarColorAnimation"
    )

    NovixScaffold(
        backgroundShapes = { BackgroundShapes() },
        snackBarHost = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                NovixAnimatedSnackBarHost(
                    data = state.snackBarData,
                    onDismiss = interactionListener::onSnackBarDismiss
                )
            }
        },
    ) {
        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxSize()
        ) {
            TvShowScreenTopBar(animatedColor, interactionListener)

            AnimatedContent(
                targetState = state.isLoading || state.noInternetConnection,
                modifier = Modifier.align(Alignment.Center),
                contentAlignment = Alignment.Center,
                label = "MainContentAnimation"
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
                    TvShowDetailContent(state, interactionListener, scrollState)
                }
            }

            BottomContainer(
                modifier = Modifier.align(Alignment.BottomCenter),
                trailerUrl = state.tvShow.trailerUrl,
                onPlayTrailerClicked = interactionListener::onPlayTrailerClicked,
                onSetRateClicked = interactionListener::onRateClicked
            )
        }
        if (state.showRateBottomSheet) {
            RateBottomSheet(
                isRateSelected = state.hasUserSelectedRate,
                imdbRating = state.imdbRating,
                onDismiss = interactionListener::onDismissAnyBottomSheet,
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
                onDismiss = interactionListener::onDismissAnyBottomSheet,
                onLoginButtonClick = { interactionListener.onLoginButtonClick() },
                isVisible = true,
                text = text,
                title = title
            )
        }
    }
}

@Composable
private fun TvShowScreenTopBar(
    animatedColor: Color,
    listener: TvShowScreenInteractionListener
) {
    TopBar(
        modifier = Modifier
            .background(animatedColor)
            .systemBarsPadding()
            .zIndex(10f),
        leftContent = {
            TopBarClickableIcon(
                icon = painterResource(designR.drawable.icon_back),
                onClick = listener::onBackClicked

            )
        },
        rightContent = {
            TopBarClickableIcon(
                icon = painterResource(R.drawable.icon_save),
                onClick = listener::onSaveTvShowClicked
            )
        },
    )
}