package com.sanaa.tvapp.presentation.screens.mediaDetails.actorScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.R
import com.sanaa.tvapp.presentation.screens.login.components.NovixAnimatedSnackBarHost
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.DetailsHeaderSection
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.DotSeparator
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.IconWithText
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.ImagesSlider
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.TopMoviesSlider
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.TopTvShowsSlider
import com.sanaa.tvapp.presentation.screens.navigation.LocalAppNavController
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.MovieDetailsRoute
import com.sanaa.tvapp.state.SnackData
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ActorScreen(
    viewModel: ActorViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalAppNavController.current

    ActorDetailsEffectHandler(viewModel, navController)

    ActorScreenContent(
        state = state,
        interactionListener = viewModel,
    )

}

@Composable
private fun ActorDetailsEffectHandler(
    viewModel: ActorViewModel,
    navController: NavHostController,
) {
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ActorScreenEffects.NavigateToMovieDetails -> navController.navigate(
                    MovieDetailsRoute(effect.movieId)
                )

                is ActorScreenEffects.NavigateToSeriesDetails -> navController.navigate(
                    ScreensRoute.TvShowDetailsRoute(
                        effect.seriesId
                    )
                )
            }
        }
    }
}

@Composable
private fun ActorScreenContent(
    state: ActorScreenUiState,
    interactionListener: ActorsScreenInteractionListener,
) {

    var snack by remember { mutableStateOf<SnackData?>(null) }

    NovixScaffold(
        backgroundShapes = {},
        snackBarHost = {
            NovixAnimatedSnackBarHost(
                data = state.snackBarData,
                onDismiss = interactionListener::onSnackDismissRequested,
                modifier = Modifier.statusBarsPadding()
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            AnimatedContent(
                targetState = state.isLoading || state.noInternetConnection,
                modifier = Modifier.align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                if (it) {
                    if (state.noInternetConnection) {
                        NetworkDisconnectionContact(
                            onRetryClick = { interactionListener.onRetryClicked() },
                            modifier = Modifier.fillMaxSize(),
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            LoadingIndicator()
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        DetailsHeaderSection(
                            state.actor.imageUrl.orEmpty(),
                            title = state.actor.name,
                        ) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {},
                                colors = CardDefaults.colors(
                                    containerColor = Color.Transparent,
                                ),
                                scale = CardDefaults.scale(
                                    scale = 1f,
                                    focusedScale = 1f,
                                    pressedScale = 1.2f
                                ),
                                border = CardDefaults.border(
                                    focusedBorder = Border.None,
                                    pressedBorder = Border.None
                                )
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                )
                                {
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                    ) {
                                        ActorInfo(state)
                                    }

                                    state.actor.biography?.let { bio ->
                                        Text(
                                            text = bio,
                                            style = Theme.textStyle.body.small,
                                            color = Theme.colors.body,
                                            maxLines = 7
                                        )
                                    }
                                }
                            }

                        }

                        ActorScreenSliders(state, interactionListener)
                    }
                }

                NovixAnimatedSnackBarHost(
                    data = snack, onDismiss = { snack = null }
                )
            }
        }
    }
}

@Composable
private fun ActorScreenSliders(
    state: ActorScreenUiState,
    listener: ActorsScreenInteractionListener,
) {
    if (state.galleryImageUrls.isNotEmpty()) {
        ImagesSlider(
            title = stringResource(R.string.Gallery),
            images = state.galleryImageUrls,
        )
    }

    if (state.topMovies.isNotEmpty()) {
        TopMoviesSlider(
            movies = state.topMovies,
            onMovieCardClicked = listener::onMovieClicked,
        )
    }
    if (state.topTvShows.isNotEmpty()) {
        TopTvShowsSlider(
            title = stringResource(R.string.series),
            tvShows = state.topTvShows,
            onTvShowCardClicked = listener::onTvShowClicked
        )
    }
}

@Composable
private fun ActorInfo(state: ActorScreenUiState) {
    state.actor.department?.let {
        AppText(
            text = it,
            style = Theme.textStyle.label.small,
            color = Theme.colors.body,
        )
    }

    state.actor.placeOfBirth?.let {
        DotSeparator()
        IconWithText(
            iconRes = R.drawable.location,
            text = it,
            contentDescription = "",
            tint = Theme.colors.body
        )
    }

    state.actor.lifeSpan?.let {
        DotSeparator()
        IconWithText(
            iconRes = R.drawable.birthday_cake,
            text = it,
            contentDescription = "",
            tint = Theme.colors.body
        )
    }
}



