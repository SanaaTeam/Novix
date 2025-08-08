package com.sanaa.presentation.screen.playlist

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.api.launchAuthActivityForResult
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.feature.playlists.presentation.R
import com.sanaa.designsystem.design_system.component.navigation.LocalNavControllerProvider
import com.sanaa.presentation.api.navigationSaved.PlayListApiEntryPoint
import com.sanaa.presentation.api.navigationSaved.SavedDetailsScreenRoute
import com.sanaa.presentation.bottomsheets.addEditBookmark.AddBookmarkListViewModel
import com.sanaa.presentation.screen.playlist.componants.AnimatedSnackBarHost
import com.sanaa.presentation.screen.playlist.componants.PlayListGuestScreen
import com.sanaa.presentation.screen.playlist.componants.PlaylistEmptyScreen
import dagger.hilt.android.EntryPointAccessors

@Composable
fun PlaylistScreen(viewModel: PlayListScreenViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val addedToListSuccessMsg = stringResource(R.string.added_to_list_successfully)
    val addedToListFailedMsg = stringResource(R.string.added_to_list_failed)
    val deleteListFailedMsg = stringResource(R.string.deleted_list_failed)
    val deleteListSuccessMsg = stringResource(R.string.deleted_list_successfully)
    val context = LocalContext.current
    var snack by remember { mutableStateOf<SnackData?>(null) }
    val navController = LocalNavControllerProvider.current
    val addListViewModel: AddBookmarkListViewModel = hiltViewModel()


    val authApi = EntryPointAccessors.fromApplication(
        context,
        PlayListApiEntryPoint::class.java
    ).authenticationApi()

    val launcher = launchAuthActivityForResult()


    LaunchedEffect(Unit) {
        snapshotFlow { navController.currentBackStackEntry }
            .collect { backStackEntry ->

                val deleted =
                    backStackEntry?.savedStateHandle?.get<Boolean>("list_deleted") ?: false

                if (deleted) {
                    viewModel.onListDeletedSuccessfully()

                    backStackEntry?.savedStateHandle?.remove<Boolean>("list_deleted")
                    backStackEntry?.savedStateHandle?.remove<Boolean>("delete_success")
                }
            }
    }




    LaunchedEffect(Unit) {
        addListViewModel.effect.collect {
            viewModel.onListAdded()
        }
    }

    LaunchedEffect(Unit) {
        addListViewModel.state.collect { state ->
            if (state.errorMessage != null) {
                viewModel.onListAddFailed()
            }
        }
    }



    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
                PlayListScreenEffect.NavigateToLogin -> {
                    launcher.launch(authApi.getLaunchIntent(context))
                }

                PlayListScreenEffect.ShowErrorToAddListSnackBar -> {
                    snack = SnackData(message = addedToListFailedMsg, isError = true)
                }

                PlayListScreenEffect.ShowSuccessToAddListSnackBar -> {
                    snack = SnackData(message = addedToListSuccessMsg, isError = false)
                }

                PlayListScreenEffect.ShowErrorToDeleteListSnackBar -> {
                    snack = SnackData(message = deleteListFailedMsg, isError = true)

                }

                PlayListScreenEffect.ShowSuccessToDeleteListSnackBar -> {
                    snack = SnackData(message = deleteListSuccessMsg, isError = false)

                }

                is PlayListScreenEffect.NavigateToSavedDetails ->
                    navController.navigate(SavedDetailsScreenRoute(it.listId, it.title).route())

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
    AnimatedContent(
        targetState = Triple(state.isUserLoggedIn, state.lists.isEmpty(), state.lists),
        label = "PlaylistContentTransition"
    ) { (isUserLoggedIn, isEmptyList, lists) ->
        when {
            !isUserLoggedIn -> {
                PlayListGuestScreen(onLoginClick = { interactionListener.onButtonLoginClicked() })
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
                    onFabClick = { interactionListener.onFabBottomSheetClicked() },
                    isVisible = state.showAddBottomSheet,
                    onDismissAddBottomSheet = { interactionListener.onDismissAddBottomSheet() },
                    lists = lists,
                    onItemClick = interactionListener::onItemListClicked
                )
            }
        }
    }
}


@PreviewLightDark
@Composable
fun Preview_PlaylistScreenContent_Guest() {
    NovixTheme(
        isSystemInDarkTheme()
    ) {
        PlaylistScreenContent(
            interactionListener = fakeListener(),
            state = PlayListScreenUiState(
                isUserLoggedIn = false,
                lists = emptyList()
            )
        )
    }
}

@PreviewLightDark
@Composable
fun Preview_PlaylistScreenContent_EmptyList() {
    NovixTheme(
        isSystemInDarkTheme()
    ) {
        PlaylistScreenContent(
            interactionListener = fakeListener(),
            state = PlayListScreenUiState(
                isUserLoggedIn = true,
                lists = emptyList()
            )
        )
    }
}

@PreviewLightDark
@Composable
fun Preview_PlaylistScreenContent_WithItems() {
    NovixTheme(
        isSystemInDarkTheme()
    ) {
        PlaylistScreenContent(
            interactionListener = fakeListener(),
            state = PlayListScreenUiState(
                isUserLoggedIn = true,
                lists = listOf(
                    PlayListUiModel(id = 1, title = "Favorites", mediaCount = 5),
                    PlayListUiModel(id = 2, title = "Watch Later", mediaCount = 10),
                    PlayListUiModel(id = 2, title = "Watch Later", mediaCount = 13),
                    PlayListUiModel(id = 2, title = "Watch Later", mediaCount = 7),
                    PlayListUiModel(id = 2, title = "Watch Later", mediaCount = 8),
                )
            )
        )
    }
}

@Composable
fun fakeListener() = object : PlayListScreenInteractionListener {
    override fun onFabBottomSheetClicked() {}
    override fun onButtonLoginClicked() {}
    override fun onDismissAddBottomSheet() {}
    override fun onItemListClicked(listId: Int, title: String) {}
}
