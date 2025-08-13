package com.sanaa.presentation.screen.genreTvShows

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.details_base.BasePagingSource
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.SeriesUiModel
import com.sanaa.presentation.model.mapper.toSeriesUiModel
import com.sanaa.presentation.navigation.GenreTvShowsScreenRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.TvSeries
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageTvSeriesUseCase
import javax.inject.Inject

@HiltViewModel
class GenreTvShowsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<GenreTvShowsScreenUiState, GenreTvShowsEffects>(
    initialState = GenreTvShowsScreenUiState(),
    defaultDispatcher = dispatcher
), GenreTvShowsScreenInteractionListener {
    val route: GenreTvShowsScreenRoute = savedStateHandle.toRoute()

    init {
        updateUserLoggingStatus()
        getTvShowsByGenreId(genreId)
    }

    fun updateUserLoggingStatus() {
        tryToCollect(
            callee = { checkIfUserIsLoggedInUseCase.isLoggedIn() },
            onCollect = ::onCollectLoggedFlag,
        )
    }

    private fun onCollectLoggedFlag(isLogged: Boolean) {
        updateState { copy(userIsLoggedIn = isLogged) }
    }

    override fun onSaveIconClick() {
        if (!state.value.userIsLoggedIn) {
            updateState {
                copy(
                    showBottomSheet = true
                )
            }
        }
    }

    override fun onRetryClick() {
        updateState {
            copy(
                noInternetConnection = false,
                isLoading = true,
                error = null
            )
        }
        getTvShowsByGenreId(genreId)
    }

    override fun onBottomSheetDismiss() {
        updateState { copy(showBottomSheet = false) }
    }

    override fun onLoginButtonClick() {
        updateState { copy(showBottomSheet = false) }
        emitEffect(GenreTvShowsEffects.NavigateToLogin)
    }

    override fun onBackClick() {
        emitEffect(GenreTvShowsEffects.NavigateBack)
    }

    override fun onTvShowClick(id: Int) {
        emitEffect(GenreTvShowsEffects.NavigateToTvShowDetails(id))
    }

    private fun getTvShowsByGenreId(genreId: Int) {
        tryToCollect(
            callee = { loadTvShowsByGenreId(genreId) },
            onCollect = ::onCollectTvShowsByGenreId,
            onError = ::onGetShowsByGeneraIdFailed
        )
    }

    private fun onCollectTvShowsByGenreId(tvShows: PagingData<SeriesUiModel>) {
        updateState {
            copy(
                title = genreName,
                tvShows = flowOf(tvShows),
                isLoading = false
            )
        }
    }

    private fun onGetShowsByGeneraIdFailed(throwable: Throwable) {
        if (throwable is NoNetworkException) {
            updateState { copy(noInternetConnection = true, isLoading = false, error = null) }
        } else {
            updateState { copy(error = throwable.message, isLoading = false) }
        }
    }

    private fun loadTvShowsByGenreId(genreId: Int) = createPagingFlow(
        pagingSourceFactory = { createTvShowsPagingDataSource(genreId) },
        mapper = TvSeries::toSeriesUiModel
    )

    private fun createTvShowsPagingDataSource(
        genreId: Int,
    ): PagingSource<Int, TvSeries> {
        return BasePagingSource { page ->
            manageTvSeriesUseCase.getTvSeriesByGenre(
                genreId = genreId, page = page
            )
        }
    }
}