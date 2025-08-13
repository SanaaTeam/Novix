package com.sanaa.presentation.screen.trendingMediaScreen.trendingTvShowScreen

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.base.BasePagingSourceForHome
import com.sanaa.presentation.components.SnackData
import com.sanaa.presentation.screen.trendingMediaScreen.MediaListScreenInteractionListener
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenEffect
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenUiState
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItemUiState
import com.sanaa.presentation.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.TvSeries
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import service.VodStringProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageTvSeriesUseCase
import javax.inject.Inject

@HiltViewModel
class TrendingTvShowsScreenViewModel @Inject constructor(
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<TrendingMediaScreenUiState, TrendingMediaScreenEffect>(
    initialState = TrendingMediaScreenUiState(),
    defaultDispatcher = dispatcher
), MediaListScreenInteractionListener {
    init {
        updateUserLoggingStatus()
        fetchGenres()
        loadTvShows()
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

    private fun fetchGenres() {
        tryToExecute(
            onStart = { updateState { copy(isLoading = true) } },
            block = { manageTvSeriesUseCase.getSeriesGenres().map { it.toState() } },
            onSuccess = ::onLoadGenresSuccess,
            onError = ::onDataLoadError
        )
    }

    private fun onLoadGenresSuccess(genres: List<GenreUiState>) {
        updateState { copy(genreList = genres, isLoading = false, isNoInternetConnection = false) }
    }

    private fun loadTvShows() {
        tryToCollect(
            block = ::loadTvShowsOperation,
            onCollect = ::onLoadTvShowsSuccess,
            onError = ::onDataLoadError
        )
    }

    private fun loadTvShowsOperation(): Flow<PagingData<MediaItemUiState>> {
        return createPagingFlow(
            pagingSourceFactory = { createTvShowsPagingSource() },
            mapper = TvSeries::toState
        )
    }

    private fun onLoadTvShowsSuccess(pagingData: PagingData<MediaItemUiState>) {
        updateState {
            copy(
                mediaList = flowOf(pagingData),
                isLoading = false,
                isNoInternetConnection = false
            )
        }
    }

    override fun onGenreClick(id: Int?) {
        if (id == state.value.selectedGenreId) return
        updateState {
            copy(selectedGenreId = id)
        }
        loadTvShows()
    }

    override fun onMediaClick(id: Int) {
        emitEffect(TrendingMediaScreenEffect.NavigateToMediaDetails(id))
    }

    override fun onSaveIconClick(media: MediaItemUiState) {
        if (!state.value.userIsLoggedIn) {
            updateState {
                copy(
                    showLoginBottomSheet = true
                )
            }
        }
    }


    override fun onSaveToListSuccess() {
        updateState {
            copy(
                snackBarData = SnackData(
                    message = stringProvider.addToListSuccess,
                    isError = false
                )
            )
        }
    }

    override fun onSaveToListFailure() {
        updateState {
            copy(
                snackBarData = SnackData(
                    message = stringProvider.addToListFailed,
                    isError = true
                )
            )
        }
    }

    override fun onBackClick() {
        emitEffect(TrendingMediaScreenEffect.NavigateBack)
    }

    override fun onRetryClick() {
        updateUserLoggingStatus()
        fetchGenres()
        loadTvShows()
    }

    override fun onLoginButtonClick() {
        emitEffect(TrendingMediaScreenEffect.NavigateToLogin)
    }

    override fun onDismissLoginBottomSheet() {
        updateState { copy(showLoginBottomSheet = false) }
    }

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
    }

    private fun onDataLoadError(e: Throwable) {
        if (e is NoNetworkException) {
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
        } else {
            updateState {
                copy(
                    isNoInternetConnection = false,
                    snackBarData =
                        SnackData(message = stringProvider.somethingWentWrongError, isError = true)
                )
            }
        }
    }

    override fun onDismissSaveToListBottomSheet() {
        TODO("Not yet implemented")
    }

    override fun onCreateNewListClick() {
        TODO("Not yet implemented")
    }

    override fun onDismissAddListBottomSheet() {
        TODO("Not yet implemented")
    }

    private fun createTvShowsPagingSource(onError: ((Throwable) -> Unit)? = ::onDataLoadError): PagingSource<Int, TvSeries> {
        return BasePagingSourceForHome(onError = onError) { page ->
            manageTvSeriesUseCase.getTrendingTvSeries(
                page = page,
                genreId = state.value.selectedGenreId
            )
        }
    }
}