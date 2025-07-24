package com.sanaa.presentation.screen.movie_details

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import com.sanaa.designsystem.design_system.component.button.NovixTextButton
import com.sanaa.designsystem.design_system.component.loading.NovixLoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixBackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.NovixTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.presentation.component.DotSeparator
import com.sanaa.presentation.component.IconWithText
import com.sanaa.presentation.component.ImageSlider
import com.sanaa.presentation.component.InfoSection
import com.sanaa.presentation.component.OverviewSection
import com.sanaa.presentation.component.RequestToLoginBottomSheet
import com.sanaa.presentation.modifier.fillWidthOfParent
import com.sanaa.presentation.navigation.ActorDetailsScreenRoute
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.MediaTypeParam
import com.sanaa.presentation.navigation.MovieCategoriesScreenRoute
import com.sanaa.presentation.navigation.MovieDetailsScreenRoute
import com.sanaa.presentation.navigation.ReviewsScreenRoute
import com.sanaa.presentation.screen.movie_details.components.MoreLikeThisCard
import com.sanaa.presentation.screen.series.components.BottomContainer
import com.sanaa.presentation.screen.series.components.CastComponent
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import com.sanaa.designsystem.R as designR

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
                if (!navController.popBackStack()) {
                    (navController.context as Activity).finish()
                }
            }

            is MovieDetailsUiEffect.NavigateToReviewsScreen -> {
                Log.d("TAG", "MovieDetailsScreen: ${e.movieId}")
                navController.navigate(
                    ReviewsScreenRoute(e.movieId, MediaTypeParam.MOVIE).route()
                )
            }

            is MovieDetailsUiEffect.NavigateToAnotherMovieDetails -> {
                navController.navigate(MovieDetailsScreenRoute(e.movieId).route())
            }

            is MovieDetailsUiEffect.NavigateToActorScreen -> {
                navController.navigate(ActorDetailsScreenRoute(e.actorId).route())
            }

            is MovieDetailsUiEffect.NavigateToMovieCategoriesScreen -> {
                navController.navigate(
                    MovieCategoriesScreenRoute(
                        e.categoryId, e.categoryName
                    ).route()
                )
            }

            else -> Unit
        }
    }
    MovieDetailsContent(
        state = state, interactionListener = viewModel
    )

}

@Composable
fun MovieDetailsContent(
    state: MovieDetailsUiState,
    interactionListener: MovieDetailsScreenInteractionListener,
) {
    NovixScaffold(
        backgroundShapes = { NovixBackgroundShapes() }) {
        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxSize()

        ) {
            NovixTopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(R.drawable.icon_back),
                        onClick = { interactionListener.onBackClick() })
                }, rightContent = {
                    TopBarClickableIcon(
                        icon = painterResource(R.drawable.icon_save), onClick = {
                            interactionListener.onBookmarkClick(state.movieDetails.id)
                        })
                }, modifier = Modifier
                    .systemBarsPadding()
                    .zIndex(10f)
            )
            AnimatedContent(
                targetState = state.isLoading,
                modifier = Modifier.align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                if (it) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        NovixLoadingIndicator()
                    }
                } else {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 112.dp),
                        columns = GridCells.Adaptive(minSize = 120.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Box(modifier = Modifier.fillWidthOfParent(16.dp)) {
                                ImageSlider(
                                    images = state.imagesUrls,
                                    contentDescription = state.movieDetails.title,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                InfoSection(
                                    title = state.movieDetails.title,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 208.dp)
                                        .padding(horizontal = 16.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        state.movieDetails.genres.forEachIndexed { index, genre ->
                                            Text(
                                                text = genre.name,
                                                style = Theme.textStyle.label.small,
                                                color = Theme.colors.body,
                                                modifier = Modifier.clickable {
                                                    interactionListener.onGenreClicked(genre)
                                                })
                                            if (index < state.movieDetails.genres.lastIndex) {
                                                DotSeparator()
                                            }
                                        }
                                    }
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        state.movieDetails.rating?.let {
                                            IconWithText(
                                                iconRes = designR.drawable.star,
                                                contentDescription = null,
                                                textColor = Theme.colors.title,
                                                text = state.movieDetails.rating,
                                                tint = Theme.colors.statusColors.yellowAccent
                                            )
                                            DotSeparator()
                                        }
                                        state.movieDetails.duration?.let {
                                            IconWithText(
                                                iconRes = R.drawable.icon_duration,
                                                contentDescription = null,
                                                text = stringResource(
                                                    R.string.m, state.movieDetails.duration
                                                ),
                                                tint = Theme.colors.body
                                            )
                                            DotSeparator()
                                        }
                                        IconWithText(
                                            iconRes = R.drawable.icon_calender,
                                            contentDescription = null,
                                            text = state.movieDetails.releaseDate,
                                            tint = Theme.colors.body
                                        )
                                    }
                                    NovixTextButton(
                                        text = stringResource(id = R.string.view_reviews),
                                        textColor = Theme.colors.primary,
                                        onClick = { interactionListener.onShowReviewsClick(state.movieDetails.id) })
                                }
                            }
                        }

                        item(span = { GridItemSpan(maxLineSpan) }) {
                            state.movieDetails.overview?.let {
                                OverviewSection(
                                    overview = state.movieDetails.overview,
                                    onReadMore = { interactionListener.onReadMoreClick() },
                                    modifier = Modifier.padding(vertical = 16.dp),
                                    titleResId = R.string.overview
                                )
                            }
                        }

                        item(span = { GridItemSpan(maxLineSpan) }) {
                            if (state.cast.isNotEmpty()) CastComponent(
                                casts = state.cast,
                                onActorClicked = interactionListener::onActorCardClick,
                                modifier = Modifier.fillWidthOfParent(16.dp)
                            )
                        }
                        if (state.similarMovies.isNotEmpty()) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Text(
                                    text = stringResource(id = R.string.more_like_this),
                                    color = Theme.colors.title,
                                    style = Theme.textStyle.title.medium,
                                    modifier = Modifier.padding(bottom = 4.dp, top = 16.dp)
                                )
                            }

                            itemsIndexed(
                                state.similarMovies,
                                key = { index, item -> item.id }) { index, item ->
                                MoreLikeThisCard(
                                    movie = item,
                                    modifier = Modifier.padding(bottom = 12.dp),
                                    onBookmarkClick = { interactionListener.onBookmarkClick(item.id) },
                                    onMovieClick = { interactionListener.onSimilarMovieClick(item.id) },
                                )
                            }
                        }
                    }

                }
            }
            BottomContainer(
                onPlayTrailerClicked = { interactionListener.onWatchTrailerClick() },
                trailerUrl = state.movieDetails.trailerUrl,
                modifier = Modifier.align(Alignment.BottomCenter),
                onSetRateClicked = { interactionListener.onRateMovieClick() })


            if (state.showLoginBottomSheet) {
                RequestToLoginBottomSheet(
                    onDismiss = { interactionListener.onDismissLoginBottomSheet() },
                    isVisible = state.showLoginBottomSheet)
            }

        }

    }
}

