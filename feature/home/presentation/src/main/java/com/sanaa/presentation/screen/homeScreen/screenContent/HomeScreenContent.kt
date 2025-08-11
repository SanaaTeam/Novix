package com.sanaa.presentation.screen.homeScreen.screenContent

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.api.AuthenticationApi
import com.sanaa.api.launchAuthActivityForResult
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.bottomsheet.addEditBookmark.AddBookmarkListBottomSheet
import com.sanaa.presentation.bottomsheet.saveToListBottomsheet.SaveToListBottomSheet
import com.sanaa.presentation.components.RefreshButton
import com.sanaa.presentation.components.RequestToLoginBottomSheet
import com.sanaa.presentation.components.SnackData
import com.sanaa.presentation.components.cards.HomeTopBar
import com.sanaa.presentation.components.shimmerEffect.MediaSliderSectionPlaceholder
import com.sanaa.presentation.components.shimmerEffect.PopularMediaSectionPlaceholder
import com.sanaa.presentation.modifiers.fillWidthOfParent
import com.sanaa.presentation.providers.LocalThemeProvider
import com.sanaa.presentation.screen.homeScreen.HomeScreenInteractionListener
import com.sanaa.presentation.screen.homeScreen.HomeScreenUiState
import com.sanaa.presentation.screen.homeScreen.section.MixedMediaSection
import com.sanaa.presentation.screen.homeScreen.section.PopularMediaSection
import com.sanaa.presentation.screen.homeScreen.section.WhatToWatchSection
import com.sanaa.presentation.screen.homeScreen.section.upcomingSection

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun HomeScreenContent(
    state: HomeScreenUiState,
    interactionListener: HomeScreenInteractionListener,
    authApi: AuthenticationApi,
) {

    val upcomingMovies = state.upcomingMovies.collectAsLazyPagingItems()
    val errorMessage = stringResource(R.string.error_message)
    var snack by remember { mutableStateOf<SnackData?>(null) }

    val context = LocalContext.current
    val launcher: ManagedActivityResultLauncher<Intent, ActivityResult> =
        launchAuthActivityForResult()
    val showNoInternetScreen = (state.isNoInternetConnection
            && upcomingMovies.itemCount == 0
            && state.popularMedia.isEmpty()
            && state.topRatingMedia.isEmpty()
            && state.continueWatchingMedia.isEmpty())

    LaunchedEffect(upcomingMovies.loadState) {
        if (upcomingMovies.loadState.refresh is LoadState.Error && !state.isNoInternetConnection) {
            snack = SnackData(
                message = errorMessage, isError = true

            )
        }
    }

    NovixScaffold(
        topBar = {
            HomeTopBar(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )
        },
        backgroundShapes = {}
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
                                interactionListener.onMediaClick(it.id, it.mediaTypeUi)
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
                                    interactionListener.onMediaClick(it.id, it.mediaTypeUi)
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
                                    interactionListener.onMediaClick(it.id, it.mediaTypeUi)
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

    if (state.userIsLoggedIn) {
        state.selectedMediaToSave?.let { mediaItem ->
            SaveToListBottomSheet(
                isVisible = state.showSaveToListBottomSheet,
                mediaId = mediaItem.id.toLong(),
                onDismiss = interactionListener::onDismissSaveToListBottomSheet,
                onCreateNewListClick = interactionListener::onCreateNewListClick,
            )
        }

        AddBookmarkListBottomSheet(
            isVisible = state.showAddListBottomSheet,
            onDismiss = interactionListener::onDismissAddListBottomSheet,
            mediaId = state.selectedMediaToSave?.id ?: 0
        )
    }
    RequestToLoginBottomSheet(
        isVisible = state.showBottomSheet,
        onDismiss = interactionListener::onDismissBottomSheet,
        onLoginButtonClick = {
            launcher.launch(authApi.getLaunchIntent(context))
        }
    )
}
