package com.sanaa.tvapp


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
class MainViewModel @Inject constructor(
    private val mangeUserPreference: MangeUserPreferenceUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(MainUiState())
    val state = _state.asStateFlow()

    init {
        fetchUserPreference()
    }

    private fun fetchUserPreference() {
        viewModelScope.launch {
            launch {
                mangeUserPreference.getTheme().collect { theme ->
                    updateState { it.copy(isDarkTheme = theme == Theme.DARK) }
                }
            }
        }
        updateState { it.copy(isReady = true) }
    }

    private fun updateState(block: (MainUiState) -> MainUiState) {
        _state.value = block(_state.value)
    }
}

data class MainUiState(
    val isDarkTheme: Boolean = true,
    val isReady: Boolean = false
)