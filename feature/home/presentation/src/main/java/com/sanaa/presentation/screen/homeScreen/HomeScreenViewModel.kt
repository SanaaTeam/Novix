package com.sanaa.presentation.screen.homeScreen

import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaType
import com.sanaa.presentation.state.mapper.toState
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
) : BaseViewModel<HomeScreenUiState, HomeScreenEffect>(
    initialState = HomeScreenUiState(),
    defaultDispatcher = dispatcher
), HomeScreenInteractionListener {
    init {
        fetchPopularMediaData()
        fetchTopRatedMediaData()
        fetchWatchedMediaData()
        fetchMovieGenres()
        fetchUpcomingMovies()
    }
    private fun fetchPopularMediaData() {
        updateState { it.copy(isLoading = true, errorMessage = null) }
        tryToExecute(
            callee = {
                val popularMovies = manageMovieUseCase
                    .getPopularMovies(5).map { it.toState() }
                val popularTvSeries = manageTvSeriesUseCase
                    .getPopularSeries(5).map { it.toState() }
                (popularMovies + popularTvSeries).shuffled()
            },
            onSuccess = {popularMediaList->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        popularMedia = popularMediaList
                    )
                }
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
                    .getTopRatedMovies(10,null).map { it.toState() }
                val topRatedTvSeries = manageTvSeriesUseCase
                    .getTopRatedTvSeries(10,null).map { it.toState() }
                (topRatedMovies + topRatedTvSeries).shuffled()
            },
            onSuccess = { topRatedMediaList->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        topRatingMedia = topRatedMediaList
                    )
                }
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
                    .getWatchedMoviesHistory(10,null).map { it.toState() }
                val watchedTvSeries = manageHistoryUseCase
                    .getWatchedSeriesHistory(10,null).map { it.toState() }
                (watchedMovies + watchedTvSeries).shuffled()
            },
            onSuccess = { watchedMediaList->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        continueWatchingMedia = watchedMediaList
                    )
                }
            },
            onError = {e->
                updateState { it.copy(isLoading = false, errorMessage = e.message) }
            },
        )
    }
    private fun fetchMovieGenres() {
        tryToExecute(
            callee = {
                updateState {
                    it.copy(isLoading = true)
                }
                manageMovieUseCase.getMovieGenres().map { it.toState() }
            },
            onSuccess = { genres ->
                updateState {
                    it.copy(movieGenres = genres, isLoading = false)
                }
            },
            onError = { exception ->
                updateState {
                    it.copy(errorMessage = exception.message, isLoading = false)
                }
            }
        )
    }
    private fun fetchUpcomingMovies(genreId: Int? = null) {
        tryToExecute(
            callee = {
                updateState {
                    it.copy(isLoading = true, movieSelectedGenreId = genreId)
                }
                manageMovieUseCase.getTrendingMovies(1, state.value.movieSelectedGenreId)
                    .map { it.toState() }
            },
            onSuccess = { mediaList ->
                updateState {
                    it.copy(
                        isLoading = false,
                        upcomingMovies = mediaList
                    )
                }
            },
            onError = { exception ->
                updateState {
                    it.copy(errorMessage = exception.message, isLoading = false)
                }
            }
        )
    }


    override fun onMoviesCardClicked() {
        emitEffect(HomeScreenEffect.NavigateToMoviesScreen)
    }

    override fun onTvShowsCardClicked() {
        emitEffect(HomeScreenEffect.NavigateToTvShowsScreen)
    }

    override fun onPeopleCardClicked() {
        emitEffect(HomeScreenEffect.NavigateToPeopleScreen)
    }

    override fun onShowAllTopRatingClicked() {
        emitEffect(HomeScreenEffect.NavigateToTopRatingMediaScreen)
    }

    override fun onShowAllContinueWatchingClicked() {
        emitEffect(HomeScreenEffect.NavigateToWatchedMediaScreen)
    }

    override fun onMovieGenreClick(id: Int?) {
        if (id != state.value.movieSelectedGenreId) {
            fetchUpcomingMovies(id)
        }
    }

    override fun onMediaClick(id: Int, mediaType: MediaType) {
        emitEffect(HomeScreenEffect.NavigateToMediaDetails(id, mediaType))
    }

    override fun onSaveIconClick(media: MediaItem) {
        TODO("Not yet implemented")
    }

    override fun onRetryClick(){
        fetchPopularMediaData()
        fetchTopRatedMediaData()
        fetchWatchedMediaData()
        fetchMovieGenres()
        fetchUpcomingMovies()
    }
}