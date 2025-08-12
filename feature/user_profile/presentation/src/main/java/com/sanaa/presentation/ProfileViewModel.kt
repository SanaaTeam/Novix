package com.sanaa.presentation

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
class ProfileViewModel @Inject constructor(
    private val mangeUserPreference: MangeUserPreferenceUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MyProfileUiState())
    val state = _state.asStateFlow()

    init {
        fetchUserPreference()
    }

    private fun fetchUserPreference() {
        viewModelScope.launch {
            fetchTheme()
            fetchContentRestriction()
        }
        updateState { it.copy(isReady = true) }
    }

    private fun fetchTheme() {
        viewModelScope.launch {
            mangeUserPreference.getTheme().collect { theme ->
                updateState { it.copy(isDarkTheme = theme == Theme.DARK) }
            }
        }
    }

    private fun fetchContentRestriction() {
        viewModelScope.launch {
            mangeUserPreference.getContentRestriction().collect {
                val threshold = when (it) {
                    ContentRestriction.RESTRICTED -> STRICT_CONTENT_THRESHOLD
                    ContentRestriction.MODERATE_RESTRICTION -> MODERATE_CONTENT_THRESHOLD
                    ContentRestriction.UNRESTRICTED -> UNRESTRICTED_CONTENT_THRESHOLD
                }
                updateState { it.copy(safeContentThreshold = threshold) }
            }
        }
    }


    private fun updateState(block: MyProfileUiState.() -> MyProfileUiState) {
        _state.value = block(_state.value)
    }

    private companion object {
        const val STRICT_CONTENT_THRESHOLD = 0.9f
        const val MODERATE_CONTENT_THRESHOLD = 0.5f
        const val UNRESTRICTED_CONTENT_THRESHOLD = 0.0f
    }
}
