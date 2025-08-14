package com.sanaa.presentation.screen.myRating

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.userprofile.presentation.R
import com.sanaa.presentation.navigation.ProfileApiEntryPoint
import com.sanaa.presentation.provider.LocalNavControllerProvider
import com.sanaa.presentation.provider.LocalThemeMode
import com.sanaa.presentation.screen.myRating.MyRatingScreenEffect.NavigateBack
import com.sanaa.presentation.screen.myRating.MyRatingScreenEffect.NavigateToMediaDetails
import com.sanaa.presentation.screen.myRating.component.AnimatedSnackBarHost
import com.sanaa.presentation.screen.myRating.component.RatedMediaListSectionContent
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import com.sanaa.designsystem.R as designR

@Composable
fun MyRatingScreen(
    viewModel: MyRatingScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MyRatingScreenEffectsHandler(effects = viewModel.effect)

    MyRatingScreenContent(
        state = state,
        interactionListener = viewModel,
    )
}

@Composable
private fun MyRatingScreenEffectsHandler(
    effects: SharedFlow<MyRatingScreenEffect>
) {
    val navController = LocalNavControllerProvider.current
    val appContext = LocalContext.current.applicationContext

    val detailsApi: MediaDetailsApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, ProfileApiEntryPoint::class.java)
            .detailsApi()
    }

    LaunchedEffect(Unit) {
        effects.collectLatest { effect ->
            when (effect) {
                is NavigateBack -> navController.popBackStack()
                is NavigateToMediaDetails -> detailsApi.launch(
                    context = navController.context,
                    id = effect.mediaId,
                    startRoute = when (effect.mediaTypeUi) {
                        MediaTypeUi.MOVIE -> StartRoute.MOVIE
                        MediaTypeUi.TV_SHOW -> StartRoute.TV_SHOW
                    }
                )
            }
        }
    }
}

@Composable
private fun MyRatingScreenContent(
    state: MyRatingScreenUiState,
    interactionListener: MyRatingScreenInteractionListener,
) {
    NovixScaffold(
        modifier = Modifier.background(color = Theme.colors.surface),
        backgroundShapes = {},
        topBar = {
            TopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(designR.drawable.icon_back),
                        onClick = interactionListener::onBackClick
                    )
                },
                screenTitle = stringResource(R.string.my_rating),
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(vertical = 12.dp)
            )
        },
        snackBarHost = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                AnimatedSnackBarHost(
                    data = state.snackBarData,
                    onDismiss = interactionListener::onDismissSnack,
                )
            }
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = state.isLoading || state.isNoInternetConnection,
                contentAlignment = Alignment.Center
            ) { shouldShowLoadingOrError ->
                if (shouldShowLoadingOrError) {
                    if (state.isNoInternetConnection) {
                        NetworkDisconnectionContact(
                            onRetryClick = { interactionListener.onRetryLoadDetails() },
                            modifier = Modifier.fillMaxSize(),
                            useDarkTheme = LocalThemeMode.current
                        )
                    } else {
                        LoadingIndicator()
                    }
                } else {
                    RatedMediaListSectionContent(
                        state = state,
                        onTabSelected = interactionListener::onTabSelected,
                        onDeleteRatingClick = interactionListener::onDeleteIconClick,
                        onCardClick = interactionListener::onMediaClick
                    )
                }
            }
        }
    }
}