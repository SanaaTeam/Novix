package com.sanaa.presentation.screen.topMoviesScreen

import androidx.activity.compose.BackHandler
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
import androidx.navigation.NavController
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
import com.sanaa.designsystem.R as designR

@Composable
fun TopMoviesScreen(
    viewModel: TopMoviesScreenViewModel = hiltViewModel(),
) {
    val navController = LocalNavControllerProvider.current
    BackHandler(onBack = { navController.popBackStack() })

    val context = LocalContext.current
    val authApi = EntryPointAccessors
        .fromApplication(context, DetailsApiEntryPoint::class.java)
        .authenticationApi()

    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val selectedMedia = uiState.selectedMediaToSave

    TopMoviesContent(
        state = uiState,
        modifier = Modifier.fillMaxSize(),
        interactionListener = viewModel,
        navController = navController,
    )

    RequestToLoginBottomSheet(
        isVisible = uiState.showLoginBottomSheet,
        onDismiss = viewModel::onDismissBottomSheet,
        onLoginButtonClick = {
            authApi.launch(context, AuthStartRoute.Login)
        }
    )

    SaveToListBottomSheet(
        isVisible = uiState.showSaveToListBottomSheet,
        mediaId = selectedMedia?.id ?: 0,
        onDismiss = viewModel::onDismissSaveToListBottomSheet,
        onCreateNewListClick = viewModel::onCreateNewListClick,
    )

    AddBookmarkListBottomSheet(
        isVisible = uiState.showAddListBottomSheet,
        onDismiss = viewModel::onDismissAddListBottomSheet,
    )

}


@Composable
private fun TopMoviesContent(
    state: TopMoviesScreenUiState,
    modifier: Modifier = Modifier,
    interactionListener: TopMoviesScreenInteractionListener,
    navController: NavController,
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
                        onClick = { navController.popBackStack() }
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
                                        onCardClick = {
                                            navController.navigate(MovieDetailsScreenRoute(movie.id))
                                        })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

