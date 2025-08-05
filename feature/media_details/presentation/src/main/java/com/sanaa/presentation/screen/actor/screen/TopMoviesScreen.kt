package com.sanaa.presentation.screen.actor.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
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
import com.sanaa.api.launchAuthActivityForResult
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.BackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.poster.RemoteImagePlaceholder
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.presentation.api.LocalSafeContentThreshold
import com.sanaa.presentation.navigation.DetailsApiEntryPoint
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.MovieDetailsScreenRoute
import com.sanaa.presentation.screen.actor.ActorScreenUiState
import com.sanaa.presentation.screen.actor.ActorViewModel
import com.sanaa.presentation.shared_component.RequestToLoginBottomSheet
import com.sanaa.presentation.shared_component.cards.MediaPosterCard
import com.sanaa.presentation.shared_component.cards.SaveIconChip
import dagger.hilt.android.EntryPointAccessors
import com.sanaa.designsystem.R as designR

@Composable
fun TopMoviesScreen(
    navigateBack: () -> Unit,
    viewModel: ActorViewModel = hiltViewModel(),
) {
    BackHandler(onBack = navigateBack)
    val context = LocalContext.current
    val authApi = EntryPointAccessors.fromApplication(
        context,
        DetailsApiEntryPoint::class.java
    ).authenticationApi()

    val launcher = launchAuthActivityForResult(
        loggedInWithSessionId = {
            viewModel.updateUserLoggingStatus()
        },
        loggedInAsGuest = {
            viewModel.updateUserLoggingStatus()
        }
    )

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    TopMoviesContent(
        state = uiState,
        onBackClick = navigateBack,
        modifier = Modifier.fillMaxSize(),
        onSaveIconClick = {
            viewModel.onSaveClicked()
        }
    )
    RequestToLoginBottomSheet(
        isVisible = uiState.showLoginBottomSheet,
        onDismiss = viewModel::onDismissBottomSheet,
        onLoginButtonClick = {
            launcher.launch(authApi.getLaunchIntent(context))
        }
    )
}

@Composable
private fun TopMoviesContent(
    state: ActorScreenUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onSaveIconClick: () -> Unit,
) {
    val navController = LocalNavControllerProvider.current

    NovixScaffold(
        backgroundShapes = { BackgroundShapes() },
    ) {
        Column(
            modifier = modifier.navigationBarsPadding()
        ) {
            TopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = designR.drawable.icon_back), onClick = onBackClick
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
                    state.isLoading,
                    modifier = Modifier.align(Alignment.Center),
                    contentAlignment = Alignment.Center

                ) { loading ->
                    if (loading) {
                        LoadingIndicator()
                    } else {
                        LazyVerticalGrid(
                            modifier = Modifier.fillMaxSize(),
                            columns = GridCells.Adaptive(minSize = 140.dp),
                            contentPadding = PaddingValues(
                                start = 16.dp, end = 16.dp, bottom = 16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(
                                12.dp
                            ),
                            horizontalArrangement = Arrangement.spacedBy(
                                12.dp
                            )
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
                                                icon = painterResource(com.sanaa.designsystem.R.drawable.icon_eye_slash)
                                            )
                                        }
                                    },
                                    topLeftContent = {
                                        SaveIconChip(onClick = {
                                            onSaveIconClick()
                                        })
                                    },
                                    onCardClick = {
                                        navController.navigate(MovieDetailsScreenRoute(movie.id).route())
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}
