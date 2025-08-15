package com.sanaa.tvapp.presentation.screens.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.map
import com.sanaa.tvapp.base.BasePagingSourceForHome
import com.sanaa.tvapp.base.BaseViewModel
import com.sanaa.tvapp.presentation.screens.HomeScreenInteractionListener
import com.sanaa.tvapp.state.MediaItem
import com.sanaa.tvapp.state.MediaTypeUi
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import repository.SavedListsStatusProvider
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
    private val savedMovieStatusProvider: SavedListsStatusProvider,
) : BaseViewModel<HomeScreenUiState, HomeScreenEffect>(
    initialState = HomeScreenUiState(),
    defaultDispatcher = dispatcher
), HomeScreenInteractionListener {

    init {
        updateUserLoggingStatus()
        fetchPopularMediaData()
        fetchTopRatedMovieData()
        fetchTopRatedTvShowData()
        fetchWatchedMediaData()
        fetchMovieGenres()
        fetchUpcomingMovies()

        viewModelScope.launch {
            savedMovieStatusProvider.savedIds.collect { savedIds ->
                updateState {
                    copy(
                        popularMedia = popularMedia.map { it.withSaved(savedIds) },
                        topRatingMovies = topRatingMovies.map { it.withSaved(savedIds) },
                        continueWatchingMovies = continueWatchingMovies.map {
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
            block = { checkIfUserIsLoggedInUseCase.isLoggedIn() },
            onCollect = { isLogged ->
                updateState {
                    copy(
                        userIsLoggedIn = isLogged,
                        showBottomSheet = false

                    )
                }
            },
        )
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
            onCollect = { watchedMediaList ->
                updateState {
                    copy(
                        isLoading = false,
                        errorMessage = null,
                        continueWatchingMovies = watchedMediaList.map { it.toState() }
                            .filter { it.mediaTypeUi == MediaTypeUi.MOVIE },
                        continueWatchingTvShows = watchedMediaList.map { it.toState() }
                            .filter { it.mediaTypeUi == MediaTypeUi.TV_SHOW }
                    )
                }
            },
            onError = ::onErrorLoadingData
        )
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
            block = {
                loadUpcomingMovies(genreId)                      // Flow<PagingData<MediaItem>>
                    .combine(savedMovieStatusProvider.savedIds) { pagingData, savedIds ->
                        pagingData.map { it.withSaved(savedIds) } // PagingData معدَّلة
                    }
                    .cachedIn(viewModelScope)                    // اختيارى: يَحفظ الـPaging فى الكاش
            },
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
            updateState { copy(movieSelectedGenreId = id) }
            fetchUpcomingMovies(id)
        }
    }

    override fun onMediaClick(id: Int, mediaTypeUi: MediaTypeUi) {
        emitEffect(HomeScreenEffect.NavigateToMediaDetails(id, mediaTypeUi))
    }

    override fun onSaveIconClick(media: MediaItem) {
        if (state.value.userIsLoggedIn) {
            if (media.isSaved) {
                savedMovieStatusProvider.markItemUnsaved(media.id)
            } else {
                updateState {
                    copy(
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
        updateState { copy(showBottomSheet = false) }
    }

    override fun onDismissSaveToListBottomSheet() {
        updateState { copy(showSaveToListBottomSheet = false) }
    }

    override fun onCreateNewListClick() {
        TODO("Not yet implemented")
    }

    override fun onRetryClick() {
        updateState { copy(isNoInternet = false) }
        fetchPopularMediaData()
        fetchTopRatedMovieData()
        fetchTopRatedTvShowData()
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
            is NoNetworkException -> updateState { copy(isNoInternet = true, isLoading = false) }
            else -> updateState { copy(errorMessage = e.message, isLoading = false) }
        }
    }
}