package com.sanaa.presentation.screen.trendingPeopleScreen

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.base.BasePagingSourceForHome
import com.sanaa.presentation.state.PersonUiState
import com.sanaa.presentation.state.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Actor
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import usecase.ManageActorUseCase
import javax.inject.Inject

@HiltViewModel
class TrendingPeopleViewModel @Inject constructor(
    private val getActorsUseCase: ManageActorUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<TrendingPeopleScreenUiState, TrendingPeopleScreenEffects>(
    initialState = TrendingPeopleScreenUiState(),
    defaultDispatcher = dispatcher
), TrendingPeopleScreenInteractionListener {

    init {
        loadActors()
    }

    private fun loadActors() {
        tryToCollect(
            callee = ::loadActorsOperation,
            onCollect = ::onLoadActorsSuccess,
            onError = ::onDataLoadError
        )
    }

    private fun loadActorsOperation(): Flow<PagingData<PersonUiState>> {
        return createPagingFlow(
            pagingSourceFactory = ::createActorsPagingSource,
            mapper = Actor::toState
        )
    }

    private fun onLoadActorsSuccess(pagingData: PagingData<PersonUiState>) {
        updateState { it.copy(people = flowOf(pagingData)) }
    }

    private fun onDataLoadError(e: Throwable) {
        if (e is NoNetworkException) updateState {
            it.copy(
                isLoading = false,
                isNoInternetConnection = true,
                error = null
            )
        }
        else updateState {
            it.copy(
                isLoading = false,
                isNoInternetConnection = false,
                error = e.message
            )
        }
    }

    override fun onBackClick() {
        emitEffect(TrendingPeopleScreenEffects.NavigateBack)
    }

    override fun onActorClick(actorId: Int) {
        emitEffect(TrendingPeopleScreenEffects.NavigateToActorDetails(actorId))
    }

    override fun onRetryClick() {
        loadActors()
    }

    private fun createActorsPagingSource(): PagingSource<Int, Actor> {
        return BasePagingSourceForHome { page ->
            getActorsUseCase.getTrendingActors(page = page)
        }
    }
}