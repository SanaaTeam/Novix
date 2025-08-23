package com.sanaa.tvapp.presentation.screens.genreTvShows

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.BackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.R
import com.sanaa.tvapp.presentation.components.TVNetworkDisconnectionContact
import com.sanaa.tvapp.presentation.screens.login.components.NovixAnimatedSnackBarHost
import com.sanaa.tvapp.presentation.screens.navigation.LocalAppNavController
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.TvShowDetailsRoute
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.FocusableMediaCard


@Composable
fun GenreTvShowsScreen(
    viewModel: GenreTvShowsViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalAppNavController.current

    GenreTvShowsScreenEffectHandler(viewModel, navController)

    GenreTvShowsScreenContent(
        state = state.value,
        interactionListener = viewModel
    )
}

@Composable
private fun GenreTvShowsScreenEffectHandler(
    viewModel: GenreTvShowsViewModel,
    navController: NavHostController
) {
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                GenreTvShowsEffects.NavigateBack -> if (!navController.popBackStack()) {
                    (navController.context as Activity).finish()
                }

                is GenreTvShowsEffects.NavigateToTvShowDetails -> {
                    navController.navigate(TvShowDetailsRoute(effect.id))
                }
            }
        }
    }
}


@Composable
fun GenreTvShowsScreenContent(
    state: GenreTvShowsScreenUiState,
    interactionListener: GenreTvShowsScreenInteractionListener,
) {
    val pagedTvShows = state.tvShows.collectAsLazyPagingItems()
    val screenState = when (pagedTvShows.loadState.refresh) {
        is LoadState.Loading -> ScreenState.LOADING
        is LoadState.Error -> ScreenState.NO_INTERNET
        else -> ScreenState.CONTENT
    }

    NovixScaffold(
        backgroundShapes = { BackgroundShapes() },
        snackBarHost ={
            NovixAnimatedSnackBarHost(
                data = state.snackBarData,
                onDismiss = interactionListener::onSnackBarDismiss,
            )
        }
    ) {
        Column(
            modifier = Modifier.navigationBarsPadding()
        ) {
            GenreTvShowsTopBar(state.title.toString())
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(), contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = screenState,
                    contentAlignment = Alignment.Center,
                    transitionSpec = { fadeIn() togetherWith fadeOut() }
                ) { currentState ->
                    when (currentState) {
                        ScreenState.LOADING -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                LoadingIndicator()
                            }
                        }

                        ScreenState.NO_INTERNET -> {
                            TVNetworkDisconnectionContact(
                                onRetryClick = { interactionListener.onRetryClick() },
                                modifier = Modifier.fillMaxSize(),
                            )
                        }

                        ScreenState.CONTENT -> {
                            LazyVerticalGrid(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 64.dp),
                                columns = GridCells.Fixed(5),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(
                                    count = pagedTvShows.itemCount,
                                    key = { index ->
                                        "${index}_${pagedTvShows[index]?.id}"
                                    }
                                ) { index ->
                                    val movie = pagedTvShows[index] ?: return@items
                                    FocusableMediaCard(
                                        imageUrl = movie.imageUrl,
                                        titleText = movie.title,
                                        onClick = { interactionListener.onTvShowClick(movie.id) }
                                    )
                                }

                                if (pagedTvShows.loadState.append is LoadState.Loading) {
                                    item(span = { GridItemSpan(maxLineSpan) }) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            LoadingIndicator()
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GenreTvShowsTopBar(
    genreName: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 36.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "TvShows\\ $genreName",

            style = Theme.textStyle.title.medium,
            color = Theme.colors.body
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.icon_logo),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(Theme.colors.primary)
            )
            Text(
                text = "Novix",
                style = Theme.textStyle.title.medium,
                color = Theme.colors.body
            )
        }
    }
}