package com.sanaa.presentation.bottomsheet.saveToListBottomsheet

import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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


    fun getMediaId(mediaId: Long) {
        updateState { copy(mediaId = mediaId) }
        observePlaylists()
    }

    private fun observePlaylists() {
        tryToCollect(
            block = { mangeSavedListsUseCase.getSavedLists() },
            onCollect = ::onCollectPlaylists,
        )
    }

    private fun onCollectPlaylists(playlist: List<SavedList>) {
        val tempPlayList = playlist.toState()
        tempPlayList.forEach { playlistUiItem: PlaylistUiItem ->
            playlistUiItem.containsMediaItem = playlistUiItem.itemsIds.contains(state.value.mediaId)
        }
        updateState { copy(playlists = tempPlayList) }
    }

    fun onPlayListClicked(listId: Long) {
        state.value.playlists.map { playList ->
            if (playList.containsMediaItem) return
        }
        val selectedListsIds = state.value.selectedListsIds
        selectedListsIds.contains(listId).let { exists ->
            if (exists) {
                addSelectedList(selectedListsIds, listId)
            } else {
                removeUnSelectedList(selectedListsIds, listId)
            }
        }
    }

    private fun removeUnSelectedList(
        selectedListsIds: MutableList<Long>,
        listId: Long
    ) {
        selectedListsIds.remove(listId)
        updateState {
            copy(
                selectedListsIds = selectedListsIds,
                isAddButtonEnabled = selectedListsIds.isNotEmpty()
            )
        }
    }

    private fun addSelectedList(
        selectedListsIds: MutableList<Long>,
        listId: Long
    ) {
        selectedListsIds.add(listId)
        updateState {
            copy(
                selectedListsIds = selectedListsIds,
                isAddButtonEnabled = true
            )
        }
    }

//    fun onPlaylistSelected(listId: Long) {
//        val list = state.value.selectedListsIds
//        list.add(listId)
//        updateState {
//            copy(
//                selectedListsIds = list,
//                isAddButtonEnabled = true
//            )
//        }
//    }
//    fun onPlayListDeSelected(listId: Long){
//        val list = state.value.selectedListsIds
//        list.remove(listId)
//        updateState {
//            copy(
//                selectedListsIds = list,
//                isAddButtonEnabled = list.isNotEmpty()
//            )
//        }
//    }

    fun onAddClicked(mediaId: Long) {
        val selectedListsIds: MutableList<Long> = state.value.selectedListsIds ?: return
        if (!state.value.isAddButtonEnabled) return
        updateState { copy(isLoading = true, errorMessage = null) }
        tryToExecute(
            block = {
                selectedListsIds.isNotEmpty().let { notEmpty->
                    if (notEmpty){
                        selectedListsIds.forEach { selectedListId ->
                            addMovieToSavedList(selectedListId, mediaId)
                        }
                    }
                }
            },
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