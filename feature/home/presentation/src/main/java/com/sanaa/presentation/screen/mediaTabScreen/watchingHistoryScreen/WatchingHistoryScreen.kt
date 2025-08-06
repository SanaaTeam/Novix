package com.sanaa.presentation.screen.mediaTabScreen.watchingHistoryScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.api.navigation.LocalAppNavController
import com.sanaa.presentation.components.MediaListSectionContent
import com.sanaa.presentation.components.MediaTabs
import com.sanaa.presentation.navigation.HomeApiEntryPoint
import com.sanaa.presentation.state.MediaTypeUi
import dagger.hilt.android.EntryPointAccessors

@Composable
fun WatchingHistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: WatchingHistoryScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    val navController = LocalAppNavController.current
    val appContext = LocalContext.current.applicationContext

    val detailsApi: MediaDetailsApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, HomeApiEntryPoint::class.java)
            .detailsApi()
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is WatchingHistoryScreenEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                is WatchingHistoryScreenEffect.NavigateToMediaDetails -> {
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
            }
        }
    }

    NovixTheme(isSystemInDarkTheme()) {
        WatchingHistoryScreenContent(
            title = stringResource(R.string.continue_watching),
            state = state.value,
            interactionListener = viewModel,
            modifier = modifier,
        )
    }
}

@Composable
private fun WatchingHistoryScreenContent(
    title: String,
    state: WatchingHistoryScreenUiState,
    interactionListener: WatchingHistoryScreenInteractionListener,
    modifier: Modifier = Modifier,
) {
    val topRatedTvShows = state.tvShowList
    val topRatedMovies = state.movieList

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
                    .statusBarsPadding()
            )
        },
        modifier = modifier.padding(top = 12.dp),
        ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp)
            ) { selectedMediaType ->
                when (selectedMediaType) {

                    MediaTypeUi.MOVIE -> {
                        MediaListSectionContent(
                            genres = state.movieGenres,
                            mediaList = topRatedMovies,
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
                            mediaList = topRatedTvShows,
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
}
