package com.sanaa.presentation.screen.genreMovies

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.BackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.image_viewer.component.RemoteBlurredHaramImageViewer
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.MovieDetailsScreenRoute
import com.sanaa.presentation.shared_component.RemoteImagePlaceholder
import com.sanaa.presentation.shared_component.RequestToLoginBottomSheet
import com.sanaa.presentation.shared_component.cards.MediaPosterCard
import com.sanaa.presentation.shared_component.cards.SaveIconChip

@Composable
fun GenreMoviesScreen(
    viewModel: GenreMoviesViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavControllerProvider.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                GenreMoviesEffects.NavigateBack -> navController.popBackStack()
                is GenreMoviesEffects.NavigateToMovieDetails -> navController.navigate(
                    MovieDetailsScreenRoute(effect.id).route()
                )
                GenreMoviesEffects.NavigateToLogin -> {
                    // Launch authentication activity
                    val intent = Intent(navController.context, Class.forName("com.sanaa.novix.MainActivity"))
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    navController.context.startActivity(intent)
                }
            }
        }
    }

    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        GenreMoviesScreenContent(
            state = state.value, interactionListener = viewModel
        )
    }

}

@Composable
fun GenreMoviesScreenContent(
    state: GenreMoviesScreenUiState,
    interactionListener: GenreMoviesScreenInteractionListener,
) {
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
                        icon = painterResource(id = R.drawable.icon_back),
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
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
                                    val movie = pagedMovies[index]
                                    "${index}-${movie?.id}"
                                }
                            ) { index ->
                                val movie = pagedMovies[index] ?: return@items
                                MediaPosterCard(
                                    posterImage = {
                                        RemoteBlurredHaramImageViewer(
                                            imageUrl = movie.posterUrl.orEmpty(),
                                            modifier = Modifier.fillMaxWidth(),
                                            haramThreshold = 0.2f,
                                            nonHaramThreshold = 0.7f,
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
                                    topLeftContent = { SaveIconChip(onClick = { interactionListener.onSaveIconClick() }) },
                                    onCardClick = { interactionListener.onMovieClick(movie.id) })
                            }
                        }
                    }
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


