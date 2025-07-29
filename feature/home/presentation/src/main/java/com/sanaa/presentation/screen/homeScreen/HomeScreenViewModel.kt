package com.sanaa.presentation.screen.homeScreen

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.base.BasePagingSourceForHome
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaType
import com.sanaa.presentation.state.mapper.toState
import entity.Movie
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
            onSuccess = { popularMediaList ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        popularMedia = popularMediaList
                    )
                }
            },
            onError = { e ->
                updateState { it.copy(isLoading = false, errorMessage = e.message) }
            },
        )
    }

    private fun fetchTopRatedMediaData() {
        updateState { it.copy(isLoading = true, errorMessage = null) }
        tryToExecute(
            callee = {
                val topRatedMovies = manageMovieUseCase
                    .getTopRatedMovies(1, null).map { it.toState() }
                val topRatedTvSeries = manageTvSeriesUseCase
                    .getTopRatedTvSeries(1, null).map { it.toState() }
                (topRatedMovies + topRatedTvSeries).shuffled()
            },
            onSuccess = { topRatedMediaList ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        topRatingMedia = topRatedMediaList
                    )
                }
            },
            onError = { e ->
                updateState { it.copy(isLoading = false, errorMessage = e.message) }
            },
        )
    }

    private fun fetchWatchedMediaData() {
        updateState { it.copy(isLoading = true, errorMessage = null) }
        tryToExecute(
            callee = {
                val watchedMovies = manageHistoryUseCase
                    .getWatchedMoviesHistory(10, null).map { it.toState() }
                val watchedTvSeries = manageHistoryUseCase
                    .getWatchedSeriesHistory(10, null).map { it.toState() }
                (watchedMovies + watchedTvSeries).shuffled()
            },
            onSuccess = { watchedMediaList ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        continueWatchingMedia = watchedMediaList
                    )
                }
            },
            onError = { e ->
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

    private fun fetchUpcomingMovies(
        genreId: Int? = null
    ) {
        tryToExecute(
            callee = { loadUpcomingMovies(genreId) },
            onSuccess = { mediaList ->
                updateState {
                    it.copy(
                        isLoadingUpcoming = false,
                        upcomingMovies = mediaList
                    )
                }
            },
            onError = { exception ->
                updateState {
                    it.copy(
                        errorMessage = exception.message,
                        isLoadingUpcoming = false
                    )
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
            updateState { it.copy(movieSelectedGenreId = id) }
            fetchUpcomingMovies(id)
        }
    }

    override fun onMediaClick(id: Int, mediaType: MediaType) {
        emitEffect(HomeScreenEffect.NavigateToMediaDetails(id, mediaType))
    }

    override fun onSaveIconClick(media: MediaItem) {
        updateState { it.copy(showBottomSheet = true) }
    }

    override fun onDismissBottomSheet() {
        updateState { it.copy(showBottomSheet = false) }
    }


    private fun loadUpcomingMovies(
        genreId: Int?
    ): Flow<PagingData<MediaItem>> {
        return createPagingFlow(
            pagingSourceFactory = {
                createUpcomingMoviesPagingDataSource(
                    genreId = genreId
                )
            },
            mapper = Movie::toState
        )
    }

    private fun createUpcomingMoviesPagingDataSource(
        genreId: Int?
    ): PagingSource<Int, Movie> {
        return BasePagingSourceForHome { page ->
            manageMovieUseCase.getUpcomingMovies(
                page = page,
                genreId = genreId
            )
        }
    }
}