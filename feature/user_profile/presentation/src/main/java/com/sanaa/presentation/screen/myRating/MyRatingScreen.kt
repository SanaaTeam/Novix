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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.sanaa.presentation.api.navigation.LocalNavControllerProvider
import com.sanaa.presentation.api.navigation.ProfileApiEntryPoint
import com.sanaa.presentation.provider.LocalThemeMode
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
    val navController = LocalNavControllerProvider.current
    val appContext = LocalContext.current.applicationContext

    val detailsApi: MediaDetailsApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, ProfileApiEntryPoint::class.java)
            .detailsApi()
    }

    var snack by remember { mutableStateOf<SnackData?>(null) }
    val state by viewModel.state.collectAsStateWithLifecycle()

    MyRatingScreenEffectsHandler(
        effects = viewModel.effect,
        onNavigateBack = { navController.popBackStack() },
        onShowErrorSnackBar = { message -> snack = SnackData(message = message, isError = true) },
        onShowSuccessSnackBar = { message -> snack = SnackData(message = message, isError = false) },
        onNavigateToMediaDetails = { id, type ->
            detailsApi.launch(
                context = navController.context,
                id = id,
                startRoute = when (type) {
                    MediaTypeUi.MOVIE -> StartRoute.MOVIE
                    MediaTypeUi.TV_SHOW -> StartRoute.SERIES
                }
            )
        }
    )

    MyRatingScreenContent(
        state = state,
        interactionListener = viewModel,
        snack = snack,
        onDismissSnack = { snack = null }
    )
}

@Composable
private fun MyRatingScreenEffectsHandler(
    onNavigateBack: () -> Unit,
    onShowErrorSnackBar: (String) -> Unit,
    onShowSuccessSnackBar: (String) -> Unit,
    onNavigateToMediaDetails: (Int, MediaTypeUi) -> Unit,
    effects:SharedFlow<MyRatingScreenEffect>
) {
    LaunchedEffect(Unit) {
        effects.collectLatest { effect ->
            when (effect) {
                is MyRatingScreenEffect.NavigateBack -> onNavigateBack()
                is MyRatingScreenEffect.ShowErrorSnackBar -> onShowErrorSnackBar(effect.message)
                is MyRatingScreenEffect.ShowSuccessSnackBar -> onShowSuccessSnackBar(effect.message)
                is MyRatingScreenEffect.NavigateToMediaDetails -> onNavigateToMediaDetails(effect.mediaId, effect.mediaTypeUi)
            }
        }
    }
}@Composable
private fun MyRatingScreenContent(
    onDismissSnack: () -> Unit,
    state: MyRatingScreenUiState,
    interactionListener: MyRatingScreenInteractionListener,
    snack: SnackData?
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
                    data = snack,
                    onDismiss = onDismissSnack,
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