package com.sanaa.presentation.screen.watchingHistory

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.animation.FadeInOut150
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.presentation.navigation.ProfileApiEntryPoint
import com.sanaa.presentation.provider.LocalNavControllerProvider
import com.sanaa.presentation.screen.bottomsheet.addEditBookmark.AddBookmarkListBottomSheet
import com.sanaa.presentation.screen.bottomsheet.saveToBottomSheet.SaveToListBottomSheet
import com.sanaa.presentation.screen.myRating.MediaTypeUi
import com.sanaa.presentation.screen.watchingHistory.WatchingHistoryScreenEffect.NavigateBack
import com.sanaa.presentation.screen.watchingHistory.WatchingHistoryScreenEffect.NavigateToMediaDetails
import com.sanaa.presentation.screen.watchingHistory.component.AnimatedSnackBarHost
import com.sanaa.presentation.screen.watchingHistory.component.FilterTab
import com.sanaa.presentation.screen.watchingHistory.component.GridSection
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WatchingHistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: WatchingHistoryViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    WatchingHistoryScreenEffectsHandler(
        effects = viewModel.effect,
    )

    WatchingHistoryScreenContent(
        state = state,
        interactionListener = viewModel,
        modifier = modifier,
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

    NovixScaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = R.drawable.icon_back),
                        onClick = interactionListener::onBackClick
                    )
                },
                screenTitle = stringResource(id = R.string.watching_history),
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            )
        },
        snackBarHost = {
            AnimatedSnackBarHost(
                data = state.snackBarData,
                onDismiss = interactionListener::onDismissSnack
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FilterTab(
                onTabClick = interactionListener::onMediaTabSelection,
                selectedTab = state.selectedMediaTypeUi,
                modifier = Modifier.fillMaxWidth()
            )

            AnimatedContent(
                targetState = state.selectedMediaTypeUi,
                transitionSpec = { FadeInOut150 },
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
}


@Composable
private fun WatchingHistoryScreenEffectsHandler(
    effects: Flow<WatchingHistoryScreenEffect>,
) {
    val navController = LocalNavControllerProvider.current
    val appContext = LocalContext.current.applicationContext

    val detailsApi: MediaDetailsApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, ProfileApiEntryPoint::class.java)
            .detailsApi()
    }

    LaunchedEffect(Unit) {
        effects.collectLatest { effect ->
            when (effect) {
                is NavigateBack -> navController.popBackStack()
                is NavigateToMediaDetails -> detailsApi.launch(
                    context = navController.context,
                    id = effect.id,
                    startRoute = when (effect.mediaTypeUi) {
                        MediaTypeUi.MOVIE -> StartRoute.MOVIE
                        MediaTypeUi.TV_SHOW -> StartRoute.TV_SHOW
                    }
                )
            }
        }
    }
}