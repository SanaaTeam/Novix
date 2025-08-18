package com.sanaa.presentation.screen.playlistDetails

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.playlists.presentation.R
import com.sanaa.presentation.api.navigationSaved.LocalNavControllerProvider
import com.sanaa.presentation.api.navigationSaved.PlaylistsApiEntryPoint
import com.sanaa.presentation.bottomsheets.removeFromListBottomSheet.RemoveFromListBottomSheet
import com.sanaa.presentation.screen.playlist.componants.AnimatedSnackBarHost
import com.sanaa.presentation.screen.playlistDetails.components.DeleteConfirmationBottomSheet
import com.sanaa.presentation.screen.playlistDetails.components.EmptyItemsScreen
import com.sanaa.presentation.screen.playlistDetails.components.PaginatedMediaListGrid
import com.sanaa.presentation.screen.playlistDetails.components.RefreshButton
import com.sanaa.presentation.screen.playlistDetails.state.MediaItem
import com.sanaa.presentation.screen.playlistDetails.state.MediaTypeUi
import com.sanaa.presentation.screen.playlistDetails.state.SavedDetailsScreenUiState
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.Flow

@Composable
fun PlaylistDetailsScreen(
    viewModel: PlaylistDetailsScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val movieList = state.value.movieList.collectAsLazyPagingItems()

    EffectHandler(effect = viewModel.effect)

    PlaylistDetailsContent(
        state = state.value,
        movieList = movieList,
        interactionListener = viewModel
    )
}


@Composable
fun PlaylistDetailsContent(
    state: SavedDetailsScreenUiState,
    movieList: LazyPagingItems<MediaItem>,
    interactionListener: PlaylistDetailsInteractionListener,
    modifier: Modifier = Modifier,
) {
    NovixScaffold(
        modifier = modifier.background(color = Theme.colors.surface),
        backgroundShapes = {},
        topBar = {
            PlaylistDetailsTopBar(
                title = state.title.orEmpty(),
                interactionListener = interactionListener,
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
            targetState = state.noInternetConnection,
            transitionSpec = {
                fadeIn(animationSpec = tween(150, delayMillis = 150))
                    .togetherWith(fadeOut(animationSpec = tween(150)))
            },
        ) { isNoInternetConnection ->

            if (isNoInternetConnection && (movieList.itemCount == 0)) {
                NetworkDisconnectionContact(onRetryClick = interactionListener::onRetryClick)
            } else {

                SavedDetailsListSectionContent(
                    mediaList = movieList,
                    onMediaClick = { interactionListener.onMediaClick(it.id, MediaTypeUi.MOVIE) },
                    onSaveIconClick = { interactionListener.onDeleteIconClick(it) }
                )

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
                if (movieList.loadState.hasError) {
                    RefreshButton(onRetryClick = interactionListener::onRetryClick)
                }
            }
        }
    }

}

@Composable
fun PlaylistDetailsTopBar(
    title: String,
    interactionListener: PlaylistDetailsInteractionListener,
) {
    TopBar(
        leftContent = {
            TopBarClickableIcon(
                icon = painterResource(R.drawable.icon_back),
                onClick = interactionListener::onBackClick
            )
        },
        screenTitle = title,
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
}

@Composable
private fun SavedDetailsListSectionContent(
    mediaList: LazyPagingItems<MediaItem>,
    onMediaClick: (MediaItem) -> Unit,
    modifier: Modifier = Modifier,
    onSaveIconClick: (MediaItem) -> Unit = {},
    isScrollEnabled: Boolean = true,
) {
    val isListEmpty = mediaList.itemCount == 0

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        when {
            mediaList.loadState.refresh is LoadState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    LoadingIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            isListEmpty -> {

                EmptyItemsScreen(
                    messageText = stringResource(R.string.the_list_is_empty))
            }

            else -> {
                PaginatedMediaListGrid(
                    mediaList = mediaList,
                    onMediaClick = onMediaClick,
                    onSaveIconClick = onSaveIconClick,
                    isScrollEnabled = isScrollEnabled,
                )

                if (mediaList.loadState.append is LoadState.Loading) {
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