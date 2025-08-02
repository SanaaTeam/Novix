package com.sanaa.presentation.screen.actor.screen

import android.app.Activity
import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.api.launchAuthActivityForResult
import com.sanaa.designsystem.design_system.component.loading.NovixLoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixBackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.component.top_bar.NovixTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.presentation.navigation.ActorGalleryScreenRoute
import com.sanaa.presentation.navigation.DetailsApiEntryPoint
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.MovieDetailsScreenRoute
import com.sanaa.presentation.navigation.SeriesDetailsScreenRoute
import com.sanaa.presentation.navigation.TopMoviesScreenRoute
import com.sanaa.presentation.navigation.TopSeriesScreenRoute
import com.sanaa.presentation.screen.actor.ActorScreenEffects
import com.sanaa.presentation.screen.actor.ActorScreenUiState
import com.sanaa.presentation.screen.actor.ActorViewModel
import com.sanaa.presentation.screen.actor.ActorsScreenInteractionListener
import com.sanaa.presentation.screen.actor.componants.ActorInfoCard
import com.sanaa.presentation.screen.actor.componants.GalleryCard
import com.sanaa.presentation.screen.actor.componants.MediaSection
import com.sanaa.presentation.screen.actor.componants.PosterCard
import com.sanaa.presentation.shared_component.ImageSlider
import com.sanaa.presentation.shared_component.OverviewSection
import com.sanaa.presentation.shared_component.RequestToLoginBottomSheet
import dagger.hilt.android.EntryPointAccessors

@Composable
fun ActorScreen(
    viewModel: ActorViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavControllerProvider.current
    val context = LocalContext.current

    val authApi = EntryPointAccessors.fromApplication(
        context,
        DetailsApiEntryPoint::class.java
    ).authenticationApi()

   val launcher =  launchAuthActivityForResult(
        loggedInWithSessionId = {
            viewModel.updateUserLoggingStatus()
        },
        loggedInAsGuest = {
            viewModel.updateUserLoggingStatus()
        }
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ActorScreenEffects.NavigateBack -> {
                    if (!navController.popBackStack()) {
                        (navController.context as Activity).finish()
                    }
                }

                is ActorScreenEffects.NavigateToTopMovies -> navController.navigate(
                    TopMoviesScreenRoute(effect.actorId).route()
                )

                is ActorScreenEffects.NavigateToTopSeries -> navController.navigate(
                    TopSeriesScreenRoute(effect.actorId).route()
                )

                is ActorScreenEffects.NavigateToGallery -> navController.navigate(
                    ActorGalleryScreenRoute(effect.actorId).route()
                )

                is ActorScreenEffects.NavigateToMovieDetails -> {
                    navController.navigate(
                        MovieDetailsScreenRoute(effect.movieId).route()
                    )
                }

                is ActorScreenEffects.NavigateToSeriesDetails -> {
                    navController.navigate(
                        SeriesDetailsScreenRoute(effect.seriesId).route()
                    )
                }

                ActorScreenEffects.NavigateToLogin -> {
                    launcher.launch(authApi.getLaunchIntent(context))
                }
            }
        }
    }

    ActorScreenContent(
        state = uiState,
        listener = viewModel,
        modifier = Modifier.fillMaxSize(),
    )

}

@Composable
private fun ActorScreenContent(
    state: ActorScreenUiState,
    listener: ActorsScreenInteractionListener,
    modifier: Modifier = Modifier,
) {
    NovixScaffold(
        backgroundShapes = { NovixBackgroundShapes() },
    ) {
        Box(modifier = modifier.navigationBarsPadding()) {

            NovixTopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = R.drawable.icon_back),
                        onClick = listener::onBackClicked
                    )
                }, modifier = Modifier
                    .systemBarsPadding()
                    .zIndex(10f)
            )

            AnimatedContent(
                targetState = state.isLoading || state.noInternetConnection,
                modifier = Modifier.align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                if (it) {
                    if (state.noInternetConnection) {
                        NetworkDisconnectionContact(
                            onRetryClick = { listener.onRetryClicked() },
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            NovixLoadingIndicator()
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
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
                                        .padding(start = 16.dp, end = 16.dp,top=16.dp)
                                        .fillMaxWidth()
                                )
                            }
                        }

                        item {
                            MediaSection(
                                title = stringResource(R.string.gallery),
                                items = state.galleryImageUrls.take(10),
                                modifier = Modifier.padding(top = 16.dp),
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
                                }, onSaveClick = listener::onSaveClicked)
                            }
                        }

                        item {
                            MediaSection(
                                title = stringResource(R.string.top_series_picks),
                                items = state.topTvSeries.take(10),
                                onActionClick = listener::onTopSeriesClicked
                            ) { series ->
                                PosterCard(series.posterPath, onCardClick = {
                                    listener.onSeriesClicked(series.id)
                                }, onSaveClick = listener::onSaveClicked)
                            }
                        }
                    }
                }
            }
        }
        if (state.showLoginBottomSheet) {
            RequestToLoginBottomSheet(
                isVisible = state.showLoginBottomSheet,
                onDismiss = listener::onDismissBottomSheet,
                onLoginButtonClick = {
                    listener.onLoginButtonClick()
                }
            )
        }
    }
}