package com.sanaa.presentation.screen.mediaTabScreen.topRatingScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.design_system.component.top_bar.NovixTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.api.navigation.LocalAppNavController
import com.sanaa.presentation.components.MediaTabs
import com.sanaa.presentation.components.PaginatedMediaListSectionContent
import com.sanaa.presentation.screen.mediaTabScreen.MediaTabScreenEffect
import com.sanaa.presentation.screen.mediaTabScreen.MediaTabScreenInteractionListener
import com.sanaa.presentation.state.MediaTypeUi
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.inject


@Composable
fun TopRatedMediaScreen(
    modifier: Modifier = Modifier,
    viewModel: TopRatedMediaScreenViewModel = koinViewModel<TopRatedMediaScreenViewModel>(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    val detailsApi: MediaDetailsApi by inject(
        MediaDetailsApi::class.java
    )

    val navController = LocalAppNavController.current
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MediaTabScreenEffect.NavigateToMediaDetails -> {
                    if (effect.mediaTypeUi == MediaTypeUi.MOVIE) {
                        detailsApi.launch(
                            context = navController.context,
                            id = effect.id,
                            startRoute = StartRoute.MOVIE
                        )
                    } else if (effect.mediaTypeUi == MediaTypeUi.TV_SHOW) {
                        detailsApi.launch(
                            context = navController.context,
                            id = effect.id,
                            startRoute = StartRoute.SERIES
                        )
                    }
                }

                is MediaTabScreenEffect.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }
    NovixTheme(isSystemInDarkTheme()) {
        TopRatedMediaScreenContent(
            title = stringResource(R.string.top_rated),
            state = state.value,
            interactionListener = viewModel,
            modifier = modifier,
        )
    }
}


@Composable
private fun TopRatedMediaScreenContent(
    title: String,
    state: TopRatedMediaScreenUiState,
    interactionListener: MediaTabScreenInteractionListener,
    modifier: Modifier = Modifier,
) {
    val topRatedTvShows = state.tvShowList.collectAsLazyPagingItems()
    val topRatedMovies = state.movieList.collectAsLazyPagingItems()
    Column(
        modifier = modifier.padding(top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        NovixTopBar(
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
            modifier = Modifier.padding(top = 8.dp)
        ) { selectedMediaType ->
            when (selectedMediaType) {

                MediaTypeUi.MOVIE -> {
                    PaginatedMediaListSectionContent(
                        genres = state.movieGenres,
                        mediaList = topRatedMovies,
                        selectedGenreId = state.movieSelectedGenreId,
                        onGenreClick = interactionListener::onMovieGenreClick,
                        onMediaClick = { media ->
                            interactionListener.onMediaClick(media.id, media.mediaTypeUi)
                        },
                        onSaveIconClick = interactionListener::onSaveIconClick,
                    )
                }

                MediaTypeUi.TV_SHOW -> {
                    PaginatedMediaListSectionContent(
                        genres = state.tvShowGenres,
                        mediaList = topRatedTvShows,
                        selectedGenreId = state.tvShowSelectedGenreId,
                        onGenreClick = interactionListener::onTvShowGenreClick,
                        onMediaClick = { media ->
                            interactionListener.onMediaClick(media.id, media.mediaTypeUi)
                        },
                        onSaveIconClick = interactionListener::onSaveIconClick,
                    )
                }
            }
        }
    }
}

