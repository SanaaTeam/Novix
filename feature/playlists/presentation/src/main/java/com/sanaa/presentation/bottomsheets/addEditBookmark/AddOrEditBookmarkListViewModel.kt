package com.sanaa.presentation.bottomsheets.addEditBookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddOrEditBookmarkListViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditBookmarkListUiState())
    val uiState = _uiState.asStateFlow()

    private var editingListId: Long? = null

    fun setEditMode(listId: Long, currentTitle: String) {
        editingListId = listId
        _uiState.update {
            it.copy(
                isEditMode = true,
                listTitle = currentTitle,
                isSaveButtonEnabled = currentTitle.isNotBlank()
            )
        }
    }

    fun resetToDefault() {
        editingListId = null
        _uiState.update { AddEditBookmarkListUiState() }
    }

    fun onListTitleChanged(title: String) {
        _uiState.update {
            it.copy(
                listTitle = title,
                isSaveButtonEnabled = title.isNotBlank()
            )
        }
    }


    fun onSaveOrAddClicked(onSuccess: () -> Unit) {
        if (!_uiState.value.isSaveButtonEnabled) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val currentTitle = _uiState.value.listTitle

            if (_uiState.value.isEditMode) {
                // --- EDIT LOGIC ---
                println("Editing playlist ID: $editingListId with new title: $currentTitle")
            } else {
                // --- ADD LOGIC ---
                println("Creating new playlist with title: $currentTitle")
            }

            // On success from use case
            resetToDefault()
            onSuccess()
        }
    }
}