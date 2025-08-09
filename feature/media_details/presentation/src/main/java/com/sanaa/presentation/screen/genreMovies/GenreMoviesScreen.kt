package com.sanaa.presentation.screen.genreMovies

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.api.launchAuthActivityForResult
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
import com.sanaa.designsystem.design_system.component.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.MovieDetailsScreenRoute
import com.sanaa.presentation.screen.movieDetails.SnackData
import com.sanaa.presentation.shared_component.RemoteImagePlaceholder
import com.sanaa.presentation.shared_component.RequestToLoginBottomSheet
import com.sanaa.presentation.shared_component.cards.MediaPosterCard
import com.sanaa.presentation.shared_component.cards.SaveIconChip
import dagger.hilt.android.EntryPointAccessors
import com.sanaa.designsystem.R as designR

@Composable
fun GenreMoviesScreen(
    viewModel: GenreMoviesViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavControllerProvider.current

    val context = LocalContext.current

    val authApi = EntryPointAccessors
        .fromApplication(
            context, DetailsApiEntryPoint::class.java
        ).authenticationApi()

    val launcher = launchAuthActivityForResult()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                GenreMoviesEffects.NavigateBack -> navController.popBackStack()
                is GenreMoviesEffects.NavigateToMovieDetails -> navController.navigate(
                    MovieDetailsScreenRoute(effect.id).route()
                )

                GenreMoviesEffects.NavigateToLogin -> {
                    launcher.launch(authApi.getLaunchIntent(context))
                }
            }
        }
    }

    GenreMoviesScreenContent(
        state = state.value, interactionListener = viewModel
    )


}


@Composable
fun GenreMoviesScreenContent(
    state: GenreMoviesScreenUiState,
    interactionListener: GenreMoviesScreenInteractionListener,
) {
    var snack by remember { mutableStateOf<SnackData?>(null) }
    val pagedMovies = state.movies.collectAsLazyPagingItems()
    NovixScaffold(
        backgroundShapes = { BackgroundShapes() },
    ) {
        Column(
            modifier = Modifier.navigationBarsPadding()
        ) {
            TopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = designR.drawable.icon_back),
                        onClick = { interactionListener.onBackClick() })
                }, screenTitle = state.title.orEmpty(), modifier = Modifier
                    .fillMaxWidth()
                    .systemBarsPadding()
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(), contentAlignment = Alignment.Center
            ) {

                AnimatedContent(
                    targetState = state.isLoading || state.noInternetConnection,
                    contentAlignment = Alignment.Center,
                    transitionSpec = { fadeIn() togetherWith fadeOut() })
                {
                    if (it) {
                        if (state.noInternetConnection) {
                            NetworkDisconnectionContact(
                                onRetryClick = { interactionListener.onRetryClicked() },
                                modifier = Modifier.fillMaxSize(),
                                useDarkTheme = LocalThemeProvider.current
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
                        LazyVerticalGrid(
                            modifier = Modifier.fillMaxSize(),
                            columns = GridCells.Adaptive(minSize = 140.dp),
                            contentPadding = PaddingValues(
                                start = 16.dp, end = 16.dp, bottom = 16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                count = pagedMovies.itemCount,
                                key = { index ->
                                    "${index}_${pagedMovies[index]?.id}"
                                }
                            ) { index ->
                                val movie = pagedMovies[index] ?: return@items
                                MediaPosterCard(
                                    posterImage = {
                                        RemoteBlurredSensitiveImage(
                                            imageUrl = movie.posterUrl.orEmpty(),
                                            modifier = Modifier.fillMaxWidth(),
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
                                                icon = painterResource(com.sanaa.designsystem.R.drawable.icon_eye_slash),
                                            )
                                        }
                                    },
                                    topLeftContent = {
                                        SaveIconChip(onClick = {
                                            interactionListener.onSaveIconClick(
                                                movie
                                            )
                                        })
                                    },
                                    onCardClick = { interactionListener.onMovieClick(movie.id) })
                            }

                            if (pagedMovies.loadState.append is LoadState.Loading) {
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

                    state.selectedMovieToSave?.let { mediaItem ->
                        SaveToListBottomSheet(
                            isVisible = state.showSaveToListBottomSheet,
                            mediaId = mediaItem.id.toLong(),
                            onDismiss = interactionListener::onDismissSaveToListBottomSheet,
                            onCreateNewListClick = interactionListener::onCreateNewListClick,
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
                    }

                    AddBookmarkListBottomSheet(
                        isVisible = state.showAddListBottomSheet,
                        onDismiss = interactionListener::onDismissAddListBottomSheet,
                        mediaId = state.selectedMovieToSave?.id ?: 0
                    )
                    RequestToLoginBottomSheet(
                        onDismiss = { interactionListener.onBottomSheetDismiss() },
                        onLoginButtonClick = { interactionListener.onLoginButtonClick() },
                        isVisible = state.showBottomSheet
                    )
                }
            }
        }
    }
}
