package com.sanaa.presentation.screen.celebritiesScreen

import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.state.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import usecase.ManageActorUseCase
import javax.inject.Inject

@HiltViewModel
class CelebritiesViewModel @Inject constructor(
    private val getActorsUseCase: ManageActorUseCase
) : BaseViewModel<CelebritiesScreenUiState, CelebritiesScreenEffects>(
    initialState = CelebritiesScreenUiState(),
    defaultDispatcher = Dispatchers.IO
), CelebritiesScreenInteractionListener {

    init {
        fetchActors()
    }

    override fun onBackClick() {
        emitEffect(CelebritiesScreenEffects.NavigateBack)
    }

    override fun onActorClick(actorId: Int) {
        emitEffect(CelebritiesScreenEffects.NavigateToActorDetails(actorId))
    }

    private fun fetchActors() {
        tryToExecute(callee = {
            updateState { it.copy(isLoading = true) }
            getActorsUseCase.getTrendingActors(1)
        }, onSuccess = { actors ->
            updateState {
                it.copy(
                    celebrities = actors.map { it.toUiState() }, isLoading = false
                )
            }
        }
        )
    }
}