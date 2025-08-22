package com.sanaa.presentation.screen.tvShow

import android.app.Activity
import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.sanaa.api.AuthStartRoute
import com.sanaa.designsystem.design_system.component.animation.FadeSlideInVertically
import com.sanaa.designsystem.design_system.component.animation.FadeSlideOutVertically
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.BackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.presentation.api.LocalThemeProvider
import com.sanaa.presentation.model.MediaTypeUiModel
import com.sanaa.presentation.navigation.ActorScreenRoute
import com.sanaa.presentation.navigation.DetailsApiEntryPoint
import com.sanaa.presentation.navigation.EpisodeDetailsScreenRoute
import com.sanaa.presentation.navigation.GenreTvShowsScreenRoute
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.ReviewsScreenRoute
import com.sanaa.presentation.screen.tvShow.components.TvShowDetailContent
import com.sanaa.presentation.shared_component.BottomContainer
import com.sanaa.presentation.shared_component.NovixAnimatedSnackBarHost
import com.sanaa.presentation.shared_component.RateBottomSheet
import com.sanaa.presentation.shared_component.RequestToLoginBottomSheet
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import com.sanaa.designsystem.R as designR


@Composable
fun TvShowScreen(
    viewModel: TvShowScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    HandleTvShowScreenEffects(
        effect = viewModel.effect,
    )

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
            NovixAnimatedSnackBarHost(
                data = state.snackBarData,
                onDismiss = interactionListener::onSnackBarDismiss,
                modifier = Modifier.statusBarsPadding()
            )
        },
    ) {
        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxSize()
        ) {
            TvShowScreenTopBar(animatedColor, interactionListener)

            AnimatedContent(
                targetState = Pair(state.isLoading, state.noInternetConnection),
                modifier = Modifier.align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) { (isLoading, noInternetConnection) ->
                when {
                    isLoading -> {
                        LoadingIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    noInternetConnection -> {
                        NetworkDisconnectionContact(
                            onRetryClick = { interactionListener.onRetryLoadDetails() },
                            modifier = Modifier.fillMaxSize(),
                            useDarkTheme = LocalThemeProvider.current
                        )

                    }

                    else -> {
                        TvShowDetailContent(state, interactionListener, scrollState)
                    }

                }
            }
            AnimatedVisibility(
                !state.isLoading,
                modifier = Modifier.align(Alignment.BottomCenter),
                enter = FadeSlideInVertically,
                exit = FadeSlideOutVertically
            ) {
                BottomContainer(
                    isFilledStarIcon = state.isRatingSubmitted,
                    modifier = Modifier,
                    trailerUrl = state.tvShow.trailerUrl,
                    isRateButtonEnabled = state.isError.not(),
                    onPlayTrailerClicked = interactionListener::onPlayTrailerClicked,
                    onSetRateClicked = interactionListener::onRateClicked
                )
            }
            RateBottomSheet(
                isRateSelected = state.hasUserSelectedRate,
                imdbRating = state.filledStarsCount,
                onDismiss = interactionListener::onDismissRateBottomSheet,
                isVisible = state.showRateBottomSheet,
                onSubmitButtonClick = interactionListener::onSubmitRateBottomSheet,
                onRatingChanged = interactionListener::onRatingChanged
            )

            RequestToLoginBottomSheet(
                onDismiss = interactionListener::onDismissLoginBottomSheet,
                onLoginButtonClick = interactionListener::onLoginButtonClick,
                isVisible = state.showLoginBottomSheet,
                text = stringResource(R.string.please_login_to_rate_your_favorite_items),
                title = stringResource(R.string.rate_it)
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
    )
}

@Composable
private fun HandleTvShowScreenEffects(
    effect: Flow<TvShowScreenEffects>,
) {
    val context = LocalContext.current
    val navController = LocalNavControllerProvider.current

    val authApi = EntryPointAccessors.fromApplication(
        context,
        DetailsApiEntryPoint::class.java
    ).authenticationApi()

    LaunchedEffect(Unit) {
        effect.collectLatest { effect ->
            when (effect) {
                is TvShowScreenEffects.NavigateToActorScreen -> {
                    navController.navigate(ActorScreenRoute(effect.actorId).copy())
                }

                is TvShowScreenEffects.NavigateToEpisodeDetailsScreen -> {
                    navController.navigate(
                        EpisodeDetailsScreenRoute(
                            effect.tvShowId, effect.seasonNumber, effect.episodeNumber
                        ).copy()
                    )
                }

                is TvShowScreenEffects.NavigateToReviewsScreen -> {
                    navController.navigate(
                        ReviewsScreenRoute(effect.tvShowId, MediaTypeUiModel.TV_SHOW).copy()
                    )
                }

                is TvShowScreenEffects.NavigateBack -> {
                    if (!navController.popBackStack()) {
                        (navController.context as Activity).finish()
                    }
                }

                is TvShowScreenEffects.PlayTrailer -> {
                    val intent = Intent(Intent.ACTION_VIEW, effect.trailerUrl?.toUri())
                    context.startActivity(intent)
                }

                is TvShowScreenEffects.NavigateToMovieCategoriesScreen -> {
                    navController.navigate(
                        GenreTvShowsScreenRoute(effect.category.id, effect.category.name).copy()
                    )
                }

                TvShowScreenEffects.NavigateToLogin -> {
                    authApi.launch(context, AuthStartRoute.Login)
                }
            }
        }
    }
}