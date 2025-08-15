package com.sanaa.presentation.screen.trendingPeopleScreen

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.base.BasePagingSourceForHome
import com.sanaa.presentation.components.SnackData
import com.sanaa.presentation.state.PersonUiState
import com.sanaa.presentation.state.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Actor
import exceptions.NoNetworkException
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import service.VodStringProvider
import usecase.ManageActorUseCase
import javax.inject.Inject

@HiltViewModel
class TrendingPeopleScreenViewModel @Inject constructor(
    private val getActorsUseCase: ManageActorUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<TrendingPeopleScreenUiState, TrendingPeopleScreenEffect>(
    initialState = TrendingPeopleScreenUiState(),
    defaultDispatcher = dispatcher
), TrendingPeopleScreenInteractionListener {

    init {
        loadActors()
    }

    private fun loadActors() {
        tryToCollect(
            block = ::loadActorsOperation,
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
        updateState {
            copy(
                people = flowOf(pagingData),
                isNoInternetConnection = false,
            )
        }
    }

    override fun onBackClick() {
        emitEffect(TrendingPeopleScreenEffect.NavigateBack)
    }

    override fun onActorClick(actorId: Int) {
        emitEffect(TrendingPeopleScreenEffect.NavigateToActorDetails(actorId))
    }

    override fun onRetryClick() {
        loadActors()
    }

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
    }

    private fun onDataLoadError(e: NovixAppException) {
        when (e) {
            is NoNetworkException -> {
                updateState {
                    copy(
                        isNoInternetConnection = true,
                        snackBarData =
                            SnackData(
                                message = stringProvider.noInternetConnectionError,
                                isError = true
                            )
                    )
                }
            }

            else -> {
                updateState {
                    copy(
                        isNoInternetConnection = false,
                        snackBarData =
                            SnackData(
                                message = stringProvider.somethingWentWrongError,
                                isError = true
                            )
                    )
                }
            }
        }
    }

    private fun createActorsPagingSource(onError: ((NovixAppException) -> Unit)? = ::onDataLoadError)
            : PagingSource<Int, Actor> {
        return BasePagingSourceForHome(onError = onError) { page ->
            getActorsUseCase.getTrendingActors(page = page)
        }
    }
}