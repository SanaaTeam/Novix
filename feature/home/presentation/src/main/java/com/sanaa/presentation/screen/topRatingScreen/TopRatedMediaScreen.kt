package com.sanaa.presentation.screen.topRatingScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.api.AuthStartRoute
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.api.HomeApiEntryPoint
import com.sanaa.presentation.app.navigation.LocalMainNavController
import com.sanaa.presentation.bottomsheet.addEditBookmark.AddBookmarkListBottomSheet
import com.sanaa.presentation.bottomsheet.saveToListBottomsheet.SaveToListBottomSheet
import com.sanaa.presentation.components.MediaTabs
import com.sanaa.presentation.components.NovixAnimatedSnackBarHost
import com.sanaa.presentation.components.PaginatedMediaListSectionContent
import com.sanaa.presentation.components.RefreshButton
import com.sanaa.presentation.components.RequestToLoginBottomSheet
import com.sanaa.presentation.state.MediaTypeUiState
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import com.sanaa.designsystem.R as designSystemR


@Composable
fun TopRatedMediaScreen(
    viewModel: TopRatedMediaScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    EffectHandler(viewModel.effect)

    TopRatedMediaScreenContent(
        state = state.value,
        interactionListener = viewModel,
    )
}


@Composable
private fun TopRatedMediaScreenContent(
    state: TopRatedMediaScreenUiState,
    interactionListener: TopRatedScreenInteractionListener,
    modifier: Modifier = Modifier,
) {
    val topRatedTvShows = state.tvShowList.collectAsLazyPagingItems()
    val topRatedMovies = state.movieList.collectAsLazyPagingItems()
    NovixScaffold(
        topBar = {
            TopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = designSystemR.drawable.icon_back),
                        onClick = interactionListener::onBackClick,
                    )
                },
                screenTitle = stringResource(R.string.top_rated),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .statusBarsPadding()
            )
        },
        snackBarHost = {
            NovixAnimatedSnackBarHost(
                data = state.snackBarData,
                onDismiss = interactionListener::onSnackBarDismiss,
                modifier = Modifier.statusBarsPadding()
            )
        },
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            MediaTabs(
                onTabClick = interactionListener::onMediaTabSelection,
                selectedTab = state.selectedMediaTypeUiState,
                modifier = Modifier.fillMaxWidth()
            )

            AnimatedContent(
                targetState = state.selectedMediaTypeUiState to state.isNoInternetConnection,
                transitionSpec = {
                    fadeIn(animationSpec = tween(150, delayMillis = 150))
                        .togetherWith(fadeOut(animationSpec = tween(150)))
                },
            ) { (selectedMediaType, isNoInternetConnection) ->
                when (selectedMediaType) {
                    MediaTypeUiState.MOVIE -> {
                        if (isNoInternetConnection && (topRatedMovies.itemCount == 0)) {
                            NetworkDisconnectionContact(onRetryClick = interactionListener::onRetryClick)
                        } else {
                            PaginatedMediaListSectionContent(
                                genres = state.movieGenres,
                                mediaList = topRatedMovies,
                                selectedGenreId = state.movieSelectedGenreId,
                                onGenreClick = interactionListener::onMovieGenreClick,
                                onMediaClick = { media ->
                                    interactionListener.onMediaClick(
                                        media.id,
                                        media.mediaTypeUiState
                                    )
                                },
                                onSaveIconClick = interactionListener::onSaveIconClick,
                            )
                            if (topRatedMovies.loadState.hasError) {
                                RefreshButton(onRetryClick = interactionListener::onRetryClick)
                            }
                        }
                    }

                    MediaTypeUiState.TV_SHOW -> {
                        if (isNoInternetConnection && (topRatedTvShows.itemCount == 0)) {
                            NetworkDisconnectionContact(onRetryClick = interactionListener::onRetryClick)
                        } else {
                            PaginatedMediaListSectionContent(
                                genres = state.tvShowGenres,
                                mediaList = topRatedTvShows,
                                selectedGenreId = state.tvShowSelectedGenreId,
                                onGenreClick = interactionListener::onTvShowGenreClick,
                                onMediaClick = { media ->
                                    interactionListener.onMediaClick(
                                        media.id,
                                        media.mediaTypeUiState
                                    )
                                },
                                onSaveIconClick = interactionListener::onSaveIconClick,
                            )
                            if (topRatedTvShows.loadState.hasError) {
                                RefreshButton(onRetryClick = interactionListener::onRetryClick)
                            }
                        }
                    }
                }
            }
        }
    }
    SaveToListBottomSheet(
        isVisible = state.showSaveToListBottomSheet,
        mediaId = state.selectedMediaToSaveId ?: 0,
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
    effect: SharedFlow<TopRatedMediaScreenEffect>,
) {
    val context = LocalContext.current
    val appContext = context.applicationContext
    val navController = LocalMainNavController.current

    val detailsApi: MediaDetailsApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, HomeApiEntryPoint::class.java)
            .detailsApi()
    }

    val authApi = EntryPointAccessors.fromApplication(
        context,
        HomeApiEntryPoint::class.java
    ).authenticationApi()


    LaunchedEffect(Unit) {
        effect.collectLatest { effect ->
            when (effect) {
                is TopRatedMediaScreenEffect.NavigateToMediaDetails -> {
                    if (effect.mediaTypeUiState == MediaTypeUiState.MOVIE) {
                        detailsApi.launch(
                            context = navController.context,
                            id = effect.id,
                            startRoute = StartRoute.MOVIE
                        )
                    } else if (effect.mediaTypeUiState == MediaTypeUiState.TV_SHOW) {
                        detailsApi.launch(
                            context = navController.context,
                            id = effect.id,
                            startRoute = StartRoute.TV_SHOW
                        )
                    }
                }

                is TopRatedMediaScreenEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                TopRatedMediaScreenEffect.NavigateToLogin -> {
                    authApi.launch(context, AuthStartRoute.Login)
                }
            }
        }
    }
}

