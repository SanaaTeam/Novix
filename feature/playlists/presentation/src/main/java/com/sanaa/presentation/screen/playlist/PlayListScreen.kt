package com.sanaa.presentation.screen.playlist

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.api.AuthStartRoute
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.presentation.api.navigationSaved.LocalNavControllerProvider
import com.sanaa.presentation.api.navigationSaved.PlayListApiEntryPoint
import com.sanaa.presentation.api.navigationSaved.SavedDetailsScreenRoute
import com.sanaa.presentation.screen.playlist.componants.AnimatedSnackBarHost
import com.sanaa.presentation.screen.playlist.componants.PlayListGuestScreen
import com.sanaa.presentation.screen.playlist.componants.PlaylistEmptyScreen
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PlaylistScreen(viewModel: PlayListScreenViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavControllerProvider.current

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
        effect = viewModel.effect,
    )

    PlaylistScreenContent(
        interactionListener = viewModel,
        state = state.value
    )
}

@Composable
private fun PlaylistEffectsHandler(
    effect: Flow<PlayListScreenEffect>,
) {
    val navController = LocalNavControllerProvider.current
    val context = LocalContext.current
    val authApi = EntryPointAccessors.fromApplication(
        context,
        PlayListApiEntryPoint::class.java
    ).authenticationApi()


    LaunchedEffect(Unit) {
       effect.collectLatest { effect ->
            when (effect) {
                PlayListScreenEffect.NavigateToLogin -> {
                    authApi.launch(context, AuthStartRoute.Login)
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
            targetState = state.screenState,
        ) { screenState ->
            when (screenState) {
                PlaylistScreenState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator()
                    }
                }

                PlaylistScreenState.NoInternet -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        NetworkDisconnectionContact(
                            onRetryClick = { interactionListener.onRetryLoadSavedLists() },
                        )
                    }
                }

                PlaylistScreenState.Guest -> {
                    PlayListGuestScreen(onLoginClick = { interactionListener.onNavigateToLogin() })
                }

                PlaylistScreenState.Empty -> {
                    PlaylistEmptyScreen(
                        onFabClick = { interactionListener.onAddNewListClicked() },
                        isVisible = state.showAddBottomSheet,
                        onDismissAddBottomSheet = { interactionListener.onDismissAddBottomSheet() },
                    )
                }

                PlaylistScreenState.WithItems -> {
                    PlayListWithItemsScreen(
                        isVisible = state.showAddBottomSheet,
                        lists = state.lists,
                        interactionListener = interactionListener,
                    )
                }
            }
        }
        AnimatedSnackBarHost(data = state.snackData, onDismiss = interactionListener::onSnackBarDismiss)
    }
}

