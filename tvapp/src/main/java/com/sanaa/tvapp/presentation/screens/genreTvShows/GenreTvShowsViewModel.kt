package com.sanaa.tvapp.presentation.screens.genreTvShows

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.tvapp.base.BasePagingSource
import com.sanaa.tvapp.base.BaseViewModel
import com.sanaa.tvapp.presentation.screens.searchScreen.TvShowUiModel
import com.sanaa.tvapp.presentation.screens.searchScreen.mapper.toUiState
import com.sanaa.tvapp.state.SnackData
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.TvShow
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import service.VodStringProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageTvShowUseCase
import javax.inject.Inject

@HiltViewModel
class GenreTvShowsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageTvShowUseCase: ManageTvShowUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<GenreTvShowsScreenUiState, GenreTvShowsEffects>(
    initialState = GenreTvShowsScreenUiState(),
    defaultDispatcher = dispatcher
), GenreTvShowsScreenInteractionListener {

    private val genreId: Int = checkNotNull(savedStateHandle["genreId"])
    private val genreName: String = checkNotNull(savedStateHandle["genreName"])

    init {
        updateUserLoggingStatus()
        getTvShowsByGenreId(genreId)
    }

    fun updateUserLoggingStatus() {
        tryToCollect(
            block = { checkIfUserIsLoggedInUseCase.isLoggedIn() },
            onCollect = ::onCollectLoggedFlag,
        )
    }

    private fun onCollectLoggedFlag(isLogged: Boolean) {
        updateState { copy(userIsLoggedIn = isLogged) }
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

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
    }

    override fun onTvShowClick(id: Int) {
        emitEffect(GenreTvShowsEffects.NavigateToTvShowDetails(id))
    }

    private fun getTvShowsByGenreId(genreId: Int) {
        tryToCollect(
            onStart = {
                updateState { copy(isLoading = true) }
            },
            block = { loadTvShowsByGenreId(genreId) },
            onCollect = ::onCollectTvShowsByGenreId,
            onError = ::onErrorLoading
        )
    }

    private fun onCollectTvShowsByGenreId(tvShows: PagingData<TvShowUiModel>) {
        updateState {
            copy(
                title = genreName,
                categoryId = genreId,
                tvShows = flowOf(tvShows),
                isLoading = false
            )
        }
    }

    private fun loadTvShowsByGenreId(genreId: Int) = createPagingFlow(
        pagingSourceFactory = { createTvShowsPagingDataSource(genreId) },
        mapper = TvShow::toUiState
    )

    private fun createTvShowsPagingDataSource(
        genreId: Int,
    ): PagingSource<Int, TvShow> {
        return BasePagingSource { page ->
            manageTvShowUseCase.getTvShowsByGenre(
                genreId = genreId, page = page
            )
        }
    }
    private fun onErrorLoading(error: Throwable) {
        when (error) {
            is NoNetworkException -> {
                updateState { copy(
                    isLoading = false,
                    noInternetConnection = true,
                    snackBarData = SnackData(
                        message = stringProvider.noInternetConnectionError,
                        isError = true
                    )
                ) }
            }
            else->{
                updateState { copy(
                    isLoading = false,
                    noInternetConnection = true,
                    snackBarData = SnackData(
                        message = stringProvider.somethingWentWrongError,
                        isError = true
                    )
                ) }
            }
        }
    }

}