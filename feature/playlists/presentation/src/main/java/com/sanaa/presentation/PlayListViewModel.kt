package com.sanaa.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import repository.Theme
import usecase.MangeUserPreferenceUseCase
import javax.inject.Inject

@HiltViewModel
class PlayListViewModel @Inject constructor(
    private val mangeUserPreference: MangeUserPreferenceUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(PlayListUiState())
    val state = _state.asStateFlow()

    init {
        fetchTheme()
    }

    private fun fetchTheme() {
        viewModelScope.launch {
            mangeUserPreference.getTheme().collect { theme ->
                updateState { PlayListUiState(isDarkMode = theme == Theme.DARK) }
            }
        }
    }

    private fun updateState(block: PlayListUiState.() -> PlayListUiState) {
        _state.value = block(_state.value)
    }
}