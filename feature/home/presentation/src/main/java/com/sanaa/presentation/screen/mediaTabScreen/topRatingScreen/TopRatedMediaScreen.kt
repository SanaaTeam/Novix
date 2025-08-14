package com.sanaa.presentation.screen.mediaTabScreen.topRatingScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.api.navigation.LocalAppNavController
import com.sanaa.presentation.bottomsheet.addEditBookmark.AddBookmarkListBottomSheet
import com.sanaa.presentation.bottomsheet.saveToListBottomsheet.SaveToListBottomSheet
import com.sanaa.presentation.components.MediaTabs
import com.sanaa.presentation.components.PaginatedMediaListSectionContent
import com.sanaa.presentation.components.RefreshButton
import com.sanaa.presentation.components.RequestToLoginBottomSheet
import com.sanaa.presentation.components.SnackData
import com.sanaa.presentation.navigation.HomeApiEntryPoint
import com.sanaa.presentation.state.MediaTypeUiState
import dagger.hilt.android.EntryPointAccessors


@Composable
fun TopRatedMediaScreen(
    modifier: Modifier = Modifier,
    viewModel: TopRatedMediaScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalAppNavController.current
    val appContext = LocalContext.current.applicationContext
    var snack by remember { mutableStateOf<SnackData?>(null) }

    val detailsApi: MediaDetailsApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, HomeApiEntryPoint::class.java)
            .detailsApi()
    }

    val context = LocalContext.current

    val authApi = EntryPointAccessors.fromApplication(
        context,
        HomeApiEntryPoint::class.java
    ).authenticationApi()

    val launcher = launchAuthActivityForResult()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TopRatedScreenEffect.NavigateToMediaDetails -> {
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

                is TopRatedScreenEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                TopRatedScreenEffect.NavigateToLogin -> {
                    launcher.launch(authApi.getLaunchIntent(context))
                }

                is TopRatedScreenEffect.ShowError -> {
                    snack = SnackData(message = effect.message, isError = true)
                }

                is TopRatedScreenEffect.ShowSuccess -> {
                    snack = SnackData(message = effect.message, isError = false)
                }
            }
        }
    }
    TopRatedMediaScreenContent(
        title = stringResource(R.string.top_rated),
        state = state.value,
        interactionListener = viewModel,
        modifier = modifier,
    )

    if (state.value.userIsLoggedIn) {
        state.value.selectedMediaToSave?.let { mediaItem ->
            SaveToListBottomSheet(
                isVisible = state.value.showSaveToListBottomSheet,
                mediaId = mediaItem.id.toLong(),
                onDismiss = viewModel::onDismissSaveToListBottomSheet,
                onCreateNewListClick = viewModel::onCreateNewListClick,
            )
        }
        AddBookmarkListBottomSheet(
            isVisible = state.value.showAddListBottomSheet,
            onDismiss = viewModel::onDismissAddListBottomSheet,
            mediaId = state.value.selectedMediaToSave?.id ?: 0
        )
    } else {
        RequestToLoginBottomSheet(
            isVisible = state.value.showLoginBottomSheet,
            onDismiss = viewModel::onDismissBottomSheet,
            onLoginButtonClick = {
                viewModel.onLoginButtonClick()
            }
        )
    }

}


@Composable
private fun TopRatedMediaScreenContent(
    title: String,
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
                        icon = painterResource(id = R.drawable.icon_back),
                        onClick = interactionListener::onBackClick
                    )
                },
                screenTitle = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )
        },
        modifier = modifier.systemBarsPadding(),
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
                                    interactionListener.onMediaClick(media.id, media.mediaTypeUiState)
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
                                    interactionListener.onMediaClick(media.id, media.mediaTypeUiState)
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
}

