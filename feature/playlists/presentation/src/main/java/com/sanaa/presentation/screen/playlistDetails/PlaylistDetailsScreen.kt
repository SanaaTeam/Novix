package com.sanaa.presentation.screen.playlistDetails

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.design_system.component.animation.FadeInOut150
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.playlists.presentation.R
import com.sanaa.presentation.bottomsheets.removeFromListBottomSheet.RemoveFromListBottomSheet
import com.sanaa.presentation.playListNavigation.PlaylistsApiEntryPoint
import com.sanaa.presentation.playListProviders.LocalNavControllerProvider
import com.sanaa.presentation.screen.playlist.componants.AnimatedSnackBarHost
import com.sanaa.presentation.screen.playlistDetails.components.DeleteConfirmationBottomSheet
import com.sanaa.presentation.screen.playlistDetails.components.RefreshButton
import com.sanaa.presentation.screen.playlistDetails.components.SavedDetailsListSectionContent
import com.sanaa.presentation.screen.playlistDetails.state.MediaTypeUi
import com.sanaa.presentation.screen.playlistDetails.state.SavedDetailsScreenUiState
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.Flow

@Composable
fun PlaylistDetailsScreen(
    viewModel: PlaylistDetailsScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    EffectHandler(effect = viewModel.effect)

    PlaylistDetailsContent(
        state = state.value,
        interactionListener = viewModel
    )
}


@Composable
fun PlaylistDetailsContent(
    state: SavedDetailsScreenUiState,
    interactionListener: PlaylistDetailsInteractionListener,
) {
    val movieList = state.movieList.collectAsLazyPagingItems()
    NovixScaffold(
        topBar = {
            TopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(R.drawable.icon_back),
                        onClick = interactionListener::onBackClick
                    )
                },
                screenTitle = state.title.orEmpty(),
                rightContent = {
                    TopBarClickableIcon(
                        icon = painterResource(R.drawable.icon_deleat),
                        onClick = { interactionListener.onDeleteListClick() },
                        tint = Theme.colors.statusColors.redAccent
                    )
                },
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(vertical = 12.dp)
            )
        },
        snackBarHost = {
            AnimatedSnackBarHost(
                data = state.snackBarData,
                onDismiss = interactionListener::onSnackBarDismiss,
            )
        },
    ) {
        AnimatedContent(
            targetState = Triple(state.noInternetConnection, movieList.loadState.refresh, movieList.itemCount),
            transitionSpec = { FadeInOut150 },
        ) { (isNoInternet, refresh, count) ->
            when {
                isNoInternet && count == 0 -> {
                    NetworkDisconnectionContact(onRetryClick = interactionListener::onRetryClick)
                }

                refresh is LoadState.Loading && count == 0 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding(),
                        contentAlignment = Alignment.Center,
                    ) {
                        LoadingIndicator()
                    }
                }

                else -> {
                    SavedDetailsListSectionContent(
                        mediaList = movieList,
                        onMediaClick = { interactionListener.onMediaClick(it.id, MediaTypeUi.MOVIE) },
                        onSaveIconClick = { interactionListener.onDeleteIconClick(it) },
                        safeContentThreshold = state.safeContentThreshold,
                    )

                    if (refresh is LoadState.Error && count == 0) {
                        RefreshButton(onRetryClick = interactionListener::onRetryClick)
                    }
                }
            }
        }}
        RemoveFromListBottomSheet(
        isVisible = state.showRemoveFromListBottomSheet,
        mediaId = state.selectedMediaToRemove?.id ?: 0,
        mediaTitle = state.selectedMediaToRemove?.title.orEmpty(),
        onDismiss = interactionListener::onDismissRemoveFromListBottomSheet,
        onDismissAfterRemoveSuccess = interactionListener::onDismissListBottomSheetAfterRemoveSuccess,
    )

    DeleteConfirmationBottomSheet(
        isVisible = state.showListDeletionConfirmationBottomSheet,
        isLoading = state.isLoading,
        onDismiss = interactionListener::onDismissConfirmationBottomSheet,
        onConfirm = interactionListener::onDeleteListConfirmed
    )
}

@Composable
private fun EffectHandler(
    effect: Flow<PlaylistDetailsScreenEffect>,
) {
    val context = LocalContext.current

    val navController = LocalNavControllerProvider.current

    val detailsApi: MediaDetailsApi = remember {
        EntryPointAccessors
            .fromApplication(context, PlaylistsApiEntryPoint::class.java)
            .detailsApi()
    }
    LaunchedEffect(Unit) {
        effect.collect { effect ->
            when (effect) {
                is PlaylistDetailsScreenEffect.NavigateBackAfterDelete -> {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("list_deleted", true)
                    navController.popBackStack()
                }

                is PlaylistDetailsScreenEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                is PlaylistDetailsScreenEffect.NavigateToMediaDetails -> {
                    detailsApi.launch(
                        context = navController.context,
                        id = effect.mediaId,
                        startRoute = StartRoute.MOVIE
                    )
                }
            }
        }
    }
}