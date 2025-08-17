package com.sanaa.presentation.screen.playlist

import com.sanaa.presentation.savedBase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NoNetworkException
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import service.VodStringProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.custom_list.ManageSavedListsUseCase
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject

@HiltViewModel
class PlayListScreenViewModel @Inject constructor(
    private val checkUserLogin: CheckIfUserIsLoggedInUseCase,
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) :
    BaseViewModel<PlayListScreenUiState, PlayListScreenEffect>(
        PlayListScreenUiState(),
        defaultDispatcher = dispatcher
    ),
    PlayListScreenInteractionListener {

    init {
        loadSavedLists()
    }

    fun loadSavedLists() {
        tryToCollect(
            callee = { checkUserLogin.isLoggedIn() },
            onCollect = { isUserLoggedIn ->
                updateState { copy(isUserLoggedIn = isUserLoggedIn) }
                if (isUserLoggedIn) {
                    fetchAndHandleSavedLists()
                }
            }
        )
    }

    private fun fetchAndHandleSavedLists() {
        tryToCollect(
            callee = { manageSavedListsUseCase.getSavedLists() },
            onCollect = { savedLists ->
                handleSuccessfulFetch(savedLists)
            },
            onError = { e ->
                handleFetchError(e)
            }
        )
    }

    private fun handleSuccessfulFetch(savedLists: List<SavedList>) {
        updateState {
            copy(
                isLoading = false,
                lists = savedLists.map { it.toUiModel() }
            )
        }
    }

    private fun handleFetchError(e: NovixAppException) {
        updateState {
            when (e) {
                is NoNetworkException -> copy(
                    isLoading = false,
                    noInternetConnection = true,
                    snackData = SnackData(
                        message = stringProvider.noInternetConnectionError,
                        isError = false
                    )
                )

                else -> copy(
                    isLoading = false,
                    snackData = SnackData(
                        message = stringProvider.somethingWentWrongError,
                        isError = true
                    )
                )
            }
        }
    }

    fun onListDeletedSuccessfully() {
        updateState {
            copy(
                snackData = SnackData(message = stringProvider.deleteListSuccess, isError = false)
            )
        }
    }

    override fun onSnackBarDismiss() {
        updateState { copy(snackData = null) }
    }

    override fun onFabBottomSheetClicked() {
        updateState { copy(showAddBottomSheet = true) }
    }

    override fun onNavigateToLogin() {
        emitEffect(PlayListScreenEffect.NavigateToLogin)
    }

    override fun onDismissAddBottomSheet() {
        updateState { copy(showAddBottomSheet = false) }
    }

    override fun onRetryLoadSavedLists() {
        loadSavedLists()
    }

    override fun onNavigateToSavedDetails(listId: Int, title: String) {
        emitEffect(PlayListScreenEffect.NavigateToSavedDetails(listId, title))
    }
}