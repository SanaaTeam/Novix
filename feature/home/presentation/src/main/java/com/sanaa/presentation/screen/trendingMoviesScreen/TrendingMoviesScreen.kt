package com.sanaa.presentation.screen.trendingMoviesScreen

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
import com.sanaa.api.launchAuthActivityForResult
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.feature.home.presentation.R
import com.sanaa.designsystem.R as designSystemR
import com.sanaa.presentation.api.HomeApiEntryPoint
import com.sanaa.presentation.app.navigation.LocalMainNavController
import com.sanaa.presentation.bottomsheet.addEditBookmark.AddBookmarkListBottomSheet
import com.sanaa.presentation.bottomsheet.saveToListBottomsheet.SaveToListBottomSheet
import com.sanaa.presentation.components.PaginatedMediaListSectionContent
import com.sanaa.presentation.components.RefreshButton
import com.sanaa.presentation.components.RequestToLoginBottomSheet
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TrendingMoviesScreen(
    viewModel: TrendingMoviesScreenViewModel = hiltViewModel()
) {

    val state = viewModel.state.collectAsStateWithLifecycle()

    EffectHandler(viewModel.effect)

    TrendingMoviesScreenContent(
        state = state.value,
        interactionListener = viewModel,
    )
}

@Composable
private fun TrendingMoviesScreenContent(
    state: TrendingMoviesScreenUiState,
    interactionListener: TrendingMoviesScreenInteractionListener,
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
                screenTitle = stringResource(R.string.trending_movies),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .statusBarsPadding(),
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
                        onSaveIconClick = interactionListener::onSaveIconClick,
                    )
                    if (trendingMedia.loadState.hasError) {
                        RefreshButton(onRetryClick = interactionListener::onRetryClick)
                    }


                }
            }
        }
    }
    SaveToListBottomSheet(
        isVisible = state.showSaveToListBottomSheet,
        mediaId = state.selectedMediaId ?: 0,
        onDismiss = interactionListener::onDismissSaveToListBottomSheet,
        onCreateNewListClick = interactionListener::onCreateNewListClick,
    )

    AddBookmarkListBottomSheet(
        isVisible = state.showAddListBottomSheet,
        onDismiss = interactionListener::onDismissAddListBottomSheet,
    )
    RequestToLoginBottomSheet(
        isVisible = state.showLoginBottomSheet,
        onDismiss = interactionListener::onDismissLoginBottomSheet,
        onLoginButtonClick = interactionListener::onLoginButtonClick
    )
}

@Composable
private fun EffectHandler(
    effect: SharedFlow<TrendingMoviesScreenEffect>,
) {
    val context = LocalContext.current
    val appContext = context.applicationContext
    val navController = LocalMainNavController.current

    val detailsApi: MediaDetailsApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, HomeApiEntryPoint::class.java)
            .detailsApi()
    }

    val authApi = EntryPointAccessors.fromApplication(
        context,
        HomeApiEntryPoint::class.java
    ).authenticationApi()

    val launcher = launchAuthActivityForResult()

    LaunchedEffect(Unit) {
        effect.collectLatest { effect ->
            when (effect) {
                is TrendingMoviesScreenEffect.NavigateToMoviesDetails -> {
                    detailsApi.launch(
                        context = navController.context,
                        id = effect.id,
                        startRoute = StartRoute.MOVIE
                    )
                }

                is TrendingMoviesScreenEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                is TrendingMoviesScreenEffect.NavigateToLogin -> {
                    launcher.launch(authApi.getLaunchIntent(context))
                }
            }
        }
    }
}
