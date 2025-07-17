package com.sanaa.presentation.screen.actor.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.loading.NovixLoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixBackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.component.ImageSlider
import com.sanaa.presentation.component.OverviewSection
import com.sanaa.presentation.navigation.ActorGalleryScreenRoute
import com.sanaa.presentation.navigation.LocalNavControllerProvider
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
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ActorScreen(
    actorId: Int,
) {
    val viewModel: ActorViewModel =
        koinViewModel(parameters = { parametersOf(actorId) })
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavControllerProvider.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ActorScreenEffects.NavigateBack ->
                    navController.popBackStack()

                is ActorScreenEffects.NavigateToTopMovies ->
                    navController.navigate(
                        TopMoviesScreenRoute(effect.actorId).route()
                    )

                is ActorScreenEffects.NavigateToTopSeries ->
                    navController.navigate(
                        TopSeriesScreenRoute(effect.actorId).route()
                    )

                is ActorScreenEffects.NavigateToGallery ->
                    navController.navigate(
                        ActorGalleryScreenRoute(effect.actorId).route()
                    )
            }
        }
    }

    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        ActorScreenContent(
            state = uiState,
            listener = viewModel,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActorScreenContent(
    state: ActorScreenUiState,
    listener: ActorsScreenInteractionListener,
    modifier: Modifier = Modifier,
) {
    val isDarkTheme = isSystemInDarkTheme()
    val placeholderResId = if (isDarkTheme) {
        com.sanaa.presentation.R.drawable.movie_placeholder_dark
    } else {
        com.sanaa.presentation.R.drawable.movie_placeholder_light
    }
    NovixScaffold(
        backgroundShapes = { NovixBackgroundShapes() },
    ) {
        Box(modifier = modifier) {

            AppTopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = R.drawable.icon_arrow_back),
                        onClick = listener::onBackClicked
                    )
                },
                modifier = Modifier
                    .systemBarsPadding()
                    .zIndex(10f)
            )

            AnimatedContent(
                targetState = state.isLoading,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) { loading ->
                if (loading) {
                    NovixLoadingIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        item {
                            Box {
                                ImageSlider(
                                    images = state.profileImages,
                                    contentDescription = stringResource(com.sanaa.presentation.R.string.actor_photos),
                                )
                            }
                        }

                        item {
                            ActorInfoCard(
                                actor = state.actor,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .offset(y = (-16).dp)
                            )
                        }

                        state.actor.biography?.let { bio ->
                            item {
                                OverviewSection(
                                    titleResId = com.sanaa.presentation.R.string.overview,
                                    overview = bio,
                                    onReadMore = { /* expand */ },
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth()
                                )
                            }
                        }

                        item {
                            MediaSection(
                                title = stringResource(com.sanaa.presentation.R.string.gallery),
                                items = state.galleryImages.take(10),
                                onActionClick = listener::onViewAllGalleryClicked
                            ) { image ->
                                GalleryCard(image)
                            }
                        }

                        item {
                            MediaSection(
                                title = stringResource(com.sanaa.presentation.R.string.top_movie_picks),
                                items = state.topMovies.take(10),
                                onActionClick = listener::onTopMoviesClicked
                            ) { movie ->
                                PosterCard(movie.imageUrl)
                            }
                        }

                        item {
                            MediaSection(
                                title = stringResource(com.sanaa.presentation.R.string.top_series_picks),
                                items = state.topTvSeries.take(10),
                                onActionClick = listener::onTopSeriesClicked
                            ) { series ->
                                PosterCard(series.imageUrl)
                            }
                        }
                    }
                }
            }
        }
    }
}