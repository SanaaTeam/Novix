package com.sanaa.presentation.screen.celebritiesScreen

import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.state.toUiState
import usecase.ManageActorUseCase

class CelebritiesViewModel(
    private val getActorsUseCase: ManageActorUseCase
) : BaseViewModel<CelebritiesScreenUiState, CelebritiesScreenEffects>(
    CelebritiesScreenUiState()
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
    override fun onRetryClick() {
        updateState { it.copy(isNoInternetConnection = false, isLoading = true) }
    }
    override fun onLoading() {
        updateState { it.copy(isLoading = true, isNoInternetConnection = false) }
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