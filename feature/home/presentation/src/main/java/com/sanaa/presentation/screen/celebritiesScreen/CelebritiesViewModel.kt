package com.sanaa.presentation.screen.celebritiesScreen

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.base.BasePagingSourceForHome
import com.sanaa.presentation.state.PersonUiState
import com.sanaa.presentation.state.toUiState
import entity.Actor
import exceptions.NoNetworkException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import usecase.ManageActorUseCase

class CelebritiesViewModel(
    private val getActorsUseCase: ManageActorUseCase
) : BaseViewModel<CelebritiesScreenUiState, CelebritiesScreenEffects>(
    CelebritiesScreenUiState()
), CelebritiesScreenInteractionListener {

    init {
        loadActors()
    }

    override fun onBackClick() {
        emitEffect(CelebritiesScreenEffects.NavigateBack)
    }

    override fun onActorClick(actorId: Int) {
        emitEffect(CelebritiesScreenEffects.NavigateToActorDetails(actorId))
    }


    private fun loadActors() {
        tryToCollect(
            callee = { loadActorsOperation() },
            onCollect = ::onActorsLoaded,
            onError = ::onDataLoadError
        )
    }

    private fun loadActorsOperation(): Flow<PagingData<PersonUiState>> {
        return createPagingFlow(
            pagingSourceFactory = { createActorsPagingSource() },
            mapper = Actor::toUiState
        )
    }

    private fun onActorsLoaded(pagingData: PagingData<PersonUiState>) {
        updateState { it.copy(celebrities = flowOf(pagingData)) }
    }

    private fun onDataLoadError(e: Throwable) {
        if (e is NoNetworkException) updateState {
            it.copy(
                isLoading = false,
                isNoInternetConnection = true
            )
        }
        else updateState {
            it.copy(
                isLoading = false,
                isNoInternetConnection = false
            )
        }
    }

    private fun createActorsPagingSource(): PagingSource<Int, Actor> {
        return BasePagingSourceForHome { page ->
            getActorsUseCase.getTrendingActors(page = page)
        }
    }
}