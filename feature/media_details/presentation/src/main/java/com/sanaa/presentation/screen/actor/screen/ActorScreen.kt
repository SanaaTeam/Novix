package com.sanaa.presentation.screen.actor.screen

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.api.launchAuthActivityForResult
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.BackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.presentation.api.LocalThemeProvider
import com.sanaa.presentation.bottomsheets.addEditBookmark.AddBookmarkListBottomSheet
import com.sanaa.presentation.bottomsheets.saveToListBottomsheet.SaveToListBottomSheet
import com.sanaa.presentation.navigation.ActorGalleryScreenRoute
import com.sanaa.presentation.navigation.DetailsApiEntryPoint
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.MovieDetailsScreenRoute
import com.sanaa.presentation.navigation.TopMoviesScreenRoute
import com.sanaa.presentation.navigation.TopTvShowsScreenRoute
import com.sanaa.presentation.navigation.TvShowScreenRoute
import com.sanaa.presentation.screen.actor.ActorScreenEffects.NavigateBack
import com.sanaa.presentation.screen.actor.ActorScreenEffects.NavigateToGallery
import com.sanaa.presentation.screen.actor.ActorScreenEffects.NavigateToLogin
import com.sanaa.presentation.screen.actor.ActorScreenEffects.NavigateToMovieDetails
import com.sanaa.presentation.screen.actor.ActorScreenEffects.NavigateToTopMovies
import com.sanaa.presentation.screen.actor.ActorScreenEffects.NavigateToTopTvShows
import com.sanaa.presentation.screen.actor.ActorScreenEffects.NavigateToTvShowDetails
import com.sanaa.presentation.screen.actor.ActorScreenUiState
import com.sanaa.presentation.screen.actor.ActorScreenViewModel
import com.sanaa.presentation.screen.actor.ActorsScreenInteractionListener
import com.sanaa.presentation.screen.actor.componants.ActorInfoCard
import com.sanaa.presentation.screen.actor.componants.GalleryCard
import com.sanaa.presentation.screen.actor.componants.MediaSection
import com.sanaa.presentation.screen.actor.componants.PosterCard
import com.sanaa.presentation.shared_component.ImageSlider
import com.sanaa.presentation.shared_component.OverviewSection
import com.sanaa.presentation.shared_component.RequestToLoginBottomSheet
import com.sanaa.presentation.shared_component.cards.SaveIconChip
import dagger.hilt.android.EntryPointAccessors
import com.sanaa.designsystem.R as designR

@Composable
fun ActorScreen(
    viewModel: ActorScreenViewModel = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavControllerProvider.current
    val context = LocalContext.current

    val authApi = EntryPointAccessors
        .fromApplication(context, DetailsApiEntryPoint::class.java)
        .authenticationApi()

    val launcher = launchAuthActivityForResult()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                NavigateBack -> {
                    if (!navController.popBackStack()) {
                        (navController.context as Activity).finish()
                    }
                }

                is NavigateToTopMovies -> navController.navigate(
                    TopMoviesScreenRoute(effect.actorId)
                )

                is NavigateToTopTvShows -> navController.navigate(
                    TopTvShowsScreenRoute(effect.actorId)
                )

                is NavigateToGallery -> {
                    navController.navigate(ActorGalleryScreenRoute(effect.actorId))
                }

                is NavigateToMovieDetails -> {
                    navController.navigate(MovieDetailsScreenRoute(effect.movieId))
                }

                is NavigateToTvShowDetails -> {
                    navController.navigate(TvShowScreenRoute(effect.tvShowId))
                }

                NavigateToLogin -> {
                    launcher.launch(authApi.getLaunchIntent(context))
                }
            }
        }
    }

    ActorScreenContent(state = uiState, interactionListener = viewModel)
}

@Composable
private fun ActorScreenContent(
    state: ActorScreenUiState,
    interactionListener: ActorsScreenInteractionListener,
) {
    val lazyState = rememberLazyListState()
    var shouldShowBackground by remember { mutableStateOf(false) }
    val animatedColor by animateColorAsState(
        targetValue = if (shouldShowBackground) Theme.colors.surface else Color.Transparent,
        animationSpec = tween(durationMillis = 500, easing = EaseInOut),
    )

    LaunchedEffect(lazyState) {
        snapshotFlow {
            if (lazyState.firstVisibleItemIndex == 0) {
                lazyState.firstVisibleItemScrollOffset
            } else {
                Int.MAX_VALUE
            }
        }.collect { totalScrollPosition ->
            shouldShowBackground = totalScrollPosition > 200
        }
    }

    NovixScaffold(
        backgroundShapes = { BackgroundShapes() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            ActorScreenTopBar(animatedColor, interactionListener)

            AnimatedContent(
                targetState = Pair(state.isLoading, state.noInternetConnection),
                modifier = Modifier.align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) { (isLoading, noInternetConnection) ->
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            LoadingIndicator()
                        }
                    }

                    noInternetConnection -> {
                        NetworkDisconnectionContact(
                            onRetryClick = { interactionListener.onRetryClicked() },
                            modifier = Modifier.fillMaxSize(),
                            useDarkTheme = LocalThemeProvider.current
                        )
                    }

                    else -> {
                        ActorInfo(lazyState, state, interactionListener)
                    }
                }
            }
        }

        if (state.showLoginBottomSheet) {
            RequestToLoginBottomSheet(
                isVisible = true,
                onDismiss = interactionListener::onDismissBottomSheet,
                onLoginButtonClick = interactionListener::onLoginButtonClick
            )
        }

        SaveToListBottomSheet(
            isVisible = state.showSaveToListBottomSheet,
            mediaId = state.selectedMediaToSave?.id?.toLong() ?: 0,
            onDismiss = interactionListener::onDismissSaveToListBottomSheet,
            onCreateNewListClick = interactionListener::onCreateNewListClick,
        )

        if (state.showAddListBottomSheet && state.selectedMediaToSave?.id != null) {
            AddBookmarkListBottomSheet(
                isVisible = true,
                onDismiss = interactionListener::onDismissAddListBottomSheet,
                mediaId = state.selectedMediaToSave.id
            )
        }
    }
}

@Composable
private fun ActorScreenTopBar(
    animatedColor: Color,
    listener: ActorsScreenInteractionListener,
) {
    TopBar(
        modifier = Modifier
            .background(animatedColor)
            .systemBarsPadding()
            .zIndex(10f),
        leftContent = {
            TopBarClickableIcon(
                icon = painterResource(id = designR.drawable.icon_back),
                onClick = listener::onBackClicked
            )
        },
    )
}

@Composable
private fun ActorInfo(
    lazyState: LazyListState,
    state: ActorScreenUiState,
    listener: ActorsScreenInteractionListener,
) {
    LazyColumn(
        state = lazyState,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Box {
                ImageSlider(
                    images = state.profileImageUrls,
                    contentDescription = stringResource(R.string.actor_photos),
                    modifier = Modifier.fillMaxWidth()
                )

                ActorInfoCard(
                    actor = state.actor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 208.dp)
                        .padding(horizontal = 16.dp)
                )
            }
        }

        state.actor.biography?.let { bio ->
            item {
                OverviewSection(
                    titleResId = R.string.biography,
                    overview = bio,
                    onReadMore = { /* expand */ },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                )
            }
        }

        item {
            MediaSection(
                title = stringResource(R.string.gallery),
                items = state.galleryImageUrls.take(10),
                onActionClick = listener::onViewAllGalleryClicked
            ) { image ->
                GalleryCard(image)
            }
        }

        item {
            MediaSection(
                title = stringResource(R.string.top_movie_picks),
                items = state.topMovies.take(10),
                onActionClick = listener::onTopMoviesClicked
            ) { movie ->
                PosterCard(movie.posterUrl, onCardClick = {
                    listener.onMovieClicked(movie.id)
                }, topLeftContent = {
                    SaveIconChip(onClick = { listener.onSaveClicked(movie) })
                })
            }
        }

        item {
            MediaSection(
                title = stringResource(R.string.top_tv_shows_picks),
                items = state.topTvShows.take(10),
                onActionClick = listener::onTopShowsClicked
            ) { tvShow ->
                PosterCard(
                    tvShow.posterPath,
                    onCardClick = {
                        listener.onTvShowClicked(tvShow.id)
                    },
                )
            }
        }
    }
}