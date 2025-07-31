package com.sanaa.presentation.screen.movieDetails

import android.app.Activity
import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.designsystem.design_system.component.button.NovixTextButton
import com.sanaa.designsystem.design_system.component.loading.NovixLoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixBackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.component.top_bar.NovixTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.presentation.modifier.fillWidthOfParent
import com.sanaa.presentation.navigation.ActorDetailsScreenRoute
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.MediaTypeParam
import com.sanaa.presentation.navigation.MovieCategoriesScreenRoute
import com.sanaa.presentation.navigation.MovieDetailsScreenRoute
import com.sanaa.presentation.navigation.ReviewsScreenRoute
import com.sanaa.presentation.screen.movieDetails.components.MoreLikeThisCard
import com.sanaa.presentation.screen.series.components.CastComponent
import com.sanaa.presentation.shared_component.BottomContainer
import com.sanaa.presentation.shared_component.DotSeparator
import com.sanaa.presentation.shared_component.IconWithText
import com.sanaa.presentation.shared_component.ImageSlider
import com.sanaa.presentation.shared_component.InfoSection
import com.sanaa.presentation.shared_component.NovixAnimatedSnackBarHost
import com.sanaa.presentation.shared_component.OverviewSection
import com.sanaa.presentation.shared_component.RateBottomSheet
import com.sanaa.presentation.shared_component.RequestToLoginBottomSheet
import com.sanaa.presentation.util.getCurrentLocale
import com.sanaa.presentation.util.toLocalizedDigits
import kotlin.time.Duration.Companion.hours
import com.sanaa.designsystem.R as designR

@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val submitRatingSuccessMsg = stringResource(R.string.submit_rating_successfully)
    val submitRatingFailedMsg = stringResource(R.string.submit_rating_failed)
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val navController = LocalNavControllerProvider.current
    var snack by remember { mutableStateOf<SnackData?>(null) }
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MovieDetailsUiEffect.OpenTrailer -> {
                    val intent = Intent(Intent.ACTION_VIEW, effect.url?.toUri())
                    context.startActivity(intent)
                }

                is MovieDetailsUiEffect.NavigateBack -> {
                    if (!navController.popBackStack()) {
                        (navController.context as Activity).finish()
                    }
                }

                is MovieDetailsUiEffect.NavigateToReviewsScreen -> {
                    navController.navigate(
                        ReviewsScreenRoute(effect.movieId, MediaTypeParam.MOVIE).route()
                    )
                }

                is MovieDetailsUiEffect.NavigateToAnotherMovieDetails -> {
                    navController.navigate(MovieDetailsScreenRoute(effect.movieId).route())
                }

                is MovieDetailsUiEffect.NavigateToActorScreen -> {
                    navController.navigate(ActorDetailsScreenRoute(effect.actorId).route())
                }

                is MovieDetailsUiEffect.NavigateToMovieCategoriesScreen -> {
                    navController.navigate(
                        MovieCategoriesScreenRoute(
                            effect.categoryId, effect.categoryName
                        ).route()
                    )
                }
                is MovieDetailsUiEffect.ShowSuccessSnackBar -> {
                    snack = SnackData(message = submitRatingSuccessMsg, isError = false)
                }

                is MovieDetailsUiEffect.ShowErrorSnackBar -> {
                    snack = SnackData(submitRatingFailedMsg, isError = true)
                }

                MovieDetailsUiEffect.NavigateToLogin -> TODO()
            }
        }
    }

    Box{
        MovieDetailsContent(
            state = state, interactionListener = viewModel
        )

        NovixAnimatedSnackBarHost(
            data = snack,
            onDismiss = { snack = null }
        )
    }


}

@Composable
fun MovieDetailsContent(
    state: MovieDetailsUiState,
    interactionListener: MovieDetailsScreenInteractionListener,
) {

    val pagedSimilarMovies = state.similarMovies.collectAsLazyPagingItems()
    val context = LocalContext.current
    val locale = remember { getCurrentLocale(context) }

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
                targetState = state.isLoading || state.noInternetConnection,
                modifier = Modifier.align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                if (it) {
                    if (state.noInternetConnection) {
                        NetworkDisconnectionContact(
                            onRetryClick = { interactionListener.onRetryLoadDetails() },
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            NovixLoadingIndicator()
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxSize(),
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
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        state.movieDetails.genres.forEachIndexed { index, genre ->
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                modifier = Modifier.clickable(
                                                    indication = null,
                                                    interactionSource = remember { MutableInteractionSource() }
                                                ) {
                                                    interactionListener.onGenreClicked(genre)
                                                }
                                            ) {
                                                Text(
                                                    text = genre.name,
                                                    style = Theme.textStyle.label.small,
                                                    color = Theme.colors.body,
                                                )
                                                if (index < state.movieDetails.genres.lastIndex) {
                                                    DotSeparator()
                                                }
                                            }
                                        }
                                    }
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
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
                                            state.movieDetails.duration?.let { duration ->
                                                val hours = duration.inWholeHours
                                                val minutes =
                                                    (duration - hours.hours).inWholeMinutes

                                                val durationText = buildString {
                                                    if (hours > 0) append("${hours.toInt().toLocalizedDigits(locale)}${stringResource(R.string.hours_label)} ")
                                                    if (minutes > 0) append("${minutes.toInt().toLocalizedDigits(locale)}${stringResource(R.string.minutes_label)}")
                                                }.trim()


                                                IconWithText(
                                                    iconRes = R.drawable.icon_duration,
                                                    contentDescription = null,
                                                    text = durationText,
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
                                    }
                                    NovixTextButton(
                                        text = stringResource(id = R.string.view_reviews),
                                        textColor = Theme.colors.primary,
                                        onClick = { interactionListener.onShowReviewsClick(state.movieDetails.id) })
                                }
                            }
                        }

                        item(span = { GridItemSpan(maxLineSpan) }) {
                            if (state.movieDetails.overview.isNotEmpty()) {
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
                        if (pagedSimilarMovies.itemCount > 0) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Text(
                                    text = stringResource(id = R.string.more_like_this),
                                    color = Theme.colors.title,
                                    style = Theme.textStyle.title.medium,
                                    modifier = Modifier.padding(bottom = 4.dp, top = 16.dp)
                                )
                            }

                            items(
                                count = pagedSimilarMovies.itemCount,
                                key = { index ->
                                    val movie = pagedSimilarMovies[index]
                                    "${index}-${movie?.id}"
                                }
                            ) { index ->
                                val item = pagedSimilarMovies[index] ?: return@items
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
                onSetRateClicked = { interactionListener.onRateMovieClick() }
            )
            if (state.showRateBottomSheet) {
                RateBottomSheet(
                    isRateSelected = state.hasUserSelectedRate,
                    imdbRating = state.imdbRating,
                    onDismiss = interactionListener::onDismissRateBottomSheet,
                    isVisible = state.showRateBottomSheet,
                    onSubmitButtonClick = interactionListener::onSubmitRateBottomSheet,
                    onRatingChanged = interactionListener::onRatingChanged
                )
            }
            if (state.showLoginBottomSheetToAddToList) {
                RequestToLoginBottomSheet(
                    onDismiss = { interactionListener.onDismissLoginBottomSheet() },
                    isVisible = state.showLoginBottomSheetToAddToList
                )
            }
        }

    }
}

