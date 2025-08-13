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
import androidx.navigation.NavHostController
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
import com.sanaa.presentation.model.SeriesUiModel
import com.sanaa.presentation.navigation.DetailsApiEntryPoint
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.SeriesDetailsScreenRoute
import com.sanaa.presentation.screen.actor.ActorScreenUiState
import com.sanaa.presentation.screen.actor.ActorViewModel
import com.sanaa.presentation.shared_component.RemoteImagePlaceholder
import com.sanaa.presentation.shared_component.RequestToLoginBottomSheet
import com.sanaa.presentation.shared_component.cards.MediaPosterCard
import dagger.hilt.android.EntryPointAccessors
import com.sanaa.designsystem.R as designR

@Composable
fun TopSeriesScreen(
    viewModel: ActorViewModel = hiltViewModel(),
) {
    val navController = LocalNavControllerProvider.current
    BackHandler(onBack = { navController.popBackStack() })

    val context = LocalContext.current
    val authApi = EntryPointAccessors
        .fromApplication(context, DetailsApiEntryPoint::class.java)
        .authenticationApi()
    val launcher = launchAuthActivityForResult()
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    TopSeriesContent(
        state = uiState,
        navController = navController,
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
private fun TopSeriesContent(
    state: ActorScreenUiState,
    navController: NavHostController,
) {
    NovixScaffold(backgroundShapes = { BackgroundShapes() }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            TopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = designR.drawable.icon_back),
                        onClick = { navController.popBackStack() }
                    )
                },
                screenTitle = stringResource(R.string.top_series_picks),
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
                                onRetryClick = onRetryClicked,
                                useDarkTheme = LocalThemeProvider.current
                            )
                        }

                        else -> {
                            SeriesList(state, navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SeriesList(
    state: ActorScreenUiState,
    navController: NavHostController,
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 140.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(
            state.topTvSeries,
            key = { index, _ -> index }
        ) { _, series ->
            MediaPosterCard(
                posterImage = {
                    PosterImage(series)
                },
                onCardClick = {
                    navController.navigate(
                        SeriesDetailsScreenRoute(series.id).route()
                    )
                }
            )
        }
    }
}

@Composable
private fun PosterImage(series: SeriesUiModel) {
    RemoteBlurredSensitiveImage(
        imageUrl = series.posterPath.orEmpty(),
        modifier = Modifier.fillMaxSize(),
        sensitiveContentThreshold = 0.2f,
        isBlurEnabled = LocalSafeContentThreshold.current != 0f,
        safeContentThreshold = LocalSafeContentThreshold.current,
        contentDescription = series.title,
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
            icon = painterResource(designR.drawable.icon_eye_slash),
        )
    }
}
