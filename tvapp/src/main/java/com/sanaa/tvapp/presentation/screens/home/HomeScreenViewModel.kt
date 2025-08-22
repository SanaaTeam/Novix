package com.sanaa.tvapp.presentation.screens.home

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.tvapp.base.BasePagingSource
import com.sanaa.tvapp.base.BaseViewModel
import com.sanaa.tvapp.state.MediaItemUiState
import com.sanaa.tvapp.state.MediaTypeUiState
import com.sanaa.tvapp.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.MediaHistoryItem
import entity.Movie
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.ManageMovieUseCase
import usecase.ManageTvShowUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val manageTvSeriesUseCase: ManageTvShowUseCase,
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<HomeScreenUiState, HomeScreenEffect>(
    initialState = HomeScreenUiState(),
    defaultDispatcher = dispatcher
) {

    init {
        updateUserLoggingStatus()
        fetchPopularMediaData()
        fetchTopRatedMovieData()
        fetchTopRatedTvShowData()
        fetchMovieGenres()
        fetchUpcomingMovies()
    }

    private fun updateUserLoggingStatus() {
        tryToCollect(
            block = { checkIfUserIsLoggedInUseCase.isLoggedIn() },
            onCollect = ::onCollectLoggedFlag,
            onError = ::onErrorLoadingData
        )
    }

    private fun onCollectLoggedFlag(isLogged: Boolean) {
        if (isLogged != state.value.userIsLoggedIn) {
            fetchWatchedMediaData()
            updateState { copy(userIsLoggedIn = isLogged) }
        }
    }

    private fun fetchPopularMediaData() {
        updateState { copy(isLoading = true, errorMessage = null) }
        tryToExecute(
            block = {
                val popularMovies = manageMovieUseCase
                    .getPopularMovies(1).map { it.toState() }
                val popularTvSeries = manageTvSeriesUseCase
                    .getPopularTvShows(1).map { it.toState() }
                (popularMovies + popularTvSeries).shuffled()
            },
            onSuccess = { popularMediaList ->
                updateState {
                    copy(
                        isLoading = false,
                        errorMessage = null,
                        popularMedia = popularMediaList.take(10)
                    )
                }
            },
            onError = ::onErrorLoadingData
        )
    }

    private fun fetchTopRatedMovieData() {
        updateState { copy(isLoading = true, errorMessage = null) }
        tryToExecute(
            block = {
                val topRatedMovies = manageMovieUseCase
                    .getTopRatedMovies(1, null).map { it.toState() }
                val topRatedTvSeries = manageTvSeriesUseCase
                    .getTopRatedTvShows(1, null).map { it.toState() }
                (topRatedMovies + topRatedTvSeries).shuffled()
            },
            onSuccess = { topRatedMediaList ->
                updateState {
                    copy(
                        isLoading = false,
                        errorMessage = null,
                        topRatingMovies = topRatedMediaList.take(10)
                    )
                }
            },
            onError = ::onErrorLoadingData,
        )
    }

    private fun fetchTopRatedTvShowData() {
        updateState { copy(isLoading = true, errorMessage = null) }
        tryToExecute(
            block = {
                manageTvSeriesUseCase.getTopRatedTvShows(1, null).map { it.toState() }
            },
            onSuccess = { topRatedMediaList ->
                updateState {
                    copy(
                        isLoading = false,
                        errorMessage = null,
                        topRatingTvShows = topRatedMediaList.take(10)
                    )
                }
            },
            onError = ::onErrorLoadingData,
        )
    }

    private fun fetchWatchedMediaData() {
        updateState { copy(isLoading = true, errorMessage = null) }
        tryToCollect(
            block = { loadWatchedMediaHistory() },
            onCollect = ::onFetchWatchedMediaSuccess,
            onError = ::onErrorLoadingData
        )
    }

    private fun onFetchWatchedMediaSuccess(mediaList: List<MediaHistoryItem>) {
        updateState {
            copy(
                isLoading = false,
                errorMessage = null,
                continueWatchingMovies = mediaList.map { it.toState() }
                    .filter { it.mediaTypeUiState == MediaTypeUiState.MOVIE },
                continueWatchingTvShows = mediaList.map { it.toState() }
                    .filter { it.mediaTypeUiState == MediaTypeUiState.TV_SHOW }
            )
        }
    }


    private fun fetchMovieGenres() {
        tryToExecute(
            block = {
                updateState {
                    copy(isLoading = true)
                }
                manageMovieUseCase.getMovieGenres().map { it.toState() }
            },
            onSuccess = { genres ->
                updateState {
                    copy(movieGenres = genres, isLoading = false)
                }
            },
            onError = ::onErrorLoadingData,
        )
    }


    private fun fetchUpcomingMovies(genreId: Int? = null) {
        tryToExecute(
            block = { loadUpcomingMovies(genreId) },
            onSuccess = { flowWithSaved ->
                updateState { copy(upcomingMovies = flowWithSaved) }
            },
            onError = ::onErrorLoadingData
        )
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadWatchedMediaHistory(): Flow<List<MediaHistoryItem>> {
        return getLoggedInUserUseCase.getLoggedInUser()
            .flatMapLatest { user ->
                manageWatchedMediaHistoryUseCase.getMediaHistory(
                    username = user.username,
                    genreId = null,
                    mediaType = null
                )
            }
            .catch { e ->
                emit(emptyList())
            }
    }

    private fun loadUpcomingMovies(
        genreId: Int?,
    ): Flow<PagingData<MediaItemUiState>> {
        return createPagingFlow(
            pagingSourceFactory = {
                createUpcomingMoviesPagingDataSource(
                    genreId = genreId
                )
            },
            mapper = Movie::toState
        )
    }

    fun createUpcomingMoviesPagingDataSource(
        genreId: Int?,
    ): PagingSource<Int, Movie> {
        return BasePagingSource { page ->
            manageMovieUseCase.getUpcomingMovies(
                page = page,
                genreId = genreId
            )
        }
    }

    private fun onErrorLoadingData(e: Throwable) {
        when (e) {
            is NoNetworkException -> updateState { copy(isNoInternet = true, isLoading = false) }
            else -> updateState { copy(errorMessage = e.message, isLoading = false) }
        }
    }
}