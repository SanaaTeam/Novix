package com.sanaa.presentation.bottomsheet.saveToListBottomsheet

import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repository.SavedListsStatusProvider
import usecase.custom_list.ManageSavedListItemsUseCase
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject

@HiltViewModel
class SaveToListViewModel @Inject constructor(
    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase,
    private val listsStatusProvider: SavedListsStatusProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<SaveToListUiState, SaveToListEffect>(SaveToListUiState(), dispatcher),
    SaveToListInteractionListener {

    init {
        observePlaylists()
    }

    private fun observePlaylists() {
        tryToCollect(
            callee = { listsStatusProvider.savedLists },
            onCollect = ::onCollectPlaylists,
        )
    }

    private fun onCollectPlaylists(playlist: List<SavedList>) {
        updateState { copy(playlists = playlist.toState()) }
    }

    override fun onPlaylistSelected(listId: Long) {
        updateState {
            copy(
                selectedListId = listId,
                isAddButtonEnabled = true
            )
        }
    }

    override fun onAddClicked(mediaId: Long) {
        val selectedListId = state.value.selectedListId ?: return
        if (!state.value.isAddButtonEnabled) return

        updateState { copy(isLoading = true, errorMessage = null) }

        tryToExecute(
            callee = addMovieToSavedList(selectedListId, mediaId),
            onSuccess = onAddMovieToSavedListSuccess(mediaId),
            onError = ::onErrorAccrue
        )
    }

    private fun addMovieToSavedList(
        selectedListId: Long,
        mediaId: Long,
    ): suspend () -> Boolean = {
        manageSavedListItemsUseCase.addMovieToSavedList(
            listId = selectedListId.toInt(),
            movieId = mediaId.toInt()
        )
    }

    private fun onAddMovieToSavedListSuccess(mediaId: Long): (Boolean) -> Unit = {
        updateState { copy(isLoading = false) }
        listsStatusProvider.markItemSaved(mediaId.toInt())
        viewModelScope.launch {
            listsStatusProvider.refreshLists()
        }
        emitEffect(SaveToListEffect.AddedSuccessfully)
    }

    private fun onErrorAccrue(throwable: Throwable): () -> Unit = {
        updateState {
            copy(
                isLoading = false,
                errorMessage = "Failed to add item to list."
            )
        }
        emitEffect(SaveToListEffect.FailedToAdd)
    }
}