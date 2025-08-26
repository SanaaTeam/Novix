package com.sanaa.tvapp.presentation.screens.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.component.chips.MediaRatingChip
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.presentation.components.MediaSection
import com.sanaa.tvapp.presentation.screens.home.component.HomeScreenLoading
import com.sanaa.tvapp.presentation.screens.home.component.HomeTabs
import com.sanaa.tvapp.presentation.screens.home.component.PopularMoviesCarousel
import com.sanaa.tvapp.presentation.screens.navigation.LocalAppNavController
import com.sanaa.tvapp.presentation.screens.navigation.LocalDrawerFocusRequester
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.MovieDetailsRoute
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.TvShowDetailsRoute
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.FocusableMediaCard
import com.sanaa.tvapp.state.MediaItemUiState
import com.sanaa.tvapp.state.MediaTypeUiState
import com.sanaa.tvapp.util.modifier.handleDPadKeyEvents
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import com.sanaa.tvapp.R as tvResource

@Composable
fun HomeScreen(
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
) {
    val state by homeScreenViewModel.state.collectAsStateWithLifecycle()

    EffectHandler(homeScreenViewModel.effect)

    HomeScreenContent(state, homeScreenViewModel)
}

@Composable
fun HomeScreenContent(
    state: HomeScreenUiState,
    interactionListener: HomeScreenInteractionListener
) {
    AnimatedContent(
        targetState = state.isLoading to state.isNoInternet,
    ) { (isLoading, isNoInternet)->
        when {
            isNoInternet -> {
                NetworkDisconnectionContact(
                    modifier = Modifier.fillMaxSize(),
                    onRetryClick = interactionListener::onRetryClick
                )
            }

            isLoading -> {
                HomeScreenLoading(modifier = Modifier.verticalScroll(rememberScrollState()))
            }

            else -> {
                HomeReadyContent(state, interactionListener)
            }
        }
    }
}

@Composable
private fun HomeReadyContent(state: HomeScreenUiState, interactionListener: HomeScreenInteractionListener){

    val sidePaddings = 36.dp
    val carouselFocusRequester = remember { FocusRequester() }

    val upcomingMovies = state.upcomingMovies.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        carouselFocusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 36.dp, vertical = 24.dp)
                    .align(Alignment.CenterEnd),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    modifier = Modifier
                        .size(30.dp),
                    painter = painterResource(tvResource.drawable.novix_logo),
                    contentDescription = null,
                )

                Text(
                    stringResource(tvResource.string.app_name),
                    color = Theme.colors.title,
                    style = Theme.textStyle.title.medium,
                )
            }
        }

        PopularMoviesCarousel(
            modifier = Modifier
                .padding(
                    start = sidePaddings,
                    end = sidePaddings,
                    bottom = 16.dp
                )
                .focusRequester(carouselFocusRequester)
            ,

            mediaItemUiStates = state.popularMedia,
            onShowDetails = {
                interactionListener.onMediaClick(
                    it.id,
                    it.mediaTypeUiState
                )
            }
        )

        HomeTabs(
            modifier = Modifier.padding(top = 12.dp),
            sidePaddings = sidePaddings,
            onTabSelected = interactionListener::onTabClick
        )

        AnimatedContent(
            targetState = state.selectedTab
        ) { selectedTab ->
            when (selectedTab) {
                SelectedHomeTab.MOVIES -> {
                    HomeMovies(state, upcomingMovies) { id ->
                        interactionListener.onMediaClick(id, MediaTypeUiState.MOVIE)
                    }
                }

                SelectedHomeTab.TV_SHOWS -> {
                    HomeTvShows(state) { id ->
                        interactionListener.onMediaClick(id, MediaTypeUiState.TV_SHOW)
                    }
                }
            }
        }
    }
}

@Composable
fun HomeMovies(
    state: HomeScreenUiState,
    upcomingMovies: LazyPagingItems<MediaItemUiState>,
    onItemClick: (Int) -> Unit,
) {
    Column {
        MediaSection(title = stringResource(tvResource.string.top_rated)) {
            items(
                items = state.topRatingMovies,
                key = { it.id }
            ) {
                FocusableMediaCard(
                    imageUrl = it.imageUrl ?: "",
                    titleText = it.title,
                    onClick = { onItemClick(it.id) }
                )
            }
        }

        if (state.continueWatchingMovies.isNotEmpty()) {
            MediaSection(title = stringResource(tvResource.string.watching_history)) {
                items(
                    items = state.continueWatchingMovies,
                    key = { it.id }
                ) {
                    FocusableMediaCard(
                        imageUrl = it.imageUrl ?: "",
                        titleText = it.title,
                        onClick = { onItemClick(it.id) }
                    )
                }
            }
        }

        MediaSection(title = stringResource(tvResource.string.up_upcoming)) {
            items(
                count = upcomingMovies.itemCount,
            ) { index ->
                upcomingMovies[index]?.let {
                    FocusableMediaCard(
                        imageUrl = it.imageUrl ?: "",
                        titleText = it.title,
                        onClick = { onItemClick(it.id) }
                    )
                }
            }
        }

        if (state.ratedMovies.isNotEmpty()) {
            MediaSection(title = stringResource(tvResource.string.my_rating)) {
                items(state.ratedMovies) { item ->
                    FocusableMediaCard(
                        imageUrl = item.imageUrl ?: "",
                        titleText = item.title,
                        onClick = { onItemClick(item.id) },
                        topCornerContent = {
                            MediaRatingChip(rating = item.rating.orEmpty())
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeTvShows(
    state: HomeScreenUiState,
    onItemClick: (Int) -> Unit,
) {
    Column {
        if (state.topRatingTvShows.isNotEmpty()) {
            MediaSection(title = stringResource(tvResource.string.top_rated)) {
                items(
                    items = state.topRatingTvShows,
                    key = { it.id }
                ) {
                    FocusableMediaCard(
                        imageUrl = it.imageUrl ?: "",
                        titleText = it.title,
                        onClick = { onItemClick(it.id) }
                    )
                }
            }
        }

        if (state.continueWatchingTvShows.isNotEmpty()) {
            MediaSection(title = stringResource(tvResource.string.watching_history)) {
                items(
                    items = state.continueWatchingTvShows,
                    key = { it.id }
                ) {
                    FocusableMediaCard(
                        imageUrl = it.imageUrl ?: "",
                        titleText = it.title,
                        onClick = { onItemClick(it.id) }
                    )
                }
            }
        }

        if (state.ratedTvShows.isNotEmpty()) {
            MediaSection(title = stringResource(tvResource.string.my_rating)) {
                items(state.ratedTvShows) { item ->
                    FocusableMediaCard(
                        imageUrl = item.imageUrl ?: "",
                        titleText = item.title,
                        onClick = { onItemClick(item.id) },
                        topCornerContent = {
                            MediaRatingChip(rating = item.rating.orEmpty())
                        }
                    )
                }
            }
        }
    }
}


@Composable
private fun EffectHandler(
    effect: Flow<HomeScreenEffect>
) {
    val mainTvNavController = LocalAppNavController.current
    LaunchedEffect(Unit) {
        effect.collectLatest { effect ->
            when (effect) {
                is HomeScreenEffect.NavigateToMediaDetails -> {
                    if (effect.mediaTypeUiState == MediaTypeUiState.MOVIE)
                        mainTvNavController.navigate(MovieDetailsRoute(effect.id))
                    else
                        mainTvNavController.navigate(TvShowDetailsRoute(effect.id))
                }
            }
        }
    }
}
