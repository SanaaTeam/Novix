package com.sanaa.presentation.screen.movieDetails

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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.api.launchAuthActivityForResult
import com.sanaa.designsystem.design_system.component.animation.FadeSlideInVertically
import com.sanaa.designsystem.design_system.component.animation.FadeSlideOutVertically
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.BackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.presentation.api.LocalThemeProvider
import com.sanaa.presentation.bottomsheets.addEditBookmark.AddBookmarkListBottomSheet
import com.sanaa.presentation.bottomsheets.saveToListBottomsheet.SaveToListBottomSheet
import com.sanaa.presentation.model.MediaTypeUiModel
import com.sanaa.presentation.navigation.ActorScreenRoute
import com.sanaa.presentation.navigation.DetailsApiEntryPoint
import com.sanaa.presentation.navigation.GenreMovieScreenRoute
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.MovieDetailsScreenRoute
import com.sanaa.presentation.navigation.ReviewsScreenRoute
import com.sanaa.presentation.screen.movieDetails.MovieDetailsUiEffect.NavigateBack
import com.sanaa.presentation.screen.movieDetails.MovieDetailsUiEffect.NavigateToLogin
import com.sanaa.presentation.screen.movieDetails.MovieDetailsUiEffect.NavigateToMovieCategoriesScreen
import com.sanaa.presentation.screen.movieDetails.MovieDetailsUiEffect.NavigateToReviewsScreen
import com.sanaa.presentation.screen.movieDetails.components.AnimatedSnackBarHost
import com.sanaa.presentation.screen.movieDetails.components.MovieDetailsGridContent
import com.sanaa.presentation.screen.movieDetails.components.MovieDetailsTopBar
import com.sanaa.presentation.shared_component.BottomContainer
import com.sanaa.presentation.shared_component.RateBottomSheet
import com.sanaa.presentation.shared_component.RequestToLoginBottomSheet
import com.sanaa.presentation.util.DateTimeUtils.getCurrentLocale
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MovieDetailsEffectsHandler(effects = viewModel.effect)

    MovieDetailsScreenContent(
        state = state,
        interactionListener = viewModel
    )
}

@Composable
private fun MovieDetailsEffectsHandler(
    effects: SharedFlow<MovieDetailsUiEffect>,
) {
    val navController = LocalNavControllerProvider.current
    val context = LocalContext.current
    val currentContext by rememberUpdatedState(context)
    val currentNavController by rememberUpdatedState(navController)

    val authApi = EntryPointAccessors.fromApplication(
        context,
        DetailsApiEntryPoint::class.java
    ).authenticationApi()

    val launcher = launchAuthActivityForResult()

    LaunchedEffect(Unit) {
        effects.collectLatest { effect ->
            when (effect) {
                is MovieDetailsUiEffect.OpenTrailer -> {
                    effect.url?.toUri()?.let { uri ->
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        currentContext.startActivity(intent)
                    }
                }

                is NavigateBack -> {
                    if (!currentNavController.popBackStack()) {
                        (currentNavController.context as? Activity)?.finish()
                    }
                }

                is NavigateToReviewsScreen -> {
                    currentNavController.navigate(
                        ReviewsScreenRoute(effect.movieId, MediaTypeUiModel.MOVIE)
                    )
                }

                is MovieDetailsUiEffect.NavigateToAnotherMovieDetails -> {
                    currentNavController.navigate(MovieDetailsScreenRoute(effect.movieId))
                }

                is MovieDetailsUiEffect.NavigateToActorScreen -> {
                    currentNavController.navigate(ActorScreenRoute(effect.actorId))
                }

                is NavigateToMovieCategoriesScreen -> {
                    currentNavController.navigate(
                        GenreMovieScreenRoute(effect.categoryId, effect.categoryName)
                    )
                }

                NavigateToLogin -> {
                    launcher.launch(authApi.getLaunchIntent(context))
                }
            }
        }
    }
}


@Composable
private fun MovieDetailsScreenContent(
    state: MovieDetailsUiState,
    interactionListener: MovieDetailsScreenInteractionListener,
) {
    val pagedSimilarMovies = state.similarMovies.collectAsLazyPagingItems()
    val context = LocalContext.current
    val locale = remember { getCurrentLocale(context) }
    val lazyState = rememberLazyGridState()

    var shouldShowBackground by remember { mutableStateOf(false) }
    val animatedColor by animateColorAsState(
        targetValue = if (shouldShowBackground) Theme.colors.surface else Color.Transparent,
        animationSpec = tween(durationMillis = 500, easing = EaseInOut),
    )

    LaunchedEffect(lazyState) {
        snapshotFlow {
            if (lazyState.firstVisibleItemIndex == 0) {
                lazyState.firstVisibleItemScrollOffset
            } else {
                Int.MAX_VALUE
            }
        }.collect { totalScrollPosition ->
            shouldShowBackground = totalScrollPosition > 200
        }
    }

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
                .navigationBarsPadding()
                .fillMaxSize()
        ) {
            MovieDetailsTopBar(
                interactionListener = interactionListener,
                movie = state.movieDetails,
                modifier = Modifier.background(color = animatedColor)
            )
            AnimatedContent(
                targetState = state.isLoading || state.noInternetConnection,
                modifier = Modifier.align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                if (it) {
                    if (state.noInternetConnection) {
                        NetworkDisconnectionContact(
                            onRetryClick = { interactionListener.onRetryLoadDetails() },
                            modifier = Modifier.fillMaxSize(),
                            useDarkTheme = LocalThemeProvider.current
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            LoadingIndicator()
                        }
                    }
                } else {
                    MovieDetailsGridContent(
                        state = state,
                        pagedSimilarMovies = pagedSimilarMovies,
                        locale = locale,
                        interactionListener = interactionListener,
                        lazyState = lazyState,
                    )
                }
            }
            BottomContainer(
                onPlayTrailerClicked = { interactionListener.onWatchTrailerClick() },
                trailerUrl = state.movieDetails.trailerUrl,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding(),
                onSetRateClicked = { interactionListener.onRateMovieClick() }
            )
        }
    }

    AnimatedVisibility(
        visible = state.showSaveToListBottomSheet,
        enter = FadeSlideInVertically,
        exit = FadeSlideOutVertically
    ) {
        SaveToListBottomSheet(
            isVisible = state.showSaveToListBottomSheet,
            mediaId = state.selectedMediaId ?: 0,
            onDismiss = interactionListener::onDismissSaveToListBottomSheet,
            onCreateNewListClick = interactionListener::onCreateNewListClick,
        )
    }

    AnimatedVisibility(
        visible = state.showAddListBottomSheet && state.selectedMediaId != null,
        enter = FadeSlideInVertically,
        exit = FadeSlideOutVertically
    ) {
        AddBookmarkListBottomSheet(
            isVisible = true,
            onDismiss = interactionListener::onDismissAddListBottomSheet,
        )
    }

    AnimatedVisibility(
        visible = state.showRateBottomSheet,
        enter = FadeSlideInVertically,
        exit = FadeSlideOutVertically
    ) {
        RateBottomSheet(
            isRateSelected = state.hasUserSelectedRate,
            imdbRating = state.imdbRating,
            onDismiss = interactionListener::onDismissRateBottomSheet,
            isVisible = true,
            onSubmitButtonClick = interactionListener::onSubmitRateBottomSheet,
            onRatingChanged = interactionListener::onRatingChanged
        )
    }

    AnimatedVisibility(
        visible = state.showLoginBottomSheet,
        enter = FadeSlideInVertically,
        exit = FadeSlideOutVertically
    ) {
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
            onDismiss = { interactionListener.onDismissLoginBottomSheet() },
            isVisible = true,
            title = title,
            text = text,
            onLoginButtonClick = {
                interactionListener.onLoginButtonClick()
            }
        )
    }
}

