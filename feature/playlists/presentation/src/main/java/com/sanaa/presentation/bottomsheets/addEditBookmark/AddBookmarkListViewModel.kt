package com.sanaa.presentation.bottomsheets.addEditBookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import usecase.custom_list.ManageSavedListsUseCase
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject

@HiltViewModel
class AddBookmarkListViewModel @Inject constructor(
    private val manageSavedListsUseCase: ManageSavedListsUseCase
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
        _uiState.update { it.copy(listTitle = "", isLoading = false, errorMessage = null) }
    }

    fun onAddClicked(onSuccess: () -> Unit) {
        if (!_uiState.value.isAddButtonEnabled) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val currentTitle = _uiState.value.listTitle.trim()

            try {
                val newList = SavedList(id = 0, title = currentTitle)
                manageSavedListsUseCase.createSavedList(newList)

                resetState()
                onSuccess()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to create list. Please try again."
                    )
                }
            }
        }
    }
}