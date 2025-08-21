package com.sanaa.presentation.app

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import repository.ContentRestriction
import repository.Theme
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.MangeUserPreferenceUseCase
import usecase.custom_list.ManageSavedListsUseCase
import javax.inject.Inject

@HiltViewModel
class NovixAppViewModel @Inject constructor(
    private val mangeUserPreference: MangeUserPreferenceUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val mangeSavedListsUseCase: ManageSavedListsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NovixAppUiState())
    val state = _state.asStateFlow()

    init {
        refreshListData()
        fetchUserPreference()
    }

    private fun refreshListData() {
        viewModelScope.launch (Dispatchers.IO){
            try {
                checkIfUserIsLoggedInUseCase.isLoggedIn().collectLatest{logged->
                    if (logged)mangeSavedListsUseCase.refreshSavedList()
                }
            }catch (e:Exception){
                Log.d("NovixAppViewModel", "refreshListData: no internet keeping on local data e:$e")
            }
        }
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