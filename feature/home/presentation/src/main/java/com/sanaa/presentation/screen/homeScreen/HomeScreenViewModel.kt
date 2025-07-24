package com.sanaa.presentation.screen.homeScreen

import com.sanaa.presentation.baseViewModel.BaseViewModel
import com.sanaa.presentation.model.MediaItem
import com.sanaa.presentation.model.MediaType
import entity.Movie
import entity.TvSeries
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
                    .getPopularMovies(5).toMedia()
                val popularTvSeries = manageTvSeriesUseCase
                    .getPopularSeries(5, null).toMedia()
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
                    .getTopRatedMovies(10,null).toMedia()
                val topRatedTvSeries = manageTvSeriesUseCase
                    .getTopRatedTvSeries(10,null).toMedia()
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
                    .getWatchedMoviesHistory(10,null).toMedia()
                val watchedTvSeries = manageHistoryUseCase
                    .getWatchedSeriesHistory(10,null).toMedia()
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

    companion object{
        fun List<Movie>.toMedia(): List<MediaItem>{
            return this.map {
                MediaItem(
                    id = it.id,
                    title = it.title,
                    imageUrl = it.posterImageUrl,
                    rating = it.imdbRating,
                    mediaType = MediaType.MOVIE
                )
            }
        }
        fun List<TvSeries>.toMedia(): List<MediaItem>{
            return this.map {
                MediaItem(
                    id = it.id,
                    title = it.title,
                    imageUrl = it.posterImageUrl.orEmpty(),
                    rating = it.imdbRating,
                    mediaType = MediaType.TV_SHOW
                )
            }
        }
    }

}