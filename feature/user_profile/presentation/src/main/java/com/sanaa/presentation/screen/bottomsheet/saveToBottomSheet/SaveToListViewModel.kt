package com.sanaa.presentation.screen.bottomsheet.saveToBottomSheet

import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.profileBase.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import repository.SavedListsStatusProvider
import usecase.custom_list.ManageSavedListItemsUseCase
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject

@HiltViewModel
class SaveToListViewModel @Inject constructor(
    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase,
    private val listsStatusProvider: SavedListsStatusProvider,
) : BaseViewModel<SaveToListUiState, SaveToListEffect>(SaveToListUiState()) {

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
        updateState {
            it.copy(
                playlists = playlist.map {
                    PlaylistUiItem(
                        title = it.title,
                        itemCount = it.itemCount,
                        id = it.id.toLong()
                    )
                }
            )
        }
    }

    fun onPlaylistSelected(listId: Long) {
        updateState {
            it.copy(
                selectedListId = listId,
                isAddButtonEnabled = true
            )
        }
    }

    fun onAddClicked(mediaId: Long) {
        val selectedListId = state.value.selectedListId ?: return
        if (!state.value.isAddButtonEnabled) return

        updateState { it.copy(isLoading = true, errorMessage = null) }

        tryToExecute(
            callee = addMovieToSavedList(selectedListId, mediaId),
            onSuccess = onAddMovieToSavedListSuccess(mediaId),
            onError = onErrorAccrue()
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
        updateState { it.copy(isLoading = false) }
        listsStatusProvider.markItemSaved(mediaId.toInt())
        viewModelScope.launch {
            listsStatusProvider.refreshLists()
        }
        emitEffect(SaveToListEffect.AddedSuccessfully)
    }

    private fun onErrorAccrue(): (Throwable) -> Unit = {
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = "Failed to add item to list."
            )
        }
        emitEffect(SaveToListEffect.FailedToAdd)
    }
}