package com.sanaa.presentation.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import repository.ContentRestriction
import repository.Theme
import usecase.MangeUserPreferenceUseCase
import javax.inject.Inject

@HiltViewModel
class NovixAppViewModel @Inject constructor(
    val mangeUserPreference: MangeUserPreferenceUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(NovixAppUiState())
    val state = _state.asStateFlow()


    init {
        fetchUserPreference()
    }

    private fun fetchUserPreference() {
        viewModelScope.launch {
            launch {
                mangeUserPreference.getTheme().collect { theme ->
                    updateState { copy(isDarkTheme = Theme.DARK == theme) }
                }
            }
            launch {
                mangeUserPreference.getContentRestriction().collect { restriction ->
                    val threshold = when (restriction) {
                        ContentRestriction.RESTRICTED -> STRICT_CONTENT_THRESHOLD
                        ContentRestriction.MODERATE_RESTRICTION -> MODERATE_CONTENT_THRESHOLD
                        ContentRestriction.UNRESTRICTED -> UNRESTRICTED_CONTENT_THRESHOLD
                    }
                    updateState { copy(safeContentThreshold = threshold) }
                }
            }

        }
    }

    private fun updateState(block: NovixAppUiState.() -> NovixAppUiState) {
        _state.value = block(_state.value)
    }

    private companion object {
        const val STRICT_CONTENT_THRESHOLD = 0.9f
        const val MODERATE_CONTENT_THRESHOLD = 0.5f
        const val UNRESTRICTED_CONTENT_THRESHOLD = 0.0f
    }
}