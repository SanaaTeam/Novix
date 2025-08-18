package com.sanaa.presentation.bottomsheet.saveToListBottomsheet

import android.util.Log
import com.sanaa.presentation.components.SnackData
import com.sanaa.presentation.homeBase.BaseViewModel
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
) : BaseViewModel<SaveToListUiState, SaveToListEffect>(SaveToListUiState(), dispatcher)
,SaveToListInteractionListener{


    fun getMediaId(mediaId: Int) {
        updateState { copy(mediaId = mediaId) }
        observePlaylists()
    }

    private fun observePlaylists() {
        updateState { copy(isLoading = true) }
        tryToCollect(
            block = {
                mangeSavedListsUseCase.getSavedLists()
                    },
            onCollect = ::onCollectPlaylists,
        )
    }

    private fun onCollectPlaylists(playlist: List<SavedList>) {
        val tempPlayList = playlist.toState()
        tempPlayList.forEach { playlistUiItem: PlaylistUiItem ->
            playlistUiItem.containsMediaItem = playlistUiItem.itemsIds.contains(state.value.mediaId)
        }
        updateState { copy(playlists = tempPlayList, isLoading = false) }
    }


    private fun removeUnSelectedList(selectedListsIds: List<Int>, listId: Int) {
        val updated = selectedListsIds.toMutableList().apply { remove(listId) }
        updateState {
            copy(
                selectedListsIds = updated,
                isAddButtonEnabled = updated.isNotEmpty()
            )
        }
    }

    private fun addSelectedList(selectedListsIds: List<Int>, listId: Int) {
        val updated = selectedListsIds.toMutableList().apply { add(listId) }
        updateState {
            copy(
                selectedListsIds = updated,
                isAddButtonEnabled = updated.isNotEmpty()
            )
        }

    }

    private suspend fun addMovieToSavedList(
        selectedListId: Int,
        mediaId: Int
    ){
        manageSavedListItemsUseCase.addMovieToSavedList(
            listId = selectedListId,
            movieId = mediaId
        )
    }

    private fun onErrorAccrue(exception: NovixAppException): () -> Unit = {
        updateState {
            copy(
                isLoading = false,
                isUploading = false,
                snackBarData = SnackData(
                    message = stringProvider.addToListFailed,
                    isError = true
                ),
                selectedListsIds = mutableListOf(),
                mediaId = null
            )
        }
        emitEffect(SaveToListEffect.DismissBottomSheet)
    }

    override fun onPlaylistClick(listId: Int) {
        if (state.value.isLoading || state.value.isUploading) return
        val targetPlaylist = state.value.playlists.find { it.id == listId }
        if (targetPlaylist?.containsMediaItem == true) return

        val selectedListsIds = state.value.selectedListsIds
        if (listId in selectedListsIds) {
            removeUnSelectedList(selectedListsIds, listId)
        } else {
            addSelectedList(selectedListsIds, listId)
        }
    }

    override fun onAddClick() {
        val selectedListsIds: MutableList<Int> = state.value.selectedListsIds
        if (selectedListsIds.isEmpty()) return
        updateState { copy(isUploading = true, isAddButtonEnabled = false) }
        tryToExecute(
            block = {
                selectedListsIds.forEach { listId ->
                    addMovieToSavedList(listId, state.value.mediaId!!)
                }
                updateState {
                    copy(
                        isUploading = false,
                        isLoading = false,
                        isAddButtonEnabled = false,
                        snackBarData = SnackData(
                            message = stringProvider.addToListSuccess,
                            isError = false,
                        ),
                        selectedListsIds = mutableListOf(),
                        mediaId = null
                    )
                }
                emitEffect(SaveToListEffect.DismissBottomSheet)
            },
            onError = ::onErrorAccrue
        )
    }

    override fun onSnackBarDismiss() {
        updateState {
            copy(snackBarData = null)
        }
    }



    override fun onCreateNewListClick() {
        emitEffect(SaveToListEffect.CreateNewList)
    }

    override fun onRequestBottomSheetDismiss() {
        updateState {
            copy(
                mediaId = null,
                selectedListsIds = mutableListOf(),
                isUploading = false,
                isLoading = false,
                isAddButtonEnabled = false,
            )
        }
    }
}