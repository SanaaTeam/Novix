package com.sanaa.tvapp.presentation.screens.mediaDetails.movieScreen

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.R
import com.sanaa.tvapp.presentation.components.LoginDialog
import com.sanaa.tvapp.presentation.components.RateDialog
import com.sanaa.tvapp.presentation.components.TVNetworkDisconnectionContact
import com.sanaa.tvapp.presentation.screens.login.LoginActivity
import com.sanaa.tvapp.presentation.screens.login.components.NovixAnimatedSnackBarHost
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.CastSlider
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.DetailsHeaderSection
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.DotSeparator
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.GenresRow
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.IconWithText
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.OverviewSection
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.SimilarMoviesSlider
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.TrailerAndRateSection
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.MovieDetailsUiModel
import com.sanaa.tvapp.presentation.screens.navigation.LocalAppNavController
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.ActorDetailsRoute
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.MovieDetailsRoute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest


@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    MovieDetailsEffectsHandler(effect = viewModel.effect)

    MovieDetailsContent(
        state = state.value,
        interactionListener = viewModel
    )
}

@Composable
private fun MovieDetailsEffectsHandler(
    effect: Flow<MovieDetailsScreenUiEffect>,
) {
    val navController = LocalAppNavController.current
    val context = LocalContext.current

    val loginLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            (context as? Activity)?.recreate()
        }
    }
    LaunchedEffect(Unit) {
        effect.collectLatest {
            when (it) {
                is MovieDetailsScreenUiEffect.NavigateToActorScreen -> navController.navigate(
                    ActorDetailsRoute(it.actorId)
                )

                is MovieDetailsScreenUiEffect.NavigateToAnotherMovieDetails -> {
                    navController.navigate(MovieDetailsRoute(it.movieId))
                }

                MovieDetailsScreenUiEffect.NavigateToLogin -> {

                    val intent = Intent(context, LoginActivity::class.java)
                    loginLauncher.launch(intent)
                }

                is MovieDetailsScreenUiEffect.OpenTrailer -> {
                    val intent = Intent(Intent.ACTION_VIEW, it.url?.toUri())
                    context.startActivity(intent)
                }
            }
        }
    }
}

@Composable
fun MovieDetailsContent(
    state: MovieDetailsScreenUiState,
    interactionListener: MovieDetailsViewModel,
) {
    val moviesPagingData: LazyPagingItems<MovieDetailsUiModel> =
        state.similarMovies.collectAsLazyPagingItems()
    val focusRequester = remember { FocusRequester() }
    var hasRequestedFocus by remember { mutableStateOf(false) }


    NovixScaffold(
        backgroundShapes = {},
        modifier = Modifier.systemBarsPadding(),
        snackBarHost = {
            NovixAnimatedSnackBarHost(
                data = state.snackBarData,
                onDismiss = interactionListener::onSnackDismissRequested,
                modifier = Modifier.statusBarsPadding()
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AnimatedContent(
                targetState = state.isLoading || state.noInternetConnection,
                modifier = Modifier.align(Alignment.Center),
                contentAlignment = Alignment.Center
            )
            { shouldShowLoadingOrError ->
                if (shouldShowLoadingOrError) {
                    if (state.noInternetConnection) {
                        TVNetworkDisconnectionContact(
                            onRetryClick = { interactionListener.onRetryLoadDetails() },
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        LoadingIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    )
                    {
                        Column(
                            modifier = Modifier
                                .verticalScroll(
                                    state = rememberScrollState()
                                )
                        ) {
                            Card(
                                modifier = Modifier
                                    .size(0.dp)
                                    .focusRequester(focusRequester)
                                    .focusable()
                                    .focusTarget()
                                    .onGloballyPositioned {
                                        if (!hasRequestedFocus) {
                                            hasRequestedFocus = true
                                            focusRequester.requestFocus()
                                        }
                                    },
                                onClick = interactionListener::onReadMoreClicked,
                                colors = CardDefaults.colors(
                                    containerColor = Color.Transparent,
                                    contentColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    focusedContentColor = Color.Transparent,
                                    pressedContainerColor = Color.Transparent,
                                    pressedContentColor = Color.Transparent
                                )
                            ) {}
                            DetailsHeaderSection(
                                backgroundImageUrl = state.movieDetails.posterUrl ?: "",
                                title = state.movieDetails.title,
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    GenresRow(genres = state.movieDetails.genres)
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                        {
                                            state.movieDetails.rating.let {
                                                IconWithText(
                                                    iconRes = R.drawable.icon_star,
                                                    text = state.movieDetails.rating ?: "",
                                                    textColor = Theme.colors.title,
                                                    contentDescription = state.movieDetails.rating,
                                                    tint = Theme.colors.statusColors.yellowAccent
                                                )

                                                DotSeparator()
                                            }
                                            state.movieDetails.releaseDate.let { releaseDate ->
                                                IconWithText(
                                                    text = releaseDate,
                                                    iconRes = R.drawable.icon_calender,
                                                    contentDescription = releaseDate,
                                                    tint = Theme.colors.hint
                                                )

                                                DotSeparator()
                                            }

                                            state.movieDetails.duration.let { duration ->
                                                IconWithText(
                                                    text = duration,
                                                    iconRes = R.drawable.icon_duration,
                                                    contentDescription = duration,
                                                    tint = Theme.colors.hint
                                                )
                                            }
                                        }
                                    }

                                   if (state.movieDetails.overview.isNotEmpty()){
                                        OverviewSection(
                                            titleResId = R.string.overview,
                                            overview = state.movieDetails.overview,
                                            onReadMore = interactionListener::onReadMoreClicked,
                                            isExpanded = state.isExpandedOverView,
                                        )
                                    }

                                    TrailerAndRateSection(
                                        trailerUrl = state.movieDetails.trailerUrl,
                                        onPlayTrailerClicked = {
                                            interactionListener.onWatchTrailerClick(
                                                state.movieDetails.trailerUrl.orEmpty()
                                            )
                                        },
                                        onRateClicked = interactionListener::onRateMovieClick,
                                        showRateButton = !state.isRatingSubmitted
                                    )
                                }
                            }

                            if (state.cast.isNotEmpty()) {
                                CastSlider(
                                    cast = state.cast,
                                    onActorCardClicked = interactionListener::onActorCardClick
                                )
                            }
                            if (moviesPagingData.itemCount > 0) {
                                SimilarMoviesSlider(
                                    moviesPagingData = moviesPagingData,
                                    onMovieCardClicked = interactionListener::onSimilarMovieClick
                                )
                            }
                        }

                    }
                }
            }

            if (state.showRateDialog) {
                RateDialog(
                    currentRating = state.rating,
                    onRatingChanged = interactionListener::onRatingChange,
                    onDismissRequest = interactionListener::onDismissRateDialog,
                    onSubmitRating = interactionListener::onSummitRateClick
                )
            }
            if (state.showLoginDialog && !state.isUserLoggedIn) {
                LoginDialog(
                    onDismissRequest = interactionListener::onDismissLoginBottomSheet,
                    onLoginClicked = interactionListener::onLoginButtonClick
                )
            }
        }
    }
}