package com.sanaa.presentation.screen.playlist

import com.sanaa.presentation.savedBase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.CheckIfUserIsLoggedInUseCase
import javax.inject.Inject

@HiltViewModel
class PlayListScreenViewModel @Inject constructor(
    private val checkUserLogin: CheckIfUserIsLoggedInUseCase,
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

    }

    fun onListDeletedSuccessfully() {
        emitEffect(PlayListScreenEffect.ShowSuccessToDeleteListSnackBar)
    }


    override fun onFabBottomSheetClicked() {
        updateState { copy(showAddBottomSheet = true) }
    }

    override fun onButtonLoginClicked() {
        emitEffect(PlayListScreenEffect.NavigateToLogin)
    }


    override fun onDismissAddBottomSheet() {
        updateState { copy(showAddBottomSheet = false) }
    }

    override fun onItemListClicked(listId: Int, title: String) {
        emitEffect(PlayListScreenEffect.NavigateToSavedDetails(listId, title))
    }
}