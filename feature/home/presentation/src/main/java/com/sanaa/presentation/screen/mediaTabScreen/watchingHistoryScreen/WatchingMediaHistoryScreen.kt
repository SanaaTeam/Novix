package com.sanaa.presentation.screen.mediaTabScreen.watchingHistoryScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.api.navigation.LocalAppNavController
import com.sanaa.presentation.components.MediaListSectionContent
import com.sanaa.presentation.components.MediaTabs
import com.sanaa.presentation.components.NovixAnimatedSnackBarHost
import com.sanaa.presentation.components.RefreshButton
import com.sanaa.presentation.components.SnackData
import com.sanaa.presentation.navigation.HomeApiEntryPoint
import com.sanaa.presentation.state.MediaTypeUi
import dagger.hilt.android.EntryPointAccessors

@Composable
fun WatchingMediaHistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: WatchingMediaHistoryScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    val navController = LocalAppNavController.current
    val appContext = LocalContext.current.applicationContext

    val detailsApi: MediaDetailsApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, HomeApiEntryPoint::class.java)
            .detailsApi()
    }

    var snack by remember { mutableStateOf<SnackData?>(null) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is WatchingMediaHistoryScreenEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                is WatchingMediaHistoryScreenEffect.NavigateToMediaDetails -> {
                    when (effect.mediaTypeUi) {
                        MediaTypeUi.MOVIE -> {
                            detailsApi.launch(
                                context = navController.context,
                                id = effect.id,
                                startRoute = StartRoute.MOVIE
                            )
                        }

                        MediaTypeUi.TV_SHOW -> {
                            detailsApi.launch(
                                context = navController.context,
                                id = effect.id,
                                startRoute = StartRoute.SERIES
                            )
                        }
                    }
                }

                is WatchingMediaHistoryScreenEffect.ShowError -> {
                    snack = SnackData(message = effect.message, isError = true)
                }
            }
        }
    }
    Box(modifier = modifier.systemBarsPadding()) {

        WatchingMediaHistoryScreenContent(
            title = stringResource(R.string.watching_history),
            state = state.value,
            interactionListener = viewModel,
            modifier = Modifier,
        )

        NovixAnimatedSnackBarHost(
            data = snack,
            onDismiss = { snack = null }
        )
    }
}

@Composable
private fun WatchingMediaHistoryScreenContent(
    title: String,
    state: WatchingMediaHistoryScreenUiState,
    interactionListener: WatchingMediaHistoryScreenInteractionListener,
    modifier: Modifier = Modifier,
) {
    val tvShows = state.tvShowList
    val movies = state.movieList

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
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            MediaTabs(
                onTabClick = interactionListener::onMediaTabSelection,
                selectedTab = state.selectedMediaTypeUi,
                modifier = Modifier.fillMaxWidth()
            )

            AnimatedContent(
                targetState = state.selectedMediaTypeUi,
                transitionSpec = {
                    fadeIn(animationSpec = tween(150, delayMillis = 150))
                        .togetherWith(fadeOut(animationSpec = tween(150)))
                },
                modifier = Modifier.fillMaxSize()
            ) { selectedMediaType ->
                when (selectedMediaType) {

                    MediaTypeUi.MOVIE -> {
                        MediaListSectionContent(
                            genres = state.movieGenres,
                            mediaList = movies,
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
                        MediaListSectionContent(
                            genres = state.tvShowGenres,
                            mediaList = tvShows,
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
                if (state.showRefreshButton) {
                    RefreshButton(onRetryClick = interactionListener::onRetryClick)
                }
            }
        }
    }
}
