package com.sanaa.presentation.bottomsheets.deletebottomsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteListViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(DeleteListUiState())
    val uiState = _uiState.asStateFlow()

    fun onDeleteConfirmed(listId: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            println("Deleting playlist with ID: $listId")

            _uiState.update { it.copy(isLoading = false) }
            onSuccess()
        }
    }
}