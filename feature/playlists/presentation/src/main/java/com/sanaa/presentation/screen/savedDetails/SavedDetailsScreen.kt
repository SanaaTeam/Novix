package com.sanaa.presentation.screen.savedDetails

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
import com.sanaa.feature.playlists.presentation.R
import com.sanaa.presentation.screen.saved.SnackData
import com.sanaa.presentation.screen.savedDetails.components.SavedDetailsListSectionContent
import com.sanaa.presentation.screen.savedDetails.state.SavedDetailsScreenUiState

@Composable
fun SavedDetailsScreen(
    viewModel: SavedDetailsScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val editedListSuccessMsg = stringResource(R.string.edited_to_list_successfully)
    val editedListFailedMsg = stringResource(R.string.edited_to_list_failed)
    val context = LocalContext.current
    var snack by remember { mutableStateOf<SnackData?>(null) }


    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
                SavedDetailsScreenEffect.NavigateBack -> {
                    //
                }

                SavedDetailsScreenEffect.ShowErrorSnackBar -> {
                    snack = SnackData(editedListFailedMsg, isError = true)
                }

                SavedDetailsScreenEffect.ShowSuccessSnackBar -> {
                    snack = SnackData(editedListSuccessMsg, isError = false)
                }
            }
        }

    }

    SavedDetailsContent(
        state = state.value,
        interactionListener = viewModel
    )
}


@Composable
fun SavedDetailsContent(
    state: SavedDetailsScreenUiState = SavedDetailsScreenUiState(),
    interactionListener: SavedDetailsInteractionListener,
    modifier: Modifier = Modifier,

    ) {
    val movieList = state.movieList.collectAsLazyPagingItems()


    NovixScaffold(
        topBar = {
            SavedDetailsTopBar(title = "My List")
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
    }
}


@Composable
fun SavedDetailsTopBar(title: String) {
    TopBar(
        leftContent = {
            TopBarClickableIcon(
                icon = painterResource(R.drawable.icon_back),
                onClick = { }
            )
        },
        screenTitle = title,
        rightContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TopBarClickableIcon(
                    icon = painterResource(R.drawable.pencil_edit),
                    onClick = {}
                )
                TopBarClickableIcon(
                    icon = painterResource(R.drawable.icon_deleat),
                    onClick = {}
                )
            }

        },
        modifier = Modifier
            .systemBarsPadding()
            .zIndex(10f)
    )
}




