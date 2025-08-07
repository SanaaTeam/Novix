package com.sanaa.presentation.screen.homeScreen
import androidx.lifecycle.viewModelScope

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.map
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.base.BasePagingSourceForHome
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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import repository.SavedMovieStatusProvider
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
                        userIsLoggedIn = isLogged,
                        showBottomSheet = false

                    )
                }
            },
        )
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



    private fun fetchUpcomingMovies(genreId: Int? = null) {
        tryToExecute(
            callee = {
                loadUpcomingMovies(genreId)                      // Flow<PagingData<MediaItem>>
                    .combine(savedMovieStatusProvider.savedIds) { pagingData, savedIds ->
                        pagingData.map { it.withSaved(savedIds) } // PagingData معدَّلة
                    }
                    .cachedIn(viewModelScope)                    // اختيارى: يَحفظ الـPaging فى الكاش
            },
            onSuccess = { flowWithSaved ->
                updateState { it.copy(upcomingMovies = flowWithSaved) }
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
        if (state.value.userIsLoggedIn) {
            updateState {
                it.copy(
                    showSaveToListBottomSheet = true,
                    selectedMediaId = media.id.toLong()
                )
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

    override fun onCreateNewListClick() {
        TODO("Not yet implemented")
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

    fun createUpcomingMoviesPagingDataSource(
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