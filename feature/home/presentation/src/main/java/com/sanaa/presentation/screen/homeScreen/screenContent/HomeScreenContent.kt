package com.sanaa.presentation.screen.homeScreen.screenContent

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.api.AuthenticationApi
import com.sanaa.api.launchAuthActivityForResult
import com.sanaa.designsystem.design_system.component.button.PrimaryButton
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.components.NovixAnimatedSnackBarHost
import com.sanaa.presentation.components.RequestToLoginBottomSheet
import com.sanaa.presentation.components.SnackData
import com.sanaa.presentation.components.cards.HomeTopBar
import com.sanaa.presentation.components.shimmerEffect.MediaSliderSectionPlaceholder
import com.sanaa.presentation.components.shimmerEffect.PopularMediaSectionPlaceholder
import com.sanaa.presentation.modifiers.fillWidthOfParent
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
        launchAuthActivityForResult(
            loggedInWithSessionId = {
                interactionListener.onAuthActivityFinishedWithResult()
            },
            loggedInAsGuest = {
                interactionListener.onAuthActivityFinishedWithResult()
            }
        )

    LaunchedEffect(upcomingMovies.loadState) {
        if (upcomingMovies.loadState.refresh is LoadState.Error && !state.isNoInternet) {
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
                    .navigationBarsPadding()
                    .statusBarsPadding()
            )
        },
        backgroundShapes = {}) {

        if (state.isNoInternet) {
            NetworkDisconnectionContact(
                onRetryClick = interactionListener::onRetryClick, modifier = Modifier.fillMaxSize()
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
                if (state.isLoading) item(span = { GridItemSpan(maxLineSpan) }) {
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
                            interactionListener.onMoviesCardClicked()
                        },
                        onTvShowsClicked = {
                            interactionListener.onTvShowsCardClicked()
                        },
                        onPeopleClicked = {
                            interactionListener.onPeopleCardClicked()
                        },
                        modifier = Modifier
                            .fillWidthOfParent(16.dp)
                            .padding(top = 8.dp),
                        isLoading = state.isLoading
                    )
                }
                if (state.isLoading) item(span = { GridItemSpan(maxLineSpan) }) {
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
                            onViewAllClick = { interactionListener.onShowAllTopRatingClicked() })
                    }
                }
                if (state.isLoading) {
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
                            onViewAllClick = { interactionListener.onShowAllContinueWatchingClicked() },
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
                    isLoading = state.isLoading
                )
            }
        }
    }
    if (upcomingMovies.loadState.append is LoadState.Error
        || upcomingMovies.loadState.refresh is LoadState.Error && !state.isNoInternet
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            PrimaryButton(
                text = null,
                icon = painterResource(R.drawable.icon_refresh),
                onClick = upcomingMovies::retry,
                modifier = Modifier
                    .width(52.dp)
                    .align(Alignment.BottomCenter)
                    .padding(
                        bottom = 16.dp
                    ),
            )
        }
    }
    NovixAnimatedSnackBarHost(
        data = snack, onDismiss = { snack = null })


    RequestToLoginBottomSheet(
        isVisible = state.showBottomSheet,
        onDismiss = interactionListener::onDismissBottomSheet,
        onLoginButtonClick = {
            launcher.launch(authApi.getLaunchIntent(context))
        }
    )
}
