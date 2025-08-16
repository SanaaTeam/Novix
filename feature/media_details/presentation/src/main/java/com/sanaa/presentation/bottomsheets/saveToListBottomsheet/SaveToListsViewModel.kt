package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.screen.movieDetails.SnackData
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
class SaveToListsViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<SaveToListsUiState, SaveToListEffects>(SaveToListsUiState(), dispatcher),
    SaveToListsInteractionListener {

    init {
        loadPlaylists()
    }

    private fun loadPlaylists() {
        updateState { copy(isLoading = true) }
        tryToCollect(
            callee = { manageSavedListsUseCase.getSavedLists() },
            onCollect = ::onLoadPlaylistsSuccess,
            onError = ::onLoadPlaylistsError
        )
    }

    private fun onLoadPlaylistsSuccess(domainLists: List<SavedList>) {
        val uiLists = domainLists.map { savedList ->
            PlaylistUiItems(
                id = savedList.id.toLong(),
                title = savedList.title,
                itemCount = savedList.itemCount
            )
        }
        updateState { copy(isLoading = false, playlists = uiLists) }
    }

    private fun onLoadPlaylistsError(throwable: Throwable) {
        updateState {
            copy(
                isLoading = false,
                snackBarData = SnackData(message = stringProvider.somethingWentWrongError, isError = true)
            )
        }
    }

    override fun onPlaylistSelected(listId: Long) {
        updateState {
            copy(selectedListId = listId, isAddButtonEnabled = true)
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
            callee = { addMovieToSavedList(selectedListId, mediaId) },
            onSuccess = ::onAddMovieToSavedListSuccess,
            onError = ::onAddMovieError
        )
    }

    private suspend fun addMovieToSavedList(selectedListId: Long, mediaId: Long) {
        manageSavedListItemsUseCase.addMovieToSavedList(
            listId = selectedListId.toInt(),
            movieId = mediaId.toInt()
        )
    }

    private fun onAddMovieToSavedListSuccess(unit: Unit) {
        updateState {
            copy(
                isLoading = false,
                snackBarData = SnackData(message = stringProvider.addToListSuccess, isError = false)
            )
        }
        emitEffect(SaveToListEffects.Dismiss)
    }

    private fun onAddMovieError(exception: NovixAppException) {
        updateState {
            copy(
                isLoading = false,
                snackBarData = SnackData(message = stringProvider.addToListFailed, isError = true)
            )
        }
    }
}