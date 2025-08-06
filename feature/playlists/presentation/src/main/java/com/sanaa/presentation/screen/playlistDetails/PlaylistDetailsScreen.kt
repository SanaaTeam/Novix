package com.sanaa.presentation.screen.playlistDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.playlists.presentation.R
import com.sanaa.presentation.api.navigationSaved.LocalNavControllerProvider
import com.sanaa.presentation.bottomsheets.deletebottomsheet.DeleteConfirmationBottomSheet
import com.sanaa.presentation.screen.playlist.SnackData
import com.sanaa.presentation.screen.playlistDetails.components.SavedDetailsListSectionContent
import com.sanaa.presentation.screen.playlistDetails.state.SavedDetailsScreenUiState

@Composable
fun PlaylistDetailsScreen(
    viewModel: PlaylistDetailsScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val editedListSuccessMsg = stringResource(R.string.edited_to_list_successfully)
    val editedListFailedMsg = stringResource(R.string.edited_to_list_failed)
    val context = LocalContext.current
    var snack by remember { mutableStateOf<SnackData?>(null) }

    val navController = LocalNavControllerProvider.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
                PlaylistDetailsScreenEffect.NavigateBack -> {
                    navController.previousBackStackEntry?.savedStateHandle?.set("list_deleted", true)
                    navController.previousBackStackEntry?.savedStateHandle?.set("delete_success", true)
                    navController.popBackStack()
                }

                PlaylistDetailsScreenEffect.ShowErrorSnackBar -> {
                    snack = SnackData(message = editedListFailedMsg, isError = true)
                }

                PlaylistDetailsScreenEffect.ShowSuccessSnackBar -> {
                    snack = SnackData(message = editedListSuccessMsg, isError = false)
                }
            }
        }

    }

    PlaylistDetailsContent(
        state = state.value,
        interactionListener = viewModel
    )
}


@Composable
fun PlaylistDetailsContent(
    state: SavedDetailsScreenUiState = SavedDetailsScreenUiState(),
    interactionListener: PlaylistDetailsInteractionListener,
    modifier: Modifier = Modifier,
    ) {
    val movieList = state.movieList.collectAsLazyPagingItems()


    NovixScaffold(
        topBar = {
            PlaylistDetailsTopBar(
                title = state.title.orEmpty(),
                interactionListener = interactionListener,
                onDeleteListClicked = { interactionListener.onDeleteListClicked() }
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
                onMediaClick = { interactionListener.onMediaClick(it.id) },
                onSaveIconClick = { interactionListener.onSaveIconClick(it) }
            )

        }
        DeleteConfirmationBottomSheet(
            isVisible = state.showBottomSheet,
            listId = state.listId?.toLong(),
            onDismiss = { interactionListener.onDismissBottomSheet() },
            onDeleteSuccess = { interactionListener.onListDeletedSuccessfully() }
        )
    }
}


@Composable
fun PlaylistDetailsTopBar(
    title: String,
    interactionListener: PlaylistDetailsInteractionListener,
    onDeleteListClicked: () -> Unit
) {
    TopBar(
        leftContent = {
            TopBarClickableIcon(
                icon = painterResource(R.drawable.icon_back),
                onClick = { interactionListener.onBackClick() }
            )
        },
        screenTitle = title,
        rightContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TopBarClickableIcon(
                    icon = painterResource(R.drawable.icon_deleat),
                    onClick = { onDeleteListClicked() },
                    tint = Theme.colors.statusColors.redAccent
                )
            }

        },
        modifier = Modifier
            .systemBarsPadding()
            .zIndex(10f)
    )
}




