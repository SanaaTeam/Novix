package com.sanaa.presentation.screen.movie_details

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.design_system.component.button.TextButton
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.designsystem.design_system.component.loading.NovixLoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixBackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.component.DotSeparator
import com.sanaa.presentation.component.IconWithText
import com.sanaa.presentation.component.ImageSlider
import com.sanaa.presentation.component.InfoSection
import com.sanaa.presentation.component.OverviewSection
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.MovieDetailsScreenRoute
import com.sanaa.presentation.screen.movie_details.components.MoreLikeThisSection
import com.sanaa.presentation.screen.series.components.BottomContainer
import com.sanaa.presentation.screen.series.components.CastComponent
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import com.sanaa.designsystem.R as designR
import com.sanaa.presentation.R as presentationR

@Composable
fun MovieDetailsScreen(
    movieId: Int,
    viewModel: MovieDetailsViewModel = koinViewModel(parameters = { parametersOf(movieId) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsStateWithLifecycle(null)

    val navController = LocalNavControllerProvider.current

    LaunchedEffect(effect) {
        when (val e = effect) {
            is MovieDetailsUiEffect.OpenTrailer -> {
                val intent = Intent(Intent.ACTION_VIEW, e.url?.toUri())
                navController.context.startActivity(intent)
            }

            is MovieDetailsUiEffect.NavigateBack -> {
                navController.popBackStack()
            }

            is MovieDetailsUiEffect.NavigateToAnotherMovieDetails -> {
                navController.navigate(MovieDetailsScreenRoute(e.movieId).route())
            }

            else -> Unit
        }
    }
    MovieDetailsContent(
        state = state,
        interactionListener = viewModel
    )

}

@Composable
fun MovieDetailsContent(
    state: MovieDetailsUiState,
    interactionListener: MovieDetailsScreenInteractionListener,
) {
    NovixScaffold(
        backgroundShapes = { NovixBackgroundShapes() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AppTopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(designR.drawable.icon_arrow_back),
                        onClick = { interactionListener.onBackClick() }
                    )
                },
                rightContent = {
                    SaveIconChip(
                        isSaved = state.movieDetails.isBookmarked,
                        onClick = { interactionListener.onBookmarkClick(state.movieDetails.id) }
                    )
                },
                modifier = Modifier
                    .systemBarsPadding()
                    .zIndex(10f)
            )
                    LazyColumn(
                        modifier = Modifier
                            .padding(bottom = 112.dp)
                            .fillMaxSize()
                    ) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                ImageSlider(
                                    images = state.imagesUrls,
                                    contentDescription = state.movieDetails.title,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                InfoSection(
                                    title = state.movieDetails.title,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 220.dp)
                                        .padding(horizontal = 16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(top = 16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        state.movieDetails.genres.forEachIndexed { index, genre ->
                                            Text(
                                                text = genre,
                                                style = Theme.textStyle.label.small,
                                                color = Theme.colors.body
                                            )
                                            if (index < state.movieDetails.genres.lastIndex) {
                                                DotSeparator()
                                            }
                                        }
                                    }
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconWithText(
                                            iconRes = designR.drawable.star,
                                            contentDescription = null,
                                            text = state.movieDetails.rating,
                                            tint = Theme.colors.statusColors.yellowAccent
                                        )
                                        DotSeparator()
                                        IconWithText(
                                            iconRes = presentationR.drawable.icon_duration,
                                            contentDescription = null,
                                            text = state.movieDetails.duration,
                                            tint = Theme.colors.body
                                        )
                                        DotSeparator()
                                        IconWithText(
                                            iconRes = presentationR.drawable.icon_calender,
                                            contentDescription = null,
                                            text = state.movieDetails.releaseDate,
                                            tint = Theme.colors.body
                                        )
                                    }
                                    TextButton(
                                        text = stringResource(id = presentationR.string.view_reviews),
                                        textColor = Theme.colors.primary,
                                        onClick = {}
                                    )
                                }
                            }
                        }

                        item {
                            OverviewSection(
                                overview = state.movieDetails.overview,
                                onReadMore = { interactionListener.onReadMoreClick() },
                                modifier = Modifier.padding(16.dp),
                                titleResId = presentationR.string.overview
                            )
                        }

                        item {
                            CastComponent(
                                cast = state.cast,
                                onActorClicked = {}
                            )
                        }
                        item {
                            MoreLikeThisSection(
                                similarMovies = state.similarMovies,
                                onBookmarkClick = interactionListener::onBookmarkClick,
                                onSimilarMovieClick = interactionListener::onSimilarMovieClick,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }



            BottomContainer(
                onPlayTrailerClicked = { interactionListener.onWatchTrailerClick() },
                trailerUrl = state.movieDetails.trailerUrl,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

