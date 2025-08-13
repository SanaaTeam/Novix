package com.sanaa.presentation.screen.watchingHistory

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
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
import com.sanaa.designsystem.design_system.component.animation.FadeSlideInVertically
import com.sanaa.designsystem.design_system.component.animation.FadeSlideOutVertically
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
import com.sanaa.presentation.screen.watchingHistory.WatchingHistoryScreenEffect.ShowErrorSnackBar
import com.sanaa.presentation.screen.watchingHistory.WatchingHistoryScreenEffect.ShowSuccessSnackBar
import com.sanaa.presentation.screen.watchingHistory.component.FilterTab
import com.sanaa.presentation.screen.watchingHistory.component.GridSection
import com.sanaa.presentation.screen.watchingHistory.component.NovixAnimatedSnackBarHost
import com.sanaa.presentation.screen.watchingHistory.component.SnackData
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WatchingHistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: WatchingHistoryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavControllerProvider.current
    val appContext = LocalContext.current.applicationContext
    var snackBarData by remember { mutableStateOf<SnackData?>(null) }

    val detailsApi: MediaDetailsApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, ProfileApiEntryPoint::class.java)
            .detailsApi()
    }

    WatchingHistoryScreenEffectsHandler(
        effects = viewModel.effect,
        onNavigateBack = { navController.popBackStack() },
        onNavigateToMediaDetails = { id, type ->
            detailsApi.launch(
                context = navController.context,
                id = id,
                startRoute = when (type) {
                    MediaTypeUi.MOVIE -> StartRoute.MOVIE
                    MediaTypeUi.TV_SHOW -> StartRoute.SERIES
                }
            )
        },
        onShowErrorSnackBar = { message ->
            snackBarData = SnackData(message = message, isError = true)
        },
        onShowSuccessSnackBar = { message ->
            snackBarData = SnackData(message = message, isError = false)
        }
    )

    WatchingHistoryScreenContent(
        state = state,
        interactionListener = viewModel,
        modifier = modifier,
        snackBarData = snackBarData,
    )
}

@Composable
private fun WatchingHistoryScreenEffectsHandler(
    onNavigateBack: () -> Unit,
    onNavigateToMediaDetails: (Int, MediaTypeUi) -> Unit,
    onShowErrorSnackBar: (String) -> Unit,
    onShowSuccessSnackBar: (String) -> Unit,
    effects: SharedFlow<WatchingHistoryScreenEffect>
) {
    LaunchedEffect(Unit) {
        effects.collectLatest { effect ->
            when (effect) {
                is NavigateBack -> onNavigateBack()
                is NavigateToMediaDetails -> onNavigateToMediaDetails(effect.id, effect.mediaTypeUi)
                is ShowErrorSnackBar -> onShowErrorSnackBar(effect.message)
                is ShowSuccessSnackBar -> onShowSuccessSnackBar(effect.message)
            }
        }
    }
}

@Composable
private fun WatchingHistoryScreenContent(
    state: WatchingHistoryUiState,
    interactionListener: WatchingHistoryInteractionListener,
    modifier: Modifier = Modifier,
    snackBarData: SnackData?,
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                NovixAnimatedSnackBarHost(
                    data = snackBarData,
                    onDismiss = interactionListener::onDismissSnack,
                )
            }
        }) {
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



    state.selectedMediaToSave?.let { mediaItem ->
        AnimatedVisibility(
            visible = state.showSaveToListBottomSheet,
            enter = FadeSlideInVertically,
            exit = FadeSlideOutVertically
        ) {
            SaveToListBottomSheet(
                isVisible = state.showSaveToListBottomSheet,
                mediaId = mediaItem.id.toLong(),
                onDismiss = interactionListener::onDismissSaveToListBottomSheet,
                onCreateNewListClick = interactionListener::onCreateNewListClick,
                onSuccess = { interactionListener.onSaveToListResult(true) },
                onFailure = { interactionListener.onSaveToListResult(false) }
            )
        }
    }

    AddBookmarkListBottomSheet(
        isVisible = state.showAddListBottomSheet,
        onDismiss = interactionListener::onDismissAddListBottomSheet,
        mediaId = state.selectedMediaToSave?.id ?: 0
    )
}