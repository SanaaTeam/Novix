package com.sanaa.presentation.screen.trendingMediaScreen.trendingTvShowScreen

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.base.BasePagingSourceForHome
import com.sanaa.presentation.screen.trendingMediaScreen.MediaListScreenInteractionListener
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenEffect
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenUiState
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItem
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
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
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
            callee = { checkIfUserIsLoggedInUseCase.isLoggedIn() },
            onCollect = { isLogged ->
                updateState {
                    it.copy(
                        userIsLoggedIn = isLogged
                    )
                }
            },
        )
    }

    private fun fetchGenres() {
        tryToExecute(
            callee = ::loadGenresOperation,
            onSuccess = ::onLoadGenresSuccess,
            onError = ::onDataLoadError
        )
    }

    private suspend fun loadGenresOperation(): List<GenreUiState> {
        updateState {
            it.copy(isLoading = true)
        }
        return manageTvSeriesUseCase.getSeriesGenres().map { it.toState() }
    }

    private fun onLoadGenresSuccess(genres: List<GenreUiState>) {
        updateState {
            it.copy(genreList = genres, isLoading = false, isNoInternetConnection = false)
        }
    }

    private fun loadTvShows() {
        tryToCollect(
            callee = ::loadTvShowsOperation,
            onCollect = ::onLoadTvShowsSuccess,
            onError = ::onDataLoadError
        )
    }

    private fun loadTvShowsOperation(): Flow<PagingData<MediaItem>> {
        return createPagingFlow(
            pagingSourceFactory = { createTvShowsPagingSource() },
            mapper = TvSeries::toState
        )
    }

    private fun onLoadTvShowsSuccess(pagingData: PagingData<MediaItem>) {
        updateState {
            it.copy(
                mediaList = flowOf(pagingData),
                isLoading = false,
                isNoInternetConnection = false
            )
        }
    }

    override fun onGenreClick(id: Int?) {
        if (id == state.value.selectedGenreId) return
        updateState {
            it.copy(selectedGenreId = id)
        }
        loadTvShows()
    }

    override fun onMediaClick(id: Int) {
        emitEffect(TrendingMediaScreenEffect.NavigateToMediaDetails(id))
    }

    override fun onSaveIconClick(media: MediaItem) {
        if (!state.value.userIsLoggedIn) {
            updateState {
                it.copy(
                    showBottomSheet = true
                )
            }
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

    override fun onDismissBottomSheet() {
        updateState { it.copy(showBottomSheet = false) }
    }


    private fun onDataLoadError(e: Throwable) {
        if (e is NoNetworkException) {
            updateState { it.copy(isNoInternetConnection = true) }
            emitEffect(TrendingMediaScreenEffect.ShowError(message = stringProvider.noInternetConnectionError))
        } else {
            updateState { it.copy(isNoInternetConnection = false) }
            emitEffect(TrendingMediaScreenEffect.ShowError(message = stringProvider.somethingWentWrongError))
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