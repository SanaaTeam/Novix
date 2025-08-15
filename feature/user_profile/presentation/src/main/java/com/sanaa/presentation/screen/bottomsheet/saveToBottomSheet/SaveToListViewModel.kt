package com.sanaa.presentation.screen.bottomsheet.saveToBottomSheet

import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.profileBase.BaseViewModel
import com.sanaa.presentation.screen.bottomsheet.components.SnackData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import repository.SavedListsStatusProvider
import service.VodStringProvider
import usecase.custom_list.ManageSavedListItemsUseCase
import javax.inject.Inject

@HiltViewModel
class SaveToListViewModel @Inject constructor(
    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase,
    private val listsStatusProvider: SavedListsStatusProvider,
    private val stringProvider: VodStringProvider,
) : BaseViewModel<SaveToListUiState, SaveToListEffect>(SaveToListUiState()),
    SaveToListInteractionsListener {

    init {
        observePlaylists()
    }

    private fun observePlaylists() {
        tryToCollect(
            block = { listsStatusProvider.savedLists },
            onCollect = { playlist ->
                updateState {
                    copy(
                        playlists = playlist.map {
                            PlaylistUiItem(
                                title = it.title,
                                itemCount = it.itemCount,
                                id = it.id.toLong()
                            )
                        }
                    )
                }
            },
        )
    }

    override fun onPlaylistSelected(listId: Long) {
        updateState {
            copy(
                selectedListId = listId,
                isAddButtonEnabled = true
            )
        }
    }

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
    }

    override fun onAddClicked(mediaId: Long) {
        val selectedListId = state.value.selectedListId ?: return
        if (!state.value.isAddButtonEnabled) return

        updateState { copy(isLoading = true) }

        tryToExecute(
            block = {
                manageSavedListItemsUseCase.addMovieToSavedList(
                    listId = selectedListId.toInt(),
                    movieId = mediaId.toInt()
                )
            },
            onSuccess = {
                updateState {
                    copy(
                        isLoading = false,
                        snackBarData = SnackData(message = stringProvider.addToListSuccess, isError = false)
                    )
                }
                listsStatusProvider.markItemSaved(mediaId.toInt())
                viewModelScope.launch {
                    listsStatusProvider.refreshLists()
                }
                emitEffect(SaveToListEffect.Dismiss)
            },
            onError = {
                updateState {
                    copy(
                        isLoading = false,
                        snackBarData = SnackData(message = stringProvider.addToListFailed, isError = true)
                    )
                }
            }
        )
    }
}