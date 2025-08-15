package com.sanaa.presentation.bottomsheet.saveToListBottomsheet

import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import repository.SavedListsStatusProvider
import usecase.custom_list.ManageSavedListItemsUseCase
import usecase.custom_list.ManageSavedListsUseCase
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject

@HiltViewModel
class SaveToListViewModel @Inject constructor(
    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase,
    private val mangeSavedListsUseCase: ManageSavedListsUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<SaveToListUiState, SaveToListEffect>(SaveToListUiState(), dispatcher) {

    init {
        observePlaylists()
    }

    private fun observePlaylists() {
        tryToCollect(
            block = { mangeSavedListsUseCase.getSavedLists() },
            onCollect = ::onCollectPlaylists,
        )
    }

    private fun onCollectPlaylists(playlist: List<SavedList>) {
        updateState { copy(playlists = playlist.toState()) }
    }

    fun onPlaylistSelected(listId: Long) {
        updateState {
            copy(
                selectedListId = listId,
                isAddButtonEnabled = true
            )
        }
    }

    fun onAddClicked(mediaId: Long) {
        val selectedListId = state.value.selectedListId ?: return
        if (!state.value.isAddButtonEnabled) return

        updateState { copy(isLoading = true, errorMessage = null) }

        tryToExecute(
            block = addMovieToSavedList(selectedListId, mediaId),
            onError = ::onErrorAccrue
        )
    }

    private fun addMovieToSavedList(
        selectedListId: Long,
        mediaId: Long,
    ): suspend () -> Flow<Boolean> = {
        manageSavedListItemsUseCase.addMovieToSavedList(
            listId = selectedListId.toInt(),
            movieId = mediaId.toInt()
        )
    }

    private fun onErrorAccrue(exception: NovixAppException): () -> Unit = {
        updateState {
            copy(
                isLoading = false,
                errorMessage = "Failed to add item to list."
            )
        }
        emitEffect(SaveToListEffect.FailedToAdd)
    }
}