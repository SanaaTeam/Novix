package com.sanaa.tvapp.presentation.screens.home

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.tvapp.base.BasePagingSource
import com.sanaa.tvapp.base.BaseViewModel
import com.sanaa.tvapp.state.MediaItemUiState
import com.sanaa.tvapp.state.MediaTypeUiState
import com.sanaa.tvapp.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.MediaHistoryItem
import entity.Movie
import entity.TvShow
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
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
    private val manageTvShowsUseCase: ManageTvShowUseCase,
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase,
    private val preferencesManager: PreferencesManager,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<HomeScreenUiState, HomeScreenEffect>(
    initialState = HomeScreenUiState(),
    defaultDispatcher = dispatcher
), HomeScreenInteractionListener {

    init {
        updateUserLoggingStatus()
        fetchPopularMediaData()
        fetchTopRatedMovieData()
        fetchTopRatedTvShowData()
        fetchUpcomingMovies()
        fetchUpcomingTvShows()
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
            loadRatedMedia()
            updateState { copy(userIsLoggedIn = isLogged) }
        }
    }

    private fun fetchPopularMediaData() {
        tryToExecute(
            onStart = { updateState { copy(isLoading = true) } },
            block = ::loadPopularMediaOperation,
            onSuccess = ::onFetchPopularMediaSuccess,
            onError = ::onErrorLoadingData
        )
    }

    private suspend fun loadPopularMediaOperation(): List<MediaItemUiState> {
        val popularMovies = manageMovieUseCase.getPopularMovies(1).map { it.toState() }.take(5)
        val popularTvShows = manageTvShowsUseCase.getPopularTvShows(1).map { it.toState() }.take(5)

        return (popularMovies + popularTvShows).sortedByDescending { media -> media.imdbRating }
    }

    private fun onFetchPopularMediaSuccess(mediaList: List<MediaItemUiState>) {
        updateState {
            copy(
                isLoading = false,
                isNoInternet = false,
                popularMedia = mediaList
            )
        }
    }

    private fun fetchTopRatedMovieData() {
        tryToExecute(
            onStart = { updateState { copy(isLoading = true, errorMessage = null) } },
            block = { manageMovieUseCase.getTopRatedMovies(1, null).map { it.toState() } },
            onSuccess = ::onFetchTopRatedMoviesSuccess,
            onError = ::onErrorLoadingData,
        )
    }

    private fun onFetchTopRatedMoviesSuccess(movies: List<MediaItemUiState>) {
        updateState {
            copy(
                isLoading = false,
                errorMessage = null,
                topRatingMovies = movies
            )
        }
    }

    private fun fetchTopRatedTvShowData() {
        updateState { copy(isLoading = true, errorMessage = null) }
        tryToExecute(
            block = { manageTvShowsUseCase.getTopRatedTvShows(1, null).map { it.toState() } },
            onSuccess = ::onFetchTopRatedTvShowsSuccess,
            onError = ::onErrorLoadingData,
        )
    }

    private fun onFetchTopRatedTvShowsSuccess(tvShows: List<MediaItemUiState>) {
        updateState {
            copy(
                isLoading = false,
                errorMessage = null,
                topRatingTvShows = tvShows
            )
        }
    }

    private fun fetchWatchedMediaData() {
        updateState { copy(isLoading = true, errorMessage = null) }
        tryToCollect(
            block = ::loadWatchedMediaHistory,
            onCollect = ::onFetchWatchedMediaSuccess,
            onError = ::onErrorLoadingData
        )
    }

    private fun onFetchWatchedMediaSuccess(mediaList: List<MediaHistoryItem>) {
        updateState {
            copy(
                isLoading = false,
                errorMessage = null,
                WatchingHistoryMovies = mediaList.map { it.toState() }
                    .filter { it.mediaTypeUiState == MediaTypeUiState.MOVIE },
                WatchingHistoryTvShows = mediaList.map { it.toState() }
                    .filter { it.mediaTypeUiState == MediaTypeUiState.TV_SHOW }
            )
        }
    }


    private fun fetchUpcomingMovies() {
        tryToExecute(
            onStart = { updateState { copy(isLoading = true) } },
            block = { loadUpcomingMovies() },
            onSuccess = ::onFetchUpcomingMoviesSuccess,
            onError = ::onErrorLoadingData
        )
    }

    private fun onFetchUpcomingMoviesSuccess(movies: Flow<PagingData<MediaItemUiState>>) {
        updateState {
            copy(
                upcomingMovies = movies,
                isLoading = false
            )
        }
    }

    private fun fetchUpcomingTvShows() {
        tryToExecute(
            onStart = { updateState { copy(isLoading = true) } },
            block = { loadUpcomingTvShows() },
            onSuccess = ::onFetchUpcomingTvShowsSuccess,
            onError = ::onErrorLoadingData
        )
    }

    private fun onFetchUpcomingTvShowsSuccess(tvShows: Flow<PagingData<MediaItemUiState>>) {
        updateState {
            copy(
                upcomingTvShows = tvShows,
                isLoading = false
            )
        }
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

    private fun loadUpcomingMovies(): Flow<PagingData<MediaItemUiState>> {
        return createPagingFlow(
            pagingSourceFactory = { createUpcomingMoviesPagingDataSource() },
            mapper = Movie::toState
        )
    }

    private fun loadUpcomingTvShows(): Flow<PagingData<MediaItemUiState>> {
        return createPagingFlow(
            pagingSourceFactory = { createUpcomingTvShowsPagingDataSource() },
            mapper = TvShow::toState
        )
    }

    fun createUpcomingMoviesPagingDataSource(): PagingSource<Int, Movie> {
        return BasePagingSource { page ->
            manageMovieUseCase.getUpcomingMovies(
                page = page,
                genreId = null
            )
        }
    }

    fun createUpcomingTvShowsPagingDataSource(): PagingSource<Int, TvShow> {
        return BasePagingSource { page ->
            manageTvShowsUseCase.getUpcomingTvShows(
                page = page,
                genreId = null
            )
        }
    }

    private fun loadRatedMedia() {
        loadRatedMovies()
        loadRatedTvShows()
    }

    private fun loadRatedMovies() {
        tryToExecute(
            block = { manageMovieUseCase.getUserRatedMovies() },
            onSuccess = ::onLoadMoviesSuccess,
            onError = ::onErrorLoadingData
        )
    }

    private fun onLoadMoviesSuccess(movies: List<Movie>) {
        updateState {
            copy(
                ratedMovies = movies.map { it.toState() },
                isLoading = false
            )
        }
    }

    private fun loadRatedTvShows() {
        tryToExecute(
            block = ::loadRatedTvShowsOperation,
            onSuccess = ::onLoadTvShowsSuccess,
            onError = ::onErrorLoadingData
        )
    }

    private fun onLoadTvShowsSuccess(tvShows: List<TvShow>) {
        updateState {
            copy(
                ratedTvShows = tvShows.map { it.toState() },
                isLoading = false
            )
        }
    }

    private suspend fun loadRatedTvShowsOperation(): List<TvShow> {
        val accountId = preferencesManager.accountId.first()
        val sessionId = preferencesManager.sessionId.first()
        return manageTvShowsUseCase.getRatedTvShows(accountId, sessionId)
    }

    private fun onErrorLoadingData(e: Throwable) {
        when (e) {
            is NoNetworkException -> updateState { copy(isNoInternet = true, isLoading = false) }
            else -> updateState {
                copy(
                    errorMessage = e.message,
                    isNoInternet = false,
                    isLoading = false
                )
            }
        }
    }

    override fun onMediaClick(id: Int, mediaTypeUiState: MediaTypeUiState) {
        emitEffect(HomeScreenEffect.NavigateToMediaDetails(id, mediaTypeUiState))
    }

    override fun onTabClick(selectedTab: SelectedHomeTab) {
        updateState { copy(selectedTab = selectedTab) }
    }

    override fun onRetryClick() {
        updateUserLoggingStatus()
        fetchPopularMediaData()
        fetchTopRatedMovieData()
        fetchTopRatedTvShowData()
        fetchUpcomingMovies()
        loadRatedMedia()
    }

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
    }

    override fun onTvShowsRateUpdated() {
        loadRatedTvShows()
    }

    override fun onMoviesRateUpdated() {
       loadRatedMovies()
    }
}