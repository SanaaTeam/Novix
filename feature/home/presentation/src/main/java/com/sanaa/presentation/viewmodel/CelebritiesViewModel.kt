package com.sanaa.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.screen.CelebritiesScreenEffects
import com.sanaa.presentation.screen.CelebritiesScreenInteractionListener
import com.sanaa.presentation.ui_state.CelebritiesScreenUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class CelebritiesViewModel : ViewModel(), CelebritiesScreenInteractionListener {
    private val _state = MutableStateFlow(
        CelebritiesScreenUiState(isLoading = false, people = emptyList())
    )
    val state: StateFlow<CelebritiesScreenUiState> = _state

    private val _effect = MutableSharedFlow<CelebritiesScreenEffects>()
    val effect: SharedFlow<CelebritiesScreenEffects> = _effect

    override fun onBackClick() {
        emitEffect(CelebritiesScreenEffects.NavigateBack)
    }

    override fun onActorClick(actorId: Int) {
        emitEffect(CelebritiesScreenEffects.NavigateToActorDetails(actorId))
    }

    private fun emitEffect(effect: CelebritiesScreenEffects) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
