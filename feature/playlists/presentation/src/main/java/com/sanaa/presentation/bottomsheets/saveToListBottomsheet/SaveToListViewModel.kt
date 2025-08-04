package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import usecase.custom_list.ManageSavedListItemsUseCase
import usecase.custom_list.ManageSavedListsUseCase
import javax.inject.Inject

@HiltViewModel
class SaveToListViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SaveToListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPlaylists()
    }

    private fun loadPlaylists() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val playlists = manageSavedListsUseCase.getSavedLists().map { savedList ->
                    PlaylistUiItem(
                        id = savedList.id.toLong(),
                        title = savedList.title,
                        itemCount = 0
                    )
                }
                _uiState.update { it.copy(isLoading = false, playlists = playlists) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to load lists."
                    )
                }
            }
        }
    }

    fun onPlaylistSelected(listId: Long) {
        _uiState.update {
            it.copy(
                selectedListId = listId, isAddButtonEnabled = true
            )
        }
    }

    fun onAddClicked(mediaId: Long, mediaType: MediaType, onSuccess: () -> Unit) {
        val selectedListId = _uiState.value.selectedListId ?: return
        if (!_uiState.value.isAddButtonEnabled) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                when (mediaType) {
                    MediaType.MOVIE -> manageSavedListItemsUseCase.addMovieToSavedList(
                        listId = selectedListId.toInt(),
                        movieId = mediaId.toInt()
                    )
                    MediaType.TV -> manageSavedListItemsUseCase.addTvSeriesToSavedList(
                        listId = selectedListId.toInt(),
                        tvSeriesId = mediaId.toInt()
                    )
                }
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to add item to list."
                    )
                }
            }
        }
    }
}