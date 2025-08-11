package com.sanaa.tvapp.presentation.screens.mediaDetails.actorScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.presentation.screen.movieDetails.SnackData
import com.sanaa.presentation.shared_component.DotSeparator
import com.sanaa.presentation.shared_component.IconWithText
import com.sanaa.presentation.shared_component.NovixAnimatedSnackBarHost
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.DetailsHeaderSection
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.DetailsTopBar
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.ImagesSlider
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.MoviesSlider
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.TvShowsSlider

@Composable
fun ActorScreen(
    viewModel: ActorViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ActorScreenEffects.NavigateBack -> TODO()
                is ActorScreenEffects.NavigateToMovieDetails -> TODO()
                is ActorScreenEffects.NavigateToSeriesDetails -> TODO()
                ActorScreenEffects.NavigateToLogin -> TODO()
            }
        }
    }

    ActorScreenContent(
        state = state,
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

    var snack by remember { mutableStateOf<SnackData?>(null) }

    NovixScaffold(
        backgroundShapes = { },
    ) {
        Box(modifier = modifier.systemBarsPadding()) {

            AnimatedContent(
                targetState = state.isLoading || state.noInternetConnection,
                modifier = Modifier.align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                if (it) {
                    if (state.noInternetConnection) {
                        NetworkDisconnectionContact(
                            onRetryClick = { listener.onRetryClicked() },
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
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {

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
                        if (state.galleryImageUrls.isNotEmpty()) {
                            ImagesSlider(
                                title = stringResource(R.string.gallery),
                                images = state.galleryImageUrls,
                            )
                        }

                        if (state.topMovies.isNotEmpty()) {
                            MoviesSlider(
                                title = stringResource(R.string.top_movie_picks),
                                movies = state.topMovies,
                                onCardClick = listener::onMovieClicked
                            )
                        }
                        if (state.topTvShows.isNotEmpty()) {
                            TvShowsSlider(
                                title = stringResource(R.string.top_series_picks),
                                tvShows = state.topTvShows,
                                onCardClick = listener::onTvShowClicked
                            )
                        }
                    }
                }
                DetailsTopBar(onBackClick = listener::onBackClicked)

                NovixAnimatedSnackBarHost(
                    data = snack, onDismiss = { snack = null })
            }
        }
    }
}

