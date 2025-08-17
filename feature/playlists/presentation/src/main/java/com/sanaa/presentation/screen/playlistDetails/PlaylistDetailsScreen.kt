package com.sanaa.presentation.screen.playlistDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.playlists.presentation.R
import com.sanaa.presentation.api.navigationSaved.LocalNavControllerProvider
import com.sanaa.presentation.api.navigationSaved.PlaylistsApiEntryPoint
import com.sanaa.presentation.screen.playlist.componants.AnimatedSnackBarHost
import com.sanaa.presentation.screen.playlistDetails.components.DeleteConfirmationBottomSheet
import com.sanaa.presentation.screen.playlistDetails.components.SavedDetailsListSectionContent
import com.sanaa.presentation.screen.playlistDetails.state.MediaItem
import com.sanaa.presentation.screen.playlistDetails.state.MediaTypeUi
import com.sanaa.presentation.screen.playlistDetails.state.SavedDetailsScreenUiState
import dagger.hilt.android.EntryPointAccessors

@Composable
fun PlaylistDetailsScreen(
    viewModel: PlaylistDetailsScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val movieList = state.value.movieList.collectAsLazyPagingItems()
    val context = LocalContext.current

    val navController = LocalNavControllerProvider.current

    val detailsApi: MediaDetailsApi = remember {
        EntryPointAccessors
            .fromApplication(context, PlaylistsApiEntryPoint::class.java)
            .detailsApi()
    }
    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
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
                        id = it.mediaId,
                        startRoute = StartRoute.MOVIE
                    )
                }

                PlaylistDetailsScreenEffect.RefreshList -> {
                    movieList.refresh()
                }
            }
        }

    }
    Box(modifier = Modifier.fillMaxSize()) {
        PlaylistDetailsContent(
            state = state.value,
            movieList = movieList,
            interactionListener = viewModel
        )
        AnimatedSnackBarHost(
            data = state.value.snackBarData,
            onDismiss = viewModel::onSnackBarDismiss,
        )
    }
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

        }
    ) {
        Column(
            modifier = modifier
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            SavedDetailsListSectionContent(
                mediaList = movieList,
                onMediaClick = { interactionListener.onMediaClick(it.id, MediaTypeUi.MOVIE) },
                onSaveIconClick = { interactionListener.onDeleteIconClick(it) }
            )


        }
        DeleteConfirmationBottomSheet(
            isVisible = state.showBottomSheet,
            isLoading = state.isLoading,
            onDismiss = { interactionListener.onDismissBottomSheet() },
            onConfirm = interactionListener::onDeleteListConfirmed
        )
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




