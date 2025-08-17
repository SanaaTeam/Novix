package com.sanaa.presentation.screen.playlist

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.api.launchAuthActivityForResult
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.presentation.api.navigationSaved.LocalNavControllerProvider
import com.sanaa.presentation.api.navigationSaved.PlayListApiEntryPoint
import com.sanaa.presentation.api.navigationSaved.SavedDetailsScreenRoute
import com.sanaa.presentation.screen.playlist.componants.AnimatedSnackBarHost
import com.sanaa.presentation.screen.playlist.componants.PlayListGuestScreen
import com.sanaa.presentation.screen.playlist.componants.PlaylistEmptyScreen
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PlaylistScreen(viewModel: PlayListScreenViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavControllerProvider.current
    var snackBarData by remember { mutableStateOf<SnackData?>(null) }

    LaunchedEffect(Unit) {
        snapshotFlow { navController.currentBackStackEntry }
            .collect { backStackEntry ->
                val deleted =
                    backStackEntry?.savedStateHandle?.get<Boolean>("list_deleted") == true
                if (deleted) {
                    viewModel.onListDeletedSuccessfully()
                    backStackEntry.savedStateHandle.remove<Boolean>("list_deleted")
                }
            }
    }

    PlaylistEffectsHandler(
        viewModel = viewModel,
        interactionListener = viewModel,
        onShowSnack = { snackBarData = it }
    )


    PlaylistScreenContent(
        interactionListener = viewModel,
        state = state.value
    )

}

@Composable
private fun PlaylistEffectsHandler(
    viewModel: PlayListScreenViewModel,
    interactionListener: PlayListScreenInteractionListener,
    onShowSnack: (SnackData) -> Unit
) {
    val navController = LocalNavControllerProvider.current
    val context = LocalContext.current
    val authApi = EntryPointAccessors.fromApplication(
        context,
        PlayListApiEntryPoint::class.java
    ).authenticationApi()

    val launcher = launchAuthActivityForResult()

    LaunchedEffect(viewModel) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                PlayListScreenEffect.NavigateToLogin -> {
                    launcher.launch(authApi.getLaunchIntent(context))
                    interactionListener.onNavigateToLogin()
                }

                is PlayListScreenEffect.NavigateToSavedDetails ->
                    navController.navigate(SavedDetailsScreenRoute(effect.listId, effect.title).route())
            }
        }
    }
}

@Composable
private fun PlaylistScreenContent(
    state: PlayListScreenUiState,
    interactionListener: PlayListScreenInteractionListener,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        AnimatedContent(
            targetState = Triple(state.isUserLoggedIn, state.lists.isEmpty(), state.lists),
        ) { (isUserLoggedIn, isEmptyList, lists) ->
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator()
                    }
                }

                state.noInternetConnection -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        NetworkDisconnectionContact(
                            onRetryClick = { },
                        )
                    }
                }

                !isUserLoggedIn -> {
                    PlayListGuestScreen(onLoginClick = { interactionListener.onNavigateToLogin() })
                }

                isEmptyList -> {
                    PlaylistEmptyScreen(
                        onFabClick = { interactionListener.onFabBottomSheetClicked() },
                        isVisible = state.showAddBottomSheet,
                        onDismissAddBottomSheet = { interactionListener.onDismissAddBottomSheet() },
                    )
                }

                else -> {
                    PlayListWithItemsScreen(
                        isVisible = state.showAddBottomSheet,
                        lists = lists,
                        interactionListener = interactionListener,
                        isUserLoggedIn = state.isUserLoggedIn
                    )
                }
            }
        }
        AnimatedSnackBarHost(data = state.snackData, onDismiss = interactionListener::onSnackBarDismiss)
    }
}

