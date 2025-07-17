package com.sanaa.presentation.screens.actors.screen

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixBackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.component.ImageSlider
import com.sanaa.presentation.component.OverviewSection
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.TopMoviesScreenRoute
import com.sanaa.presentation.screens.actors.ActorScreenEffects
import com.sanaa.presentation.screens.actors.ActorScreenUiState
import com.sanaa.presentation.screens.actors.ActorViewModel
import com.sanaa.presentation.screens.actors.ActorsScreenInteractionListener
import com.sanaa.presentation.screens.actors.componants.ActorInfoCard
import com.sanaa.presentation.screens.actors.componants.MediaSection
import com.sanaa.presentation.screens.actors.componants.PosterCard
import com.sanaa.presentation.screens.actors.componants.WavyProgressIndicator
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

    /* ─── one-shot side-effects ─── */
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ActorScreenEffects.NavigateBack ->
                    navController.popBackStack()

                is ActorScreenEffects.NavigateToTopMovies ->
                    navController.navigate(
                        TopMoviesScreenRoute(effect.actorId).route()
                    )
            }
        }
    }

    /* ─── theme wrapper ─── */
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
    /**
     * ──────────────────────────────────────────────────────────────
     *  Scaffold & background identical to SeriesScreen
     * ──────────────────────────────────────────────────────────────
     */
    NovixScaffold(
        backgroundShapes = { NovixBackgroundShapes() },
    ) {
        Box(modifier = modifier) {

            /* ───── Top bar (always visible) ───── */
            AppTopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = R.drawable.icon_arrow_back),
                        onClick = listener::onBackClicked
                    )
                },
                modifier = Modifier
                    .padding(top = 52.dp)
                    .align(Alignment.TopCenter)
            )

            /* ───── Animated switch between loading and content ───── */
            AnimatedContent(
                targetState = state.isLoading,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) { loading ->
                if (loading) {
                    /** full-screen centered indicator */
                    WavyProgressIndicator()
                } else {
                    /** main scrollable body */
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        /* ───── Header images ───── */
                        item {
                            Box {
                                ImageSlider(
                                    images = state.profileImages,
                                    contentDescription = "Actor photos"
                                )
                            }
                        }

                        /* ───── Actor info card ───── */
                        item {
                            ActorInfoCard(
                                actor = state.actor,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .offset(y = (-16).dp)
                            )
                        }

                        /* ───── Biography ───── */
                        state.actor.biography?.let { bio ->
                            item {
                                OverviewSection(
                                    titleResId = R.string.clear,
                                    overview = bio,
                                    onReadMore = { /* expand */ },
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth()
                                )
                            }
                        }

                        /* ───── Top-movie picks ───── */
                        item {
                            MediaSection(
                                title = "Top movie picks",
                                items = state.topMovies,
                                onActionClick = listener::onTopMoviesClicked
                            ) { movie ->
                                PosterCard(movie.imageUrl)
                            }
                        }

                        /* ───── Top-series picks ───── */
                        item {
                            MediaSection(
                                title = "Top series picks",
                                items = state.topTvSeries,
                                onActionClick = { /* see all series */ }
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