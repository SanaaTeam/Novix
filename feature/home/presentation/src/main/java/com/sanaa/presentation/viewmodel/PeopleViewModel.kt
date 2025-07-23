package com.sanaa.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.ui_state.PeopleScreenEffects
import com.sanaa.presentation.ui_state.PeopleScreenInteractionListener
import com.sanaa.presentation.ui_state.PeopleScreenUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PeopleViewModel : ViewModel(), PeopleScreenInteractionListener {
    private val _state = MutableStateFlow(
        PeopleScreenUiState(isLoading = false, people = emptyList())
    )
    val state: StateFlow<PeopleScreenUiState> = _state

    private val _effect = MutableSharedFlow<PeopleScreenEffects>()
    val effect: SharedFlow<PeopleScreenEffects> = _effect

    override fun onBackClick() {
        viewModelScope.launch { _effect.emit(PeopleScreenEffects.NavigateBack) }
    }

    override fun onActorClick(actorId: Int) {
        viewModelScope.launch { _effect.emit(PeopleScreenEffects.NavigateToActorDetails(actorId)) }
    }
}
