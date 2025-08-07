package com.sanaa.presentation.screen.movieDetails

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.api.launchAuthActivityForResult
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.BackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.presentation.bottomsheets.addEditBookmark.AddBookmarkListBottomSheet
import com.sanaa.presentation.bottomsheets.saveToListBottomsheet.SaveToListBottomSheet
import com.sanaa.presentation.model.MovieUiModel
import com.sanaa.presentation.navigation.ActorDetailsScreenRoute
import com.sanaa.presentation.navigation.DetailsApiEntryPoint
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.MediaTypeParam
import com.sanaa.presentation.navigation.MovieCategoriesScreenRoute
import com.sanaa.presentation.navigation.MovieDetailsScreenRoute
import com.sanaa.presentation.navigation.ReviewsScreenRoute
import com.sanaa.presentation.screen.movieDetails.components.MovieDetailsGridContent
import com.sanaa.presentation.shared_component.BottomContainer
import com.sanaa.presentation.shared_component.NovixAnimatedSnackBarHost
import com.sanaa.presentation.shared_component.RateBottomSheet
import com.sanaa.presentation.shared_component.RequestToLoginBottomSheet
import com.sanaa.presentation.util.getCurrentLocale
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.collectLatest
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
    val selectedMedia = state.selectedMediaId

    HandleMovieDetailsEffects(
        viewModel = viewModel,
        context = context,
        navController = navController,
        onShowSuccess = { snack = SnackData(submitRatingSuccessMsg, isError = false) },
        onShowError = { snack = SnackData(submitRatingFailedMsg, isError = true) }
    )

    Box {
        MovieDetailsContent(
            state = state,
            interactionListener = viewModel
        )

        NovixAnimatedSnackBarHost(
            data = snack,
            onDismiss = { snack = null }
        )

        SaveToListBottomSheet(
            isVisible = state.showSaveToListBottomSheet,
            mediaId = selectedMedia?.toLong() ?: 0,
            onDismiss = viewModel::onDismissSaveToListBottomSheet,
            onCreateNewListClick = viewModel::onCreateNewListClick,
            onSuccess = {
                snack = SnackData(
                    message = "Added to list successfully",
                    isError = false
                )
            },
            onFailure = {
                snack = SnackData(
                    message = "Added to list failed",
                    isError = true
                )
            },
        )
        if (state.showAddListBottomSheet && selectedMedia != null) {
            AddBookmarkListBottomSheet(
                isVisible = state.showAddListBottomSheet,
                onDismiss = viewModel::onDismissAddListBottomSheet,
                mediaId = selectedMedia
            )
        }
    }
}

@Composable
private fun HandleMovieDetailsEffects(
    viewModel: MovieDetailsViewModel,
    context: Context,
    navController: NavController,
    onShowSuccess: () -> Unit,
    onShowError: () -> Unit
) {
    val currentContext by rememberUpdatedState(context)
    val currentNavController by rememberUpdatedState(navController)


    val authApi = EntryPointAccessors.fromApplication(
        context,
        DetailsApiEntryPoint::class.java
    ).authenticationApi()

    val launcher = launchAuthActivityForResult()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is MovieDetailsUiEffect.OpenTrailer -> {
                    effect.url?.toUri()?.let { uri ->
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        currentContext.startActivity(intent)
                    }
                }

                is MovieDetailsUiEffect.NavigateBack -> {
                    if (!currentNavController.popBackStack()) {
                        (currentNavController.context as? Activity)?.finish()
                    }
                }

                is MovieDetailsUiEffect.NavigateToReviewsScreen -> {
                    currentNavController.navigate(
                        ReviewsScreenRoute(effect.movieId, MediaTypeParam.MOVIE).route()
                    )
                }

                is MovieDetailsUiEffect.NavigateToAnotherMovieDetails -> {
                    currentNavController.navigate(MovieDetailsScreenRoute(effect.movieId).route())
                }

                is MovieDetailsUiEffect.NavigateToActorScreen -> {
                    currentNavController.navigate(ActorDetailsScreenRoute(effect.actorId).route())
                }

                is MovieDetailsUiEffect.NavigateToMovieCategoriesScreen -> {
                    currentNavController.navigate(
                        MovieCategoriesScreenRoute(effect.categoryId, effect.categoryName).route()
                    )
                }

                is MovieDetailsUiEffect.ShowSuccessSnackBar -> onShowSuccess()
                is MovieDetailsUiEffect.ShowErrorSnackBar -> onShowError()

                MovieDetailsUiEffect.NavigateToLogin -> {
                    launcher.launch(authApi.getLaunchIntent(context))
                }
            }
        }
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

    val lazyState = rememberLazyGridState()
    var shouldShowBackground by remember { mutableStateOf(false) }
    val animatedColor by animateColorAsState(
        targetValue = if (shouldShowBackground) Theme.colors.surface else Color.Transparent,
        animationSpec = tween(durationMillis = 500, easing = EaseInOut),
    )

    LaunchedEffect(lazyState) {
        snapshotFlow {
            if (lazyState.firstVisibleItemIndex == 0) {
                lazyState.firstVisibleItemScrollOffset
            } else {
                Int.MAX_VALUE
            }
        }.collect { totalScrollPosition ->
            shouldShowBackground = totalScrollPosition > 200
        }
    }

    NovixScaffold(
        backgroundShapes = { BackgroundShapes() }) {
        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxSize()
        ) {

            MovieTopBar(
                interactionListener = interactionListener,
                movie = state.movieDetails,
                modifier = Modifier.background(color = animatedColor)
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
                            LoadingIndicator()
                        }
                    }
                } else {
                    MovieDetailsGridContent(
                        state = state,
                        pagedSimilarMovies = pagedSimilarMovies,
                        locale = locale,
                        interactionListener = interactionListener,
                        lazyState = lazyState
                    )
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
            if (state.showLoginBottomSheet) {
                val title = when (state.loginPromptType) {
                    LoginPromptType.RATE -> stringResource(R.string.rate_it)
                    LoginPromptType.BOOKMARK -> stringResource(R.string.add_to_list)
                    else -> stringResource(R.string.add_to_list)
                }

                val text = when (state.loginPromptType) {
                    LoginPromptType.RATE -> stringResource(R.string.please_login_to_rate_your_favorite_items)
                    LoginPromptType.BOOKMARK -> stringResource(R.string.request_login)
                    else -> stringResource(R.string.request_login)
                }
                RequestToLoginBottomSheet(
                    onDismiss = { interactionListener.onDismissLoginBottomSheet() },
                    isVisible = state.showLoginBottomSheet,
                    title = title,
                    text = text,
                    onLoginButtonClick = {
                        interactionListener.onLoginButtonClick()
                    }
                )
            }
        }

    }
}


@Composable
fun MovieTopBar(
    interactionListener: MovieDetailsScreenInteractionListener,
    movie: MovieUiModel,
    modifier: Modifier = Modifier,
) {
    TopBar(
        leftContent = {
            TopBarClickableIcon(
                icon = painterResource(designR.drawable.icon_back),
                onClick = { interactionListener.onBackClick() }
            )
        },
        rightContent = {
            TopBarClickableIcon(
                icon = if (movie.isBookmarked)
                    painterResource(com.sanaa.designsystem.R.drawable.icon_saved)
                else
                    painterResource(R.drawable.icon_save),
                onClick = { interactionListener.onBookmarkClick(movie) }
            )
        },
        modifier = modifier
            .systemBarsPadding()
            .zIndex(10f)
    )
}







