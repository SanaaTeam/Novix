package com.sanaa.tvapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import repository.ContentRestriction
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
                mangeUserPreference.getContentRestriction().collect { contentRestriction ->
                    val threshold = when (contentRestriction) {
                        ContentRestriction.RESTRICTED -> STRICT_CONTENT_THRESHOLD
                        ContentRestriction.MODERATE_RESTRICTION -> MODERATE_CONTENT_THRESHOLD
                        ContentRestriction.UNRESTRICTED -> UNRESTRICTED_CONTENT_THRESHOLD
                    }
                    updateState { it.copy(safeContentThreshold = threshold) }
                }
            }
        }
        updateState { it.copy(isReady = true) }
    }

    private fun updateState(block: (MainUiState) -> MainUiState) {
        _state.value = block(_state.value)
    }

    private companion object {
        const val STRICT_CONTENT_THRESHOLD = 0.9f
        const val MODERATE_CONTENT_THRESHOLD = 0.5f
        const val UNRESTRICTED_CONTENT_THRESHOLD = 0.0f
    }
}

data class MainUiState(
    val isDarkTheme: Boolean = true,
    val safeContentThreshold: Float = 0f,
    val isReady: Boolean = false,
)