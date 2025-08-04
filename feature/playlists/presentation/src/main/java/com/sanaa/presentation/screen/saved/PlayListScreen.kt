package com.sanaa.presentation.screen.saved

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.api.launchAuthActivityForResult
import com.sanaa.feature.playlists.presentation.R
import com.sanaa.presentation.navigation.PlayListApiEntryPoint
import com.sanaa.presentation.screen.saved.componants.AnimatedSnackBarHost
import com.sanaa.presentation.screen.saved.componants.PlayListGuestScreen
import com.sanaa.presentation.screen.saved.componants.PlaylistEmptyScreen
import dagger.hilt.android.EntryPointAccessors

@Composable
fun PlaylistScreen(viewModel: PlayListScreenViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val addedToListSuccessMsg = stringResource(R.string.added_to_list_successfully)
    val addedToListFailedMsg = stringResource(R.string.added_to_list_failed)
    val context = LocalContext.current
    var snack by remember { mutableStateOf<SnackData?>(null) }

    val authApi = EntryPointAccessors.fromApplication(
        context,
        PlayListApiEntryPoint::class.java
    ).authenticationApi()

    val launcher = launchAuthActivityForResult(
        loggedInWithSessionId = {
            viewModel.updateUserStatus()
        },
        loggedInAsGuest = {
            viewModel.updateUserStatus()
        }
    )
    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
                PlayListScreenEffect.NavigateToLogin -> {
                    launcher.launch(authApi.getLaunchIntent(context))
                }

                PlayListScreenEffect.ShowErrorSnackBar -> {
                    snack = SnackData(addedToListFailedMsg, isError = true)
                }

                PlayListScreenEffect.ShowSuccessSnackBar -> {
                    snack = SnackData(addedToListSuccessMsg, isError = false)
                }
            }
        }

    }
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
    when {
        !state.isUserLoggedIn -> {
            PlayListGuestScreen(onLoginClick = { interactionListener.onButtonLoginClicked() })
        }

        state.lists.isEmpty() -> {
            PlaylistEmptyScreen(onFabClick = { interactionListener.onFabBottomSheetClicked() })
        }

        else -> {
            PlayListWithItemsScreen(
                lists = state.lists,
                onItemClick = { interactionListener.onItemListClicked() }
            )
        }
    }

}