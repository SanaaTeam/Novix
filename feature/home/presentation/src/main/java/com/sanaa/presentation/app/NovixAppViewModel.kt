package com.sanaa.presentation.app

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
                    updateState { it.copy(isDarkTheme = Theme.DARK == theme) }
                }
            }
            launch {
                mangeUserPreference.getContentRestriction().collect { restriction ->
                    updateState {
                        it.copy(
                            contentRestriction = ContentRestrictionUiState.valueOf(
                                restriction.name
                            )
                        )
                    }
                }
            }
        }
    }

    private fun updateState(block: (NovixAppUiState) -> NovixAppUiState) {
        _state.value = block(_state.value)
    }
}