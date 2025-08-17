package com.sanaa.presentation.bottomsheets.removeFromListBottomSheet

import com.sanaa.presentation.savedBase.BaseViewModel
import com.sanaa.presentation.screen.playlist.SnackData
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NoNetworkException
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import service.VodStringProvider
import usecase.custom_list.ManageSavedListItemsUseCase
import usecase.custom_list.ManageSavedListsUseCase
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject

@HiltViewModel
class RemoveFromListViewModel @Inject constructor(
    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase,
    private val mangeSavedListsUseCase: ManageSavedListsUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<RemoveFromListUiState, RemoveFromListEffect>(RemoveFromListUiState(), dispatcher)
,RemoveFromListInteractionListener{

    init{
        observePlaylists()
    }

    fun getMediaId(mediaId: Int) {
        updateState { copy(mediaId = mediaId) }
        observePlaylists()
    }

    private fun observePlaylists() {
        tryToCollect(
            onStart = { updateState { copy(isLoading = true) } },
            block = { mangeSavedListsUseCase.getSavedLists() },
            onCollect = ::onCollectPlaylists,
            onError = ::onErrorAccrue
        )
    }

    private fun onCollectPlaylists(playlist: List<SavedList>) {
        val selectedLists = playlist.filter { it.itemsIds.contains(state.value.mediaId) }.toState()
        updateState { copy(playlists = playlist.toState(), selectedLists = selectedLists, isLoading = false) }
    }


    private fun addUnSelectedList(selectedListsIds: List<Int>, listId: Int) {
        val updated = selectedListsIds.toMutableList().apply { add(listId) }
        updateState {
            copy(
                deselectedListsIds = updated,
                isRemoveButtonEnabled = updated.isNotEmpty()
            )
        }
    }

    private fun removeUnSelectedList(selectedListsIds: List<Int>, listId: Int) {
        val updated = selectedListsIds.toMutableList().apply { remove(listId) }
        updateState {
            copy(
                deselectedListsIds = updated,
                isRemoveButtonEnabled = updated.isNotEmpty()
            )
        }
    }

    private suspend fun removeMovieFromSavedList(
        selectedListId: Int,
        mediaId: Int
    ){
        manageSavedListItemsUseCase.removeMovieFromSavedList(
            listId = selectedListId,
            movieId =mediaId
        )
    }

    private fun onErrorAccrue(exception: NovixAppException): () -> Unit = {
        updateState {
            when (exception) {
                is NoNetworkException -> copy(
                    isLoading = false,
                    isUploading = false,
                    snackBarData = SnackData(
                        message = stringProvider.noInternetConnectionError,
                        isError = true
                    ),
                )

                else -> copy(
                    isLoading = false,
                    isUploading = false,
                    snackBarData = SnackData(
                        message = stringProvider.somethingWentWrongError,
                        isError = true
                    ),
                )
            }
        }
    }

    override fun onPlaylistClick(listId: Int) {
        val deselectedListsIds = state.value.deselectedListsIds
        if (listId in deselectedListsIds) {
            removeUnSelectedList(deselectedListsIds, listId)
        } else {
            addUnSelectedList(deselectedListsIds, listId)
        }
    }

    override fun onRemoveClick() {
        val deselectedListsIds: MutableList<Int> = state.value.deselectedListsIds
        if (deselectedListsIds.isEmpty()) return
        updateState { copy(isUploading = true, isRemoveButtonEnabled = false) }
        tryToExecute(
            block = {
                deselectedListsIds.forEach { listId ->
                    removeMovieFromSavedList(listId, state.value.mediaId!!)
                }
                updateState {
                    copy(
                        isUploading = false,
                        isLoading = false,
                        isRemoveButtonEnabled = false,
                        deselectedListsIds = mutableListOf(),
                        mediaId = null
                    )
                }
                emitEffect(RemoveFromListEffect.DismissBottomSheet(deselectedListsIds))
            },
            onError = ::onErrorAccrue
        )
    }

    override fun onSnackBarDismiss() {
        updateState {
            copy(snackBarData = null)
        }
    }

    override fun onBottomSheetDismiss() {
        updateState {
            copy(
                mediaId = null,
                deselectedListsIds = mutableListOf(),
                isUploading = false,
                isLoading = false,
                isRemoveButtonEnabled = false,
            )
        }
    }
}

fun SavedList.toState(): PlaylistUiItem {
    return PlaylistUiItem(title = title, itemCount = itemCount, id = id, itemsIds = itemsIds)
}

fun List<SavedList>.toState(): List<PlaylistUiItem> {
    return map { it.toState() }
}