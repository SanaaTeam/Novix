package com.sanaa.presentation.screen.homeScreen

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.base.BasePagingSourceForHome
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaTypeUi
import com.sanaa.presentation.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.MediaHistoryItem
import entity.Movie
import exceptions.NoNetworkException
import exceptions.NoLoggedInUserException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import usecase.GetLoggedInUserUseCase
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import javax.inject.Inject
import usecase.history.ManageWatchedMediaHistoryUseCase

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<HomeScreenUiState, HomeScreenEffect>(
    initialState = HomeScreenUiState(),
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
                    .getPopularMovies(1).map { it.toState() }
                val popularTvSeries = manageTvSeriesUseCase
                    .getPopularSeries(1).map { it.toState() }
                (popularMovies + popularTvSeries).shuffled()
            },
            onSuccess = { popularMediaList ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        popularMedia = popularMediaList.take(10)
                    )
                }
            },
            onError = ::onErrorLoadingData
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
                        topRatingMedia = topRatedMediaList.take(10)
                    )
                }
            },
            onError = ::onErrorLoadingData,
        )
    }

    private fun fetchWatchedMediaData() {
        updateState { it.copy(isLoading = true, errorMessage = null) }
        tryToCollect(
            callee = { loadWatchedMediaHistory() },
            onCollect = { watchedMediaList ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        continueWatchingMedia = watchedMediaList.map { it.toState() }
                    )
                }
            },
            onError = ::onErrorLoadingData
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
            onError = ::onErrorLoadingData,
        )
    }

    private fun fetchUpcomingMovies(
        genreId: Int? = null
    ) {
        tryToCollect(
            callee = { loadUpcomingMovies(genreId) },
            onCollect = { mediaList ->
                updateState {
                    it.copy(
                        upcomingMovies = flowOf(mediaList),
                    )
                }
            },
            onError = ::onErrorLoadingData,
        )
    }

    private suspend fun loadWatchedMediaHistory(): Flow<List<MediaHistoryItem>> {
        val user = try {
            getLoggedInUserUseCase.getLoggedInUser()
        } catch (_: NoLoggedInUserException) {
            null
        }
        if (user == null) return flowOf(emptyList())
        return manageWatchedMediaHistoryUseCase.getMediaHistory(user.username, null, null)
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

    override fun onMediaClick(id: Int, mediaTypeUi: MediaTypeUi) {
        emitEffect(HomeScreenEffect.NavigateToMediaDetails(id, mediaTypeUi))
    }

    override fun onSaveIconClick(media: MediaItem) {
        updateState {
            it.copy(showBottomSheet = true)
        }
    }

    override fun onDismissBottomSheet() {

        updateState { it.copy(showBottomSheet = false) }
    }

    override fun onRetryClick() {
        updateState { it.copy(isNoInternet = false) }
        fetchPopularMediaData()
        fetchTopRatedMediaData()
        fetchWatchedMediaData()
        fetchMovieGenres()
        fetchUpcomingMovies()
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


    private fun onErrorLoadingData(e: Throwable) {
        when (e) {
            is NoNetworkException -> updateState { it.copy(isNoInternet = true, isLoading = false) }
            else -> updateState { it.copy(errorMessage = e.message, isLoading = false) }
        }
    }
}