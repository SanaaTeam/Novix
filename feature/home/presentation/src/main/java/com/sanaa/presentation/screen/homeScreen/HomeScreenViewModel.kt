package com.sanaa.presentation.screen.homeScreen

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.map
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.base.BasePagingSourceForHome
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaTypeUi
import com.sanaa.presentation.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.MediaHistoryItem
import entity.Movie
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import repository.SavedMovieStatusProvider
import service.VodStringProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val savedMovieStatusProvider: SavedMovieStatusProvider,
) : BaseViewModel<HomeScreenUiState, HomeScreenEffect>(
    initialState = HomeScreenUiState(),
    defaultDispatcher = dispatcher
), HomeScreenInteractionListener {

    init {
        updateUserLoggingStatus()
        fetchPopularMediaData()
        fetchTopRatedMediaData()
        fetchWatchedMediaData()
        fetchMovieGenres()
        fetchUpcomingMovies()
        viewModelScope.launch {
            savedMovieStatusProvider.savedIds.collect { savedIds ->
                updateState { current ->
                    current.copy(
                        popularMedia = current.popularMedia.map { it.withSaved(savedIds) },
                        topRatingMedia = current.topRatingMedia.map { it.withSaved(savedIds) },
                        continueWatchingMedia = current.continueWatchingMedia.map {
                            it.withSaved(
                                savedIds
                            )
                        }
                    )
                }
            }
        }
    }

    private fun MediaItem.withSaved(savedIds: Set<Int>) =
        copy(isSaved = savedIds.contains(id))

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

    private fun fetchPopularMediaData() {
        updateState { it.copy(isLoadingPopular = true) }
        tryToExecute(
            callee = ::loadPopularMediaOperation,
            onSuccess = ::onFetchPopularMediaSuccess,
            onError = ::onDataLoadError
        )
    }

    private suspend fun loadPopularMediaOperation(): List<MediaItem> {
        val popularMovies = manageMovieUseCase.getPopularMovies(1).map { it.toState() }
        val popularTvSeries = manageTvSeriesUseCase.getPopularSeries(1).map { it.toState() }

        return (popularMovies + popularTvSeries).sortedByDescending { media -> media.rating }
    }

    private fun onFetchPopularMediaSuccess(mediaList: List<MediaItem>) {
        updateState {
            it.copy(
                isLoadingPopular = false,
                isNoInternetConnection = false,
                popularMedia = mediaList.take(10)
            )
        }
    }

    private fun fetchTopRatedMediaData() {
        updateState { it.copy(isLoadingTopRated = true) }
        tryToExecute(
            callee = ::loadTopRatedMediaOperation,
            onSuccess = ::onFetchTopRatedMediaSuccess,
            onError = ::onDataLoadError,
        )
    }

    private suspend fun loadTopRatedMediaOperation(): List<MediaItem> {
        val topRatedMovies = manageMovieUseCase.getTopRatedMovies(1, null).map { it.toState() }
        val topRatedTvSeries =
            manageTvSeriesUseCase.getTopRatedTvSeries(1, null).map { it.toState() }

        return (topRatedMovies + topRatedTvSeries).sortedByDescending { it.rating }
    }

    private fun onFetchTopRatedMediaSuccess(mediaList: List<MediaItem>) {
        updateState {
            it.copy(
                isLoadingTopRated = false,
                isNoInternetConnection = false,
                topRatingMedia = mediaList.take(10)
            )
        }
    }

    private fun fetchWatchedMediaData() {
        tryToCollect(
            callee = ::loadWatchedMediaHistory,
            onCollect = ::onFetchWatchedMediaSuccess,
            onError = ::onDataLoadError
        )
    }

    private fun onFetchWatchedMediaSuccess(mediaList: List<MediaHistoryItem>) {
        updateState {
            it.copy(
                continueWatchingMedia = mediaList.map { it.toState() },
                isNoInternetConnection = false,
                isLoadingHistory = false
            )
        }
    }

    private fun fetchMovieGenres() {
        tryToExecute(
            callee = ::fetchMovieGenresOperation,
            onSuccess = ::onFetchMovieGenresSuccess,
            onError = ::onDataLoadError,
        )
    }

    private suspend fun fetchMovieGenresOperation(): List<GenreUiState> {
        updateState { it.copy(isLoadingGenre = true) }
        return manageMovieUseCase.getMovieGenres().map { it.toState() }
    }

    private fun onFetchMovieGenresSuccess(genres: List<GenreUiState>) {
        updateState {
            it.copy(movieGenres = genres, isLoadingGenre = false, isNoInternetConnection = false)
        }
    }

    private fun fetchUpcomingMovies(genreId: Int? = null) {
        tryToCollect(
            callee = {
                loadUpcomingMovies(genreId).combine(savedMovieStatusProvider.savedIds) { pagingData, savedIds ->
                    pagingData.map { it.withSaved(savedIds) } // PagingData معدَّلة
                }
                    .cachedIn(viewModelScope)
            },
            onCollect = ::onFetchUpcomingMoviesSuccess,
            onError = ::onDataLoadError,
        )
    }


    private fun loadUpcomingMovies(
        genreId: Int?
    ): Flow<PagingData<MediaItem>> {
        return createPagingFlow(
            pagingSourceFactory = { createUpcomingMoviesPagingDataSource(genreId = genreId) },
            mapper = Movie::toState
        )
    }

    private fun onFetchUpcomingMoviesSuccess(pagingData: PagingData<MediaItem>) {
        updateState { it.copy(upcomingMovies = flowOf(pagingData), isNoInternetConnection = false) }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadWatchedMediaHistory(): Flow<List<MediaHistoryItem>> {
        updateState { it.copy(isLoadingHistory = true) }

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

    override fun onMoviesCardClick() {
        emitEffect(HomeScreenEffect.NavigateToMoviesScreen)
    }

    override fun onTvShowsCardClick() {
        emitEffect(HomeScreenEffect.NavigateToTvShowsScreen)
    }

    override fun onPeopleCardClick() {
        emitEffect(HomeScreenEffect.NavigateToPeopleScreen)
    }

    override fun onShowAllTopRatingClick() {
        emitEffect(HomeScreenEffect.NavigateToTopRatingMediaScreen)
    }

    override fun onShowAllContinueWatchingClick() {
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
        if (state.value.userIsLoggedIn) {
            if (media.isSaved) {
                savedMovieStatusProvider.markUnsaved(media.id)
            } else {
                updateState {
                    it.copy(
                        showSaveToListBottomSheet = true,
                        selectedMediaId = media.id.toLong()
                    )
                }
            }
        } else {
            emitEffect(HomeScreenEffect.NavigateToPlayListScreen)
        }
    }

    override fun onDismissBottomSheet() {
        updateState { it.copy(showBottomSheet = false) }
    }

    override fun onDismissSaveToListBottomSheet() {
        updateState { it.copy(showSaveToListBottomSheet = false) }
    }

    override fun onSaveToListSuccess() {
        emitEffect(HomeScreenEffect.ShowSuccess(message = stringProvider.addToListSuccess))
    }

    override fun onSaveToListFailure() {
        emitEffect(HomeScreenEffect.ShowError(message = stringProvider.addToListFailed))
    }

    override fun onCreateNewListClick() {
        TODO("Not yet implemented")
    }

    override fun onRetryClick() {
        fetchPopularMediaData()
        fetchTopRatedMediaData()
        fetchWatchedMediaData()
        fetchMovieGenres()
        fetchUpcomingMovies()
    }


    private fun onDataLoadError(e: Throwable) {
        if (e is NoNetworkException) {
            updateState { it.copy(isNoInternetConnection = true) }
            emitEffect(HomeScreenEffect.ShowError(message = stringProvider.noInternetConnectionError))
        } else {
            updateState { it.copy(isNoInternetConnection = false) }
            emitEffect(HomeScreenEffect.ShowError(message = stringProvider.somethingWentWrongError))
        }
    }

    fun createUpcomingMoviesPagingDataSource(
        genreId: Int?,
        onError: (Throwable) -> Unit = ::onDataLoadError
    ): PagingSource<Int, Movie> {
        return BasePagingSourceForHome(onError = onError) { page ->
            manageMovieUseCase.getUpcomingMovies(
                page = page,
                genreId = genreId
            )
        }
    }
}