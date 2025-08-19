package com.sanaa.presentation.screen.trendingTvShowScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.api.HomeApiEntryPoint
import com.sanaa.presentation.app.navigation.LocalMainNavController
import com.sanaa.presentation.components.NovixAnimatedSnackBarHost
import com.sanaa.presentation.components.PaginatedMediaListSectionContent
import com.sanaa.presentation.components.RefreshButton
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import com.sanaa.designsystem.R as designSystemR

@Composable
fun TrendingTvShowsScreen(
    viewModel: TrendingTvShowsScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    EffectHandler(viewModel.effect)

    TrendingTvShowsScreenContent(
        state = state.value,
        interactionListener = viewModel,
    )
}

@Composable
private fun TrendingTvShowsScreenContent(
    state: TrendingTvShowsScreenUiState,
    interactionListener: TrendingTvShowsScreenInteractionListener,
) {

    val trendingMedia = state.mediaList.collectAsLazyPagingItems()

    NovixScaffold(
        topBar = {
            TopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = designSystemR.drawable.icon_back),
                        onClick = interactionListener::onBackClick
                    )
                },
                screenTitle = stringResource(R.string.trending_tvshows),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .statusBarsPadding()
            )
        },
        snackBarHost = {
            NovixAnimatedSnackBarHost(
                data = state.snackBarData,
                onDismiss = interactionListener::onSnackBarDismiss,
                modifier = Modifier.statusBarsPadding()
            )
        },
    ) {

        AnimatedContent(
            targetState = state.isNoInternetConnection && trendingMedia.itemCount == 0,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) { showNoInternetScreen ->
            when (showNoInternetScreen) {
                true -> {
                    NetworkDisconnectionContact(onRetryClick = interactionListener::onRetryClick)
                }

                else -> {
                    PaginatedMediaListSectionContent(
                        genres = state.genreList,
                        mediaList = trendingMedia,
                        selectedGenreId = state.selectedGenreId,
                        onGenreClick = interactionListener::onGenreClick,
                        onMediaClick = { media -> interactionListener.onMediaClick(media.id) },
                    )
                    if (trendingMedia.loadState.hasError) {
                        RefreshButton(onRetryClick = interactionListener::onRetryClick)
                    }
                }
            }
        }
    }
}

@Composable
private fun EffectHandler(
    effect: SharedFlow<TrendingTvShowsScreenEffect>,
) {
    val navController = LocalMainNavController.current
    val appContext = LocalContext.current.applicationContext

    val detailsApi: MediaDetailsApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, HomeApiEntryPoint::class.java)
            .detailsApi()
    }

    LaunchedEffect(Unit) {
        effect.collectLatest { effect ->
            when (effect) {
                is TrendingTvShowsScreenEffect.NavigateToTvShowDetails -> {
                    detailsApi.launch(
                        context = navController.context,
                        id = effect.id,
                        startRoute = StartRoute.TV_SHOW
                    )
                }

                is TrendingTvShowsScreenEffect.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }
}