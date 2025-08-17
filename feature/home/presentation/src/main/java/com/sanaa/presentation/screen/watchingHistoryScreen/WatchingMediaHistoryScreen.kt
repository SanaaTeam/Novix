package com.sanaa.presentation.screen.watchingHistoryScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
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
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute
import com.sanaa.api.launchAuthActivityForResult
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.api.HomeApiEntryPoint
import com.sanaa.presentation.app.navigation.LocalMainNavController
import com.sanaa.presentation.bottomsheet.addEditBookmark.AddBookmarkListBottomSheet
import com.sanaa.presentation.bottomsheet.saveToListBottomsheet.SaveToListBottomSheet
import com.sanaa.presentation.components.MediaListSectionContent
import com.sanaa.presentation.components.MediaTabs
import com.sanaa.presentation.components.NovixAnimatedSnackBarHost
import com.sanaa.presentation.components.RefreshButton
import com.sanaa.presentation.components.RequestToLoginBottomSheet
import com.sanaa.presentation.state.MediaTypeUiState
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WatchingMediaHistoryScreen(
    viewModel: WatchingMediaHistoryScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    EffectHandler(viewModel.effect)

    WatchingMediaHistoryScreenContent(
        state = state.value,
        interactionListener = viewModel,
    )
}

@Composable
private fun WatchingMediaHistoryScreenContent(
    state: WatchingMediaHistoryScreenUiState,
    interactionListener: WatchingMediaHistoryScreenInteractionListener,
) {

    NovixScaffold(
        topBar = {
            TopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = R.drawable.icon_back),
                        onClick = interactionListener::onBackClick
                    )
                },
                screenTitle = stringResource(R.string.watching_history),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )
        },
        snackBarHost = {
            NovixAnimatedSnackBarHost(
                data = state.snackBarData,
                onDismiss = interactionListener::onSnackBarDismiss
            )
        },
        modifier = Modifier.systemBarsPadding(),
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
                targetState = state.selectedMediaTypeUiState,
                transitionSpec = {
                    fadeIn(animationSpec = tween(150, delayMillis = 150))
                        .togetherWith(fadeOut(animationSpec = tween(150)))
                },
                modifier = Modifier.fillMaxSize()
            ) { selectedMediaType ->
                when (selectedMediaType) {

                    MediaTypeUiState.MOVIE -> {
                        MediaListSectionContent(
                            genres = state.movieGenres,
                            mediaList = state.movieList,
                            selectedGenreId = state.movieSelectedGenreId,
                            onGenreClick = interactionListener::onMovieGenreClick,
                            onMediaClick = { media ->
                                interactionListener.onMediaClick(media.id, media.mediaTypeUiState)
                            },
                            onSaveIconClick = interactionListener::onSaveIconClick,
                            modifier = Modifier.fillMaxSize()
                        )
                        val selectedMedia = state.selectedMediaToSave
                        if (state.userIsLoggedIn) {
                            if (state.showSaveToListBottomSheet && selectedMedia != null) {
                                SaveToListBottomSheet(
                                    isVisible = true,
                                    mediaId = selectedMedia.id.toLong(),
                                    onDismiss = interactionListener::onDismissSaveToListBottomSheet,
                                    onCreateNewListClick = interactionListener::onCreateNewListClick
                                )
                            }
                            if (state.showAddListBottomSheet && selectedMedia != null) {
                                AddBookmarkListBottomSheet(
                                    isVisible = true,
                                    onDismiss = interactionListener::onDismissAddListBottomSheet,
                                    mediaId = selectedMedia.id
                                )
                            }
                        } else {
                            RequestToLoginBottomSheet(
                                isVisible = state.showLoginBottomSheet,
                                onDismiss = interactionListener::onDismissLoginBottomSheet,
                                onLoginButtonClick = interactionListener::onLoginButtonClick
                            )
                        }
                    }

                    MediaTypeUiState.TV_SHOW -> {
                        MediaListSectionContent(
                            genres = state.tvShowGenres,
                            mediaList = state.tvShowList,
                            selectedGenreId = state.tvShowSelectedGenreId,
                            onGenreClick = interactionListener::onTvShowGenreClick,
                            onMediaClick = { media ->
                                interactionListener.onMediaClick(media.id, media.mediaTypeUiState)
                            },
                            onSaveIconClick = interactionListener::onSaveIconClick,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                if (state.showRefreshButton) {
                    RefreshButton(onRetryClick = interactionListener::onRetryClick)
                }
            }
        }
    }
}

@Composable
private fun EffectHandler(
    effect: SharedFlow<WatchingMediaHistoryScreenEffect>,
) {
    val navController = LocalMainNavController.current
    val appContext = LocalContext.current.applicationContext
    val authApi = EntryPointAccessors.fromApplication(
        appContext,
        HomeApiEntryPoint::class.java
    ).authenticationApi()
    val launcher = launchAuthActivityForResult()

    val detailsApi: MediaDetailsApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, HomeApiEntryPoint::class.java)
            .detailsApi()
    }

    LaunchedEffect(Unit) {
        effect.collectLatest { effect ->
            when (effect) {
                is WatchingMediaHistoryScreenEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                is WatchingMediaHistoryScreenEffect.NavigateToLogin -> {
                    launcher.launch(authApi.getLaunchIntent(appContext))
                }

                is WatchingMediaHistoryScreenEffect.NavigateToMediaDetails -> {
                    when (effect.mediaTypeUiState) {
                        MediaTypeUiState.MOVIE -> {
                            detailsApi.launch(
                                context = navController.context,
                                id = effect.id,
                                startRoute = StartRoute.MOVIE
                            )
                        }

                        MediaTypeUiState.TV_SHOW -> {
                            detailsApi.launch(
                                context = navController.context,
                                id = effect.id,
                                startRoute = StartRoute.TV_SHOW
                            )
                        }
                    }
                }
            }
        }
    }
}