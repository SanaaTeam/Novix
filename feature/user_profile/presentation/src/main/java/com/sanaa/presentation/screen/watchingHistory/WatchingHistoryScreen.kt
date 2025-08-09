package com.sanaa.presentation.screen.watchingHistory

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
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.presentation.api.navigation.LocalNavControllerProvider
import com.sanaa.presentation.api.navigation.ProfileApiEntryPoint
import com.sanaa.presentation.screen.bottomsheet.addEditBookmark.AddBookmarkListBottomSheet
import com.sanaa.presentation.screen.bottomsheet.saveToBottomSheet.SaveToListBottomSheet
import com.sanaa.presentation.screen.myRating.MediaTypeUi
import com.sanaa.presentation.screen.watchingHistory.component.FilterTab
import com.sanaa.presentation.screen.watchingHistory.component.GridSection
import com.sanaa.presentation.screen.watchingHistory.component.NovixAnimatedSnackBarHost
import com.sanaa.presentation.screen.watchingHistory.component.SnackData
import dagger.hilt.android.EntryPointAccessors

@Composable
fun WatchingHistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: WatchingHistoryViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavControllerProvider.current
    val appContext = LocalContext.current.applicationContext
    var snack by remember { mutableStateOf<SnackData?>(null) }

    val detailsApi: MediaDetailsApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, ProfileApiEntryPoint::class.java)
            .detailsApi()
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is WatchingHistoryScreenEffect.NavigateToMediaDetails -> {
                    val startRoute =
                        if (effect.mediaTypeUi == MediaTypeUi.MOVIE) StartRoute.MOVIE else StartRoute.SERIES
                    detailsApi.launch(
                        context = navController.context,
                        id = effect.id,
                        startRoute = startRoute
                    )
                }

                is WatchingHistoryScreenEffect.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    WatchingHistoryScreenContent(
        state = state.value,
        interactionListener = viewModel,
        modifier = modifier,
    )
    NovixAnimatedSnackBarHost(
        data = snack, onDismiss = { snack = null })
    state.value.selectedMediaToSave?.let { mediaItem ->
        SaveToListBottomSheet(
            isVisible = state.value.showSaveToListBottomSheet,
            mediaId = mediaItem.id.toLong(),
            onDismiss = viewModel::onDismissSaveToListBottomSheet,
            onCreateNewListClick = viewModel::onCreateNewListClick,
            onSuccess = {
                snack = SnackData(
                    message = "Added to list successfully",
                    isError = false
                )
            },
            onFailure = {
                snack = SnackData(
                    message = "Added to list failed",
                    isError = true
                )
            },
        )
    }

    AddBookmarkListBottomSheet(
        isVisible = state.value.showAddListBottomSheet,
        onDismiss = viewModel::onDismissAddListBottomSheet,
        mediaId = state.value.selectedMediaToSave?.id ?: 0
    )
}

@Composable
private fun WatchingHistoryScreenContent(
    state: WatchingHistoryUiState,
    interactionListener: WatchingHistoryInteractionListener,
    modifier: Modifier = Modifier,
) {
    val watchedMovies = state.movieList
    val watchedTvShows = state.tvShowList

    Column(
        modifier = modifier.padding(top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        TopBar(
            leftContent = {
                TopBarClickableIcon(
                    icon = painterResource(id = R.drawable.icon_back),
                    onClick = { interactionListener.onBackClick() }
                )
            },
            screenTitle = stringResource(id = R.string.watching_history),
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        )

        FilterTab(
            onTabClick = { mediaTypeUi ->
                interactionListener.onMediaTabSelection(mediaTypeUi)
            },
            selectedTab = state.selectedMediaTypeUi,
            modifier = Modifier.fillMaxWidth()
        )
        AnimatedContent(
            targetState = state.selectedMediaTypeUi,
            transitionSpec = {
                fadeIn(animationSpec = tween(150, delayMillis = 150))
                    .togetherWith(fadeOut(animationSpec = tween(150)))
            },
            modifier = Modifier.padding(top = 8.dp)
        ) { selectedMediaType ->
            when (selectedMediaType) {
                MediaTypeUi.MOVIE -> {
                    GridSection(
                        genres = state.movieGenres,
                        mediaList = watchedMovies,
                        selectedGenreId = state.movieSelectedGenreId,
                        onGenreClick = interactionListener::onMovieGenreClick,
                        onMediaClick = { media ->
                            interactionListener.onMediaClick(media.id, media.mediaTypeUi)
                        },
                        onSaveIconClick = interactionListener::onSaveIconClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                MediaTypeUi.TV_SHOW -> {
                    GridSection(
                        genres = state.tvShowGenres,
                        mediaList = watchedTvShows,
                        selectedGenreId = state.tvShowSelectedGenreId,
                        onGenreClick = interactionListener::onTvShowGenreClick,
                        onMediaClick = { media ->
                            interactionListener.onMediaClick(media.id, media.mediaTypeUi)
                        },
                        onSaveIconClick = interactionListener::onSaveIconClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

