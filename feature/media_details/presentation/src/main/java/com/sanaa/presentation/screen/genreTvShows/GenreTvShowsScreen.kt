package com.sanaa.presentation.screen.genreTvShows

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.BackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.presentation.api.LocalThemeProvider
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.TvShowScreenRoute
import com.sanaa.presentation.screen.genreTvShows.components.GenreTvShowsGrid
import com.sanaa.presentation.shared_component.NovixAnimatedSnackBarHost
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GenreTvShowsScreen(
    viewModel: GenreTvShowsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    GenreTvShowsEffectsHandler(effects = viewModel.effect)

    GenreTvShowsScreenContent(
        state = state,
        interactionListener = viewModel
    )
}

@Composable
private fun GenreTvShowsEffectsHandler(
    effects: Flow<GenreTvShowsEffects>,
) {
    val navController = LocalNavControllerProvider.current

    LaunchedEffect(Unit) {
        effects.collectLatest { effect ->
            when (effect) {
                GenreTvShowsEffects.NavigateBack -> if (!navController.popBackStack()) {
                    (navController.context as Activity).finish()
                }

                is GenreTvShowsEffects.NavigateToTvShowDetails -> navController.navigate(
                    TvShowScreenRoute(effect.id)
                )
            }
        }
    }
}

@Composable
private fun GenreTvShowsScreenContent(
    state: GenreTvShowsScreenUiState,
    interactionListener: GenreTvShowsScreenInteractionListener,
) {
    NovixScaffold(
        backgroundShapes = { BackgroundShapes() },
        snackBarHost = {
            NovixAnimatedSnackBarHost(
                data = state.snackBarData,
                onDismiss = interactionListener::onSnackDismissRequested,
                modifier = Modifier.statusBarsPadding()
            )
        },
    ) {
        Column(
            modifier = Modifier.navigationBarsPadding()
        ) {
            TopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = R.drawable.icon_back),
                        onClick = interactionListener::onBackClick
                    )
                },
                screenTitle = state.title.orEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                val pagedTvShows = state.tvShows.collectAsLazyPagingItems()

                val screenState = when {
                    pagedTvShows.loadState.refresh is LoadState.Loading -> ScreenState.LOADING
                    pagedTvShows.loadState.refresh is LoadState.Error -> ScreenState.NO_INTERNET
                    else -> ScreenState.CONTENT
                }

                AnimatedContent(
                    targetState = screenState,
                    contentAlignment = Alignment.Center,
                    transitionSpec = { fadeIn() togetherWith fadeOut() }
                ) { target ->
                    when (target) {
                        ScreenState.NO_INTERNET -> {
                            NetworkDisconnectionContact(
                                onRetryClick = interactionListener::onRetryClick,
                                modifier = Modifier.fillMaxSize(),
                                useDarkTheme = LocalThemeProvider.current
                            )
                        }

                        ScreenState.LOADING -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                LoadingIndicator()
                            }
                        }

                        ScreenState.CONTENT -> {
                            GenreTvShowsGrid(
                                pagedTvShows = pagedTvShows,
                                onTvShowClick = interactionListener::onTvShowClick
                            )
                        }
                    }
                }
            }
        }
    }
}