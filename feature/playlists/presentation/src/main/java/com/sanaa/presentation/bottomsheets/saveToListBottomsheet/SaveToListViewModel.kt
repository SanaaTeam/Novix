package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveToListViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(SaveToListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPlaylists()
    }

    private fun loadPlaylists() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val fakePlaylists = listOf(
                PlaylistUiItem(id = 1, title = "My favorite", itemCount = 12),
                PlaylistUiItem(id = 2, title = "My movies", itemCount = 5),
                PlaylistUiItem(id = 3, title = "Watch Later", itemCount = 23)
            )
            _uiState.update { it.copy(isLoading = false, playlists = fakePlaylists) }
        }
    }

    fun onPlaylistSelected(listId: Long) {
        _uiState.update {
            it.copy(
                selectedListId = listId, isAddButtonEnabled = true
            )
        }
    }

    fun onAddClicked(mediaId: Long, onSuccess: () -> Unit) {
        val selectedListId = _uiState.value.selectedListId ?: return
        if (!_uiState.value.isAddButtonEnabled) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            println("Adding media $mediaId to playlist $selectedListId")

            _uiState.update { it.copy(isLoading = false) }
            onSuccess()
        }
    }
}