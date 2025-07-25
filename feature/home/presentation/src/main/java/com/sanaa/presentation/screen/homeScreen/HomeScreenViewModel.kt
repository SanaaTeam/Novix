package com.sanaa.presentation.screen.homeScreen

import com.sanaa.presentation.baseViewModel.BaseViewModel
import com.sanaa.presentation.mapper.movieToMedia
import com.sanaa.presentation.mapper.tvShowToMedia
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import usecase.history.ManageHistoryUseCase

class HomeScreenViewModel(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val manageHistoryUseCase: ManageHistoryUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<HomeScreenUiState, HomeScreenUiEffect>(
    initialState = HomeScreenUiState(),
    defaultDispatcher = dispatcher
) {
    init {

    }
    private fun fetchPopularMediaData() {
        updateState { it.copy(isLoading = true, errorMessage = null) }
        tryToExecute(
            callee = {
                val popularMovies = manageMovieUseCase
                    .getPopularMovies(5).movieToMedia()
                val popularTvSeries = manageTvSeriesUseCase
                    .getPopularSeries(5).tvShowToMedia()
                updateState {
                    it.copy(
                        popularMedia = (popularMovies + popularTvSeries).shuffled()
                    )
                }
            },
            onSuccess = {
                updateState { it.copy(isLoading = false, errorMessage = null) }
            },
            onError = {e->
                updateState { it.copy(isLoading = false, errorMessage = e.message) }
            },
        )
    }
    private fun fetchTopRatedMediaData(){
        updateState { it.copy(isLoading = true, errorMessage = null) }
        tryToExecute(
            callee = {
                val topRatedMovies = manageMovieUseCase
                    .getTopRatedMovies(10,null).movieToMedia()
                val topRatedTvSeries = manageTvSeriesUseCase
                    .getTopRatedTvSeries(10,null).tvShowToMedia()
                updateState {
                    it.copy(
                        topRatingMedia = (topRatedMovies + topRatedTvSeries).shuffled()
                    )
                }
            },
            onSuccess = {
                updateState { it.copy(isLoading = false, errorMessage = null) }
            },
            onError = {e->
                updateState { it.copy(isLoading = false, errorMessage = e.message) }
            },
        )
    }
    private fun fetchWatchedMediaData(){
        updateState { it.copy(isLoading = true, errorMessage = null) }
        tryToExecute(
            callee = {
                val watchedMovies = manageHistoryUseCase
                    .getWatchedMoviesHistory(10,null).movieToMedia()
                val watchedTvSeries = manageHistoryUseCase
                    .getWatchedSeriesHistory(10,null).tvShowToMedia()
                updateState {
                    it.copy(
                        continueWatchingMedia = (watchedMovies + watchedTvSeries).shuffled()
                    )
                }
            },
            onSuccess = {
                updateState { it.copy(isLoading = false, errorMessage = null) }
            },
            onError = {e->
                updateState { it.copy(isLoading = false, errorMessage = e.message) }
            },
        )
    }


}