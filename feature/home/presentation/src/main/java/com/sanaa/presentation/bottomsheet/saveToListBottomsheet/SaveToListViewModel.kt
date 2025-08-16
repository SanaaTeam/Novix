package com.sanaa.presentation.bottomsheet.saveToListBottomsheet

import android.util.Log
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.components.SnackData
import com.sanaa.presentation.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import service.VodStringProvider
import usecase.custom_list.ManageSavedListItemsUseCase
import usecase.custom_list.ManageSavedListsUseCase
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject

@HiltViewModel
class SaveToListViewModel @Inject constructor(
    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase,
    private val mangeSavedListsUseCase: ManageSavedListsUseCase,
    private val stringProvider: VodStringProvider,
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

    fun clearBottomSheetState(){
        updateState {
            copy(
                selectedListsIds = mutableListOf(),
                mediaId = null
            )
        }
    }


    fun onPlayListClicked(listId: Long) {
        Log.d(TAG, "viewModel:onPlayListClicked: listId:$listId")
        val targetPlaylist = state.value.playlists.find { it.id == listId }
        if (targetPlaylist?.containsMediaItem == true) return

        val selectedListsIds = state.value.selectedListsIds
        if (listId in selectedListsIds) {
            removeUnSelectedList(selectedListsIds, listId)
            Log.d(
                TAG,
                "viewModel:onPlayListClicked: selectedListIds.contains list id : exists= true ->removing list id"
            )
        } else {
            addSelectedList(selectedListsIds, listId)
            Log.d(
                TAG,
                "viewModel:onPlayListClicked: selectedListIds.contains list id : exists= false ->adding list id"
            )
        }
    }

    private fun removeUnSelectedList(selectedListsIds: List<Long>, listId: Long) {
        val updated = selectedListsIds.toMutableList().apply { remove(listId) }
        updateState {
            copy(
                selectedListsIds = updated,
                isAddButtonEnabled = updated.isNotEmpty()
            )
        }
        Log.d(TAG, "viewModel:removeUnSelectedList: removed // updated state.selectedListIds:${state.value.selectedListsIds} ")
    }

    private fun addSelectedList(selectedListsIds: List<Long>, listId: Long) {
        val updated = selectedListsIds.toMutableList().apply { add(listId) }
        updateState {
            copy(
                selectedListsIds = updated,
                isAddButtonEnabled = true
            )
        }

        Log.d(TAG, "viewModel:addSelectedList: added // updated state.selectedListIds:${state.value.selectedListsIds} ")
    }


    fun onAddClicked(mediaId: Long) {
        val selectedListsIds: MutableList<Long> = state.value.selectedListsIds ?: return
        if (!state.value.isAddButtonEnabled) return
        updateState { copy(isLoading = true) }
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

    private suspend fun addMovieToSavedList(
        selectedListId: Long,
        mediaId: Long,
    ){
        manageSavedListItemsUseCase.addMovieToSavedList(
            listId = selectedListId.toInt(),
            movieId = mediaId.toInt()
        )
        emitEffect(SaveToListEffect.AddedSuccessfully)
        updateState {
            copy(
                isLoading = false,
                snackBarData = SnackData(
                    message = stringProvider.addToListSuccess,
                    isError = false
                )
            )
        }
    }

    private fun onErrorAccrue(exception: NovixAppException): () -> Unit = {
        updateState {
            copy(
                isLoading = false,
                snackBarData = SnackData(
                    message = stringProvider.addToListFailed,
                    isError = true
                )
            )
        }
        Log.d("SaveToListViewModel", "onErrorAccrue: with exception :$exception")
        emitEffect(SaveToListEffect.FailedToAdd)
    }
}