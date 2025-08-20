package com.sanaa.presentation.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import repository.Theme
import repository.UserPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val userPreference: UserPreferencesRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AuthenticationUiState())
    val state = _state.asStateFlow()

    init {
        fetchUserPreference()
    }

    private fun fetchUserPreference() {
        viewModelScope.launch {
            launch {
                userPreference.getTheme().collect { theme ->
                    updateState { copy(isDarkTheme = theme == Theme.DARK) }
                }
            }
        }
    }

    private fun updateState(block: AuthenticationUiState.() -> AuthenticationUiState) {
        _state.value = block(_state.value)
    }
}

data class AuthenticationUiState(
    val isDarkTheme: Boolean = false,
)