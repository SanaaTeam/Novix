package com.sanaa.presentation.screen.homeScreen

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute
import com.sanaa.api.launchAuthActivityForResult
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.api.HomeApiEntryPoint
import com.sanaa.presentation.app.navigation.LocalMainNavController
import com.sanaa.presentation.app.navigation.PlayListScreenRoute
import com.sanaa.presentation.app.navigation.TopRatedMediaScreenRoute
import com.sanaa.presentation.app.navigation.TrendingMoviesScreenRoute
import com.sanaa.presentation.app.navigation.TrendingPeopleScreenRoute
import com.sanaa.presentation.app.navigation.TrendingTvShowsScreenRoute
import com.sanaa.presentation.app.navigation.WatchingMediaHistoryScreenRoute
import com.sanaa.presentation.bottomsheet.addEditBookmark.AddBookmarkListBottomSheet
import com.sanaa.presentation.bottomsheet.saveToListBottomsheet.SaveToListBottomSheet
import com.sanaa.presentation.components.NovixAnimatedSnackBarHost
import com.sanaa.presentation.components.RefreshButton
import com.sanaa.presentation.components.RequestToLoginBottomSheet
import com.sanaa.presentation.components.cards.HomeTopBar
import com.sanaa.presentation.components.shimmerEffect.MediaSliderSectionPlaceholder
import com.sanaa.presentation.components.shimmerEffect.PopularMediaSectionPlaceholder
import com.sanaa.presentation.modifiers.fillWidthOfParent
import com.sanaa.presentation.providers.LocalThemeProvider
import com.sanaa.presentation.screen.homeScreen.section.MixedMediaSection
import com.sanaa.presentation.screen.homeScreen.section.PopularMediaSection
import com.sanaa.presentation.screen.homeScreen.section.WhatToWatchSection
import com.sanaa.presentation.screen.homeScreen.section.upcomingSection
import com.sanaa.presentation.state.MediaTypeUiState
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    val state = viewModel.state.collectAsStateWithLifecycle()

    EffectHandler(effect = viewModel.effect)

    HomeScreenContent(
        state = state.value,
        interactionListener = viewModel,
    )
}

@Composable
private fun HomeScreenContent(
    state: HomeScreenUiState,
    interactionListener: HomeScreenInteractionListener,
) {

    val upcomingMovies = state.upcomingMovies.collectAsLazyPagingItems()

    val showNoInternetScreen = (state.isNoInternetConnection
            && upcomingMovies.itemCount == 0
            && state.popularMedia.isEmpty()
            && state.topRatingMedia.isEmpty()
            && state.continueWatchingMedia.isEmpty())

    NovixScaffold(
        topBar = { HomeTopBar(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) },
        backgroundShapes = {},
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
        modifier = Modifier.systemBarsPadding()
    ) {

        AnimatedContent(
            targetState = showNoInternetScreen,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) { isDisconnected ->
            if (isDisconnected) {
                NetworkDisconnectionContact(
                    onRetryClick = interactionListener::onRetryClick,
                    modifier = Modifier.fillMaxSize(),
                    useDarkTheme = LocalThemeProvider.current

                )
            } else {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Adaptive(minSize = 140.dp),
                    contentPadding = PaddingValues(
                        start = 16.dp, end = 16.dp, bottom = 12.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    if (state.isLoadingPopular) item(span = { GridItemSpan(maxLineSpan) }) {
                        PopularMediaSectionPlaceholder(
                            modifier = Modifier.fillWidthOfParent(16.dp)
                        )
                    }
                    else item(span = { GridItemSpan(maxLineSpan) }) {
                        PopularMediaSection(
                            mediaItems = state.popularMedia, onMediaClick = {
                                interactionListener.onMediaClick(it.id, it.mediaTypeUiState)
                            }, onSaveIconClicked = {
                                interactionListener.onSaveIconClick(it)
                            }, modifier = Modifier.fillWidthOfParent(16.dp)
                        )
                    }
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        WhatToWatchSection(
                            onMoviesClicked = {
                                interactionListener.onMoviesCardClick()
                            },
                            onTvShowsClicked = {
                                interactionListener.onTvShowsCardClick()
                            },
                            onPeopleClicked = {
                                interactionListener.onPeopleCardClick()
                            },
                            modifier = Modifier
                                .fillWidthOfParent(16.dp)
                                .padding(top = 8.dp),
                            isLoading = state.isLoadingHistory
                        )
                    }
                    if (state.isLoadingTopRated) item(span = { GridItemSpan(maxLineSpan) }) {
                        MediaSliderSectionPlaceholder(
                            modifier = Modifier
                                .fillWidthOfParent(16.dp)
                                .padding(
                                    top = 24.dp
                                ),
                        )
                    }
                    else {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            MixedMediaSection(
                                headerLabel = stringResource(R.string.top_rated),
                                modifier = Modifier
                                    .fillWidthOfParent(16.dp)
                                    .padding(
                                        top = 24.dp
                                    ),
                                mediaItems = state.topRatingMedia,
                                onMediaClick = {
                                    interactionListener.onMediaClick(it.id, it.mediaTypeUiState)
                                },
                                onSaveIconClicked = {
                                    interactionListener.onSaveIconClick(it)
                                },
                                onViewAllClick = { interactionListener.onShowAllTopRatingClick() })
                        }
                    }
                    if (state.isLoadingHistory) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            MediaSliderSectionPlaceholder(
                                modifier = Modifier
                                    .fillWidthOfParent(16.dp)
                                    .padding(
                                        top = 24.dp, bottom = 24.dp
                                    ),
                            )
                        }
                    } else {
                        if (state.continueWatchingMedia.isNotEmpty()) item(span = {
                            GridItemSpan(maxLineSpan)
                        }) {
                            MixedMediaSection(
                                headerLabel = stringResource(R.string.continue_watching),
                                mediaItems = state.continueWatchingMedia,
                                onMediaClick = {
                                    interactionListener.onMediaClick(it.id, it.mediaTypeUiState)
                                },
                                onSaveIconClicked = {
                                    interactionListener.onSaveIconClick(it)
                                },
                                onViewAllClick = { interactionListener.onShowAllContinueWatchingClick() },
                                modifier = Modifier
                                    .fillWidthOfParent(16.dp)
                                    .padding(top = 24.dp),
                            )
                        }
                    }
                    upcomingSection(
                        upcomingMovies = upcomingMovies,
                        movieGenres = state.movieGenres,
                        movieSelectedGenreId = state.movieSelectedGenreId,
                        onGenreClick = interactionListener::onMovieGenreClick,
                        onSaveIconClick = interactionListener::onSaveIconClick,
                        onMovieClick = interactionListener::onMediaClick,
                        isLoading = state.isLoadingUpcoming
                    )
                }
            }
        }
        if (upcomingMovies.loadState.hasError && !showNoInternetScreen) {
            RefreshButton(onRetryClick = interactionListener::onRetryClick)
        }

    }
    SaveToListBottomSheet(
        isVisible = state.showSaveToListBottomSheet,
        mediaId = state.selectedMediaToSave?.id ?: 0,
        onDismiss = interactionListener::onDismissSaveToListBottomSheet,
        onCreateNewListClick = interactionListener::onCreateNewListClick,
    )

    AddBookmarkListBottomSheet(
        isVisible = state.showAddListBottomSheet,
        onDismiss = interactionListener::onDismissAddListBottomSheet,
    )

    RequestToLoginBottomSheet(
        isVisible = state.showLoginBottomSheet,
        onDismiss = interactionListener::onDismissLoginBottomSheet,
        onLoginButtonClick = interactionListener::onLoginButtonClick
    )
}

@Composable
private fun EffectHandler(
    effect: SharedFlow<HomeScreenEffect>,
) {

    val context = LocalContext.current
    val appContext = context.applicationContext
    val navController = LocalMainNavController.current

    val detailsApi: MediaDetailsApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, HomeApiEntryPoint::class.java)
            .detailsApi()
    }
    val authApi = remember {
        EntryPointAccessors.fromApplication(
            appContext,
            HomeApiEntryPoint::class.java
        ).authenticationApi()
    }

    val launcher: ManagedActivityResultLauncher<Intent, ActivityResult> =
        launchAuthActivityForResult()

    LaunchedEffect(Unit) {
        effect.collectLatest { effect ->
            when (effect) {
                is HomeScreenEffect.NavigateToMediaDetails -> {
                    when (effect.mediaTypeUiState) {
                        MediaTypeUiState.MOVIE -> {
                            detailsApi.launch(
                                context = navController.context,
                                startRoute = StartRoute.MOVIE,
                                id = effect.id
                            )
                        }

                        MediaTypeUiState.TV_SHOW -> {
                            detailsApi.launch(
                                context = navController.context,
                                startRoute = StartRoute.TV_SHOW,
                                id = effect.id
                            )
                        }
                    }
                }

                HomeScreenEffect.NavigateToTrendingMoviesScreen -> {
                    navController.navigate(TrendingMoviesScreenRoute)
                }

                HomeScreenEffect.NavigateToTrendingPeopleScreen -> {
                    navController.navigate(TrendingPeopleScreenRoute)
                }

                HomeScreenEffect.NavigateToTopRatingMediaScreen -> {
                    navController.navigate(TopRatedMediaScreenRoute)
                }

                HomeScreenEffect.NavigateToTrendingTvShowsScreen -> {
                    navController.navigate(TrendingTvShowsScreenRoute)
                }

                HomeScreenEffect.NavigateToWatchedMediaScreen -> {
                    navController.navigate(WatchingMediaHistoryScreenRoute)
                }

                HomeScreenEffect.NavigateToPlayListScreen -> {
                    navController.navigate(PlayListScreenRoute)
                }

                HomeScreenEffect.NavigateToLogin -> {
                    launcher.launch(authApi.getLaunchIntent(context))
                }
            }
        }
    }
}
