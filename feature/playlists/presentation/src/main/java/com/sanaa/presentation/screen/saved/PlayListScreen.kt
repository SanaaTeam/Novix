package com.sanaa.presentation.screen.saved

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.feature.playlists.presentation.R
import com.sanaa.presentation.screen.saved.componants.AnimatedSnackBarHost
import com.sanaa.presentation.screen.saved.componants.PlayListGuestScreen
import com.sanaa.presentation.screen.saved.componants.PlaylistEmptyScreen

@Composable
fun PlaylistScreen(viewModel: PlayListScreenViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val addedToListSuccessMsg = stringResource(R.string.added_to_list_successfully)
    val addedToListFailedMsg = stringResource(R.string.added_to_list_failed)
    val context = LocalContext.current
    var snack by remember { mutableStateOf<SnackData?>(null) }

    Box {
        PlaylistScreenContent(
            interactionListener = viewModel,
            state = state.value
        )
        AnimatedSnackBarHost(
            data = snack,
            onDismiss = { snack = null }
        )
    }
}

@Composable
fun PlaylistScreenContent(
    interactionListener: PlayListScreenInteractionListener,
    state: PlayListScreenUiState
) {
    if (!state.isUserLoggedIn) {
        PlayListGuestScreen(onLoginClick = { interactionListener.onButtonLoginClicked() })
    } else {
        PlaylistEmptyScreen(
            onFabClick = { interactionListener.onFabBottomSheetClicked() }
        )
    }

}