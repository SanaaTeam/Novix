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
class AddBookmarkListViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddBookmarkListUiState())
    val uiState = _uiState.asStateFlow()

    fun onListTitleChanged(title: String) {
        _uiState.update {
            it.copy(
                listTitle = title,
                isAddButtonEnabled = title.isNotBlank()
            )
        }
    }

    fun resetState() {
        _uiState.update { it.copy(listTitle = "", isLoading = false) }
    }

    fun onAddClicked(onSuccess: () -> Unit) {
        if (!_uiState.value.isAddButtonEnabled) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val currentTitle = _uiState.value.listTitle
            println("Creating new playlist with title: $currentTitle")
            resetState()
            onSuccess()
        }
    }
}