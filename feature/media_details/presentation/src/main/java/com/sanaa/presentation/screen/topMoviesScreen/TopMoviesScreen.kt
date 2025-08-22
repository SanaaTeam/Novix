package com.sanaa.presentation.screen.topMoviesScreen

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.api.AuthStartRoute
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.BackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.presentation.api.LocalSafeContentThreshold
import com.sanaa.presentation.api.LocalThemeProvider
import com.sanaa.presentation.bottomsheets.addEditBookmark.AddBookmarkListBottomSheet
import com.sanaa.presentation.bottomsheets.saveToListBottomsheet.SaveToListBottomSheet
import com.sanaa.presentation.navigation.DetailsApiEntryPoint
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.MovieDetailsScreenRoute
import com.sanaa.presentation.shared_component.NovixAnimatedSnackBarHost
import com.sanaa.presentation.shared_component.RemoteImagePlaceholder
import com.sanaa.presentation.shared_component.RequestToLoginBottomSheet
import com.sanaa.presentation.shared_component.cards.MediaPosterCard
import com.sanaa.presentation.shared_component.cards.SaveIconChip
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import com.sanaa.designsystem.R as designR

@Composable
fun TopMoviesScreen(
    viewModel: TopMoviesScreenViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TopMoviesEffectsHandler(effects = viewModel.effect)
    TopMoviesContent(
        state = state,
        modifier = Modifier.fillMaxSize(),
        interactionListener = viewModel,
    )
}

@Composable
private fun TopMoviesContent(
    state: TopMoviesScreenUiState,
    modifier: Modifier = Modifier,
    interactionListener: TopMoviesScreenInteractionListener,
) {
    NovixScaffold(
        backgroundShapes = { BackgroundShapes() },
        snackBarHost = {
            NovixAnimatedSnackBarHost(
                data = state.snackBarData,
                onDismiss = interactionListener::onSnackBarDismiss,
                modifier = Modifier.statusBarsPadding()
            )
        },
    ) {
        Column(
            modifier = modifier.navigationBarsPadding()
        ) {
            TopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = designR.drawable.icon_back),
                        onClick = interactionListener::onBackClick
                    )
                },
                screenTitle = stringResource(R.string.top_movie_picks),
                modifier = Modifier
                    .fillMaxWidth()
                    .systemBarsPadding()
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(), contentAlignment = Alignment.Center
            ) {

                AnimatedContent(
                    targetState = Pair(state.isLoading, state.noInternetConnection),
                    modifier = Modifier.align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) { (isLoading, noInternetConnection) ->
                    when {
                        isLoading -> {
                            LoadingIndicator()
                        }

                        noInternetConnection -> {
                            NetworkDisconnectionContact(
                                onRetryClick = interactionListener::onRetryClicked,
                                useDarkTheme = LocalThemeProvider.current
                            )
                        }

                        else -> {
                            LazyVerticalGrid(
                                modifier = Modifier.fillMaxSize(),
                                columns = GridCells.Adaptive(minSize = 140.dp),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                itemsIndexed(
                                    state.topMovies,
                                    key = { index, _ -> index }
                                ) { _, movie ->
                                    MediaPosterCard(
                                        posterImage = {
                                            RemoteBlurredSensitiveImage(
                                                imageUrl = movie.posterUrl.orEmpty(),
                                                modifier = Modifier.fillMaxSize(),
                                                sensitiveContentThreshold = 0.2f,
                                                isBlurEnabled = LocalSafeContentThreshold.current != 0f,
                                                safeContentThreshold = LocalSafeContentThreshold.current,
                                                contentDescription = movie.title,
                                                placeholderContent = {
                                                    RemoteImagePlaceholder(Modifier.fillMaxSize())
                                                },
                                                errorContent = {
                                                    RemoteImagePlaceholder(Modifier.fillMaxSize())
                                                },
                                            ) {
                                                OnBlurContent(
                                                    hintText = stringResource(R.string.unsuitable_image),
                                                    textStyle = Theme.textStyle.body.small.copy(
                                                        color = Color(0x99FFFFFF)
                                                    ),
                                                    iconSize = 24.dp,
                                                    icon = painterResource(designR.drawable.icon_eye_slash)
                                                )
                                            }
                                        },
                                        topLeftContent = {
                                            SaveIconChip(
                                                onClick = { interactionListener.onSaveClicked(movie) }
                                            )
                                        },
                                        onCardClick = { interactionListener.onMovieClick(movie.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    RequestToLoginBottomSheet(
        isVisible = state.showLoginBottomSheet,
        onDismiss = interactionListener::onDismissLoginBottomSheet,
        onLoginButtonClick = interactionListener::onLoginButtonClick
    )

    SaveToListBottomSheet(
        isVisible = state.showSaveToListBottomSheet,
        mediaId = state.selectedMediaToSave?.id ?: 0,
        onDismiss = interactionListener::onDismissSaveToListBottomSheet,
        onCreateNewListClick = interactionListener::onCreateNewListClick,
    )

    AddBookmarkListBottomSheet(
        isVisible = state.showAddListBottomSheet,
        onDismiss = interactionListener::onDismissAddListBottomSheet,
    )
}

@Composable
private fun TopMoviesEffectsHandler(
    effects: Flow<TopMoviesScreenEffect>,
) {
    val navController = LocalNavControllerProvider.current
    val context = LocalContext.current

    val authApi = EntryPointAccessors
        .fromApplication(
            context, DetailsApiEntryPoint::class.java
        ).authenticationApi()

    LaunchedEffect(Unit) {
        effects.collectLatest { effect ->
            when (effect) {
                TopMoviesScreenEffect.NavigateBack -> if (!navController.popBackStack()) {
                    (navController.context as Activity).finish()
                }

                is TopMoviesScreenEffect.NavigateToMovieDetails -> navController.navigate(
                    MovieDetailsScreenRoute(effect.id)
                )

                TopMoviesScreenEffect.NavigateToLogin -> {
                    authApi.launch(context, AuthStartRoute.Login)
                }
            }
        }
    }
}