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
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import repository.SavedListsStatusProvider
import service.VodStringProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.ManageTvShowUseCase
import usecase.custom_list.ManageSavedListItemsUseCase
import usecase.custom_list.ManageSavedListsUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase
import usecase.manageMovieUseCase.GetMovieGenresUseCase
import usecase.manageMovieUseCase.GetPopularMoviesUseCase
import usecase.manageMovieUseCase.GetTopRatedMoviesUseCase
import usecase.manageMovieUseCase.GetUpcomingMoviesUseCase
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getMovieGenresUseCase: GetMovieGenresUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val manageTvShowUseCase: ManageTvShowUseCase,
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val savedListsStatusProvider: SavedListsStatusProvider,
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
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
    }

    private fun MediaItem.withSaved(savedIds: Set<Int>) =
        copy(isSaved = savedIds.contains(id))

    fun updateUserLoggingStatus() {
        tryToCollect(
            block = { checkIfUserIsLoggedInUseCase.isLoggedIn() },
            onCollect = ::onCollectLoggedFlag,
            onError = ::onDataLoadError
        )
    }

    private fun onCollectLoggedFlag(isLogged: Boolean) {
        updateState { copy(userIsLoggedIn = isLogged, showBottomSheet = false) }
    }

    private fun fetchPopularMediaData() {
        updateState { copy(isLoadingPopular = true) }
        tryToExecute(
            block = ::loadPopularMediaOperation,
            onSuccess = ::onFetchPopularMediaSuccess,
            onError = ::onDataLoadError
        )
    }

    private suspend fun loadPopularMediaOperation(): List<MediaItem> {
        val popularMovies = getPopularMoviesUseCase(1).map { it.toState() }.take(5)
        val popularTvShows = manageTvShowUseCase.getPopularTvShows(1).map { it.toState() }.take(5)

        return (popularMovies + popularTvShows).sortedByDescending { media -> media.rating }
    }

    private fun onFetchPopularMediaSuccess(mediaList: List<MediaItem>) {
        updateState {
            copy(
                isLoadingPopular = false,
                isNoInternetConnection = false,
                popularMedia = mediaList
            )
        }
    }

    private fun fetchTopRatedMediaData() {
        updateState { copy(isLoadingTopRated = true) }
        tryToExecute(
            block = ::loadTopRatedMediaOperation,
            onSuccess = ::onFetchTopRatedMediaSuccess,
            onError = ::onDataLoadError,
        )
    }

    private suspend fun loadTopRatedMediaOperation(): List<MediaItem> {
        val topRatedMovies = getTopRatedMoviesUseCase(1, null)
            .map { it.toState() }.take(5)
        val topRatedTvShows = manageTvShowUseCase.getTopRatedTvShows(1, null)
            .map { it.toState() }.take(5)

        return (topRatedMovies + topRatedTvShows).sortedByDescending { it.rating }
    }

    private fun onFetchTopRatedMediaSuccess(mediaList: List<MediaItem>) {
        updateState {
            copy(
                isLoadingTopRated = false,
                isNoInternetConnection = false,
                topRatingMedia = mediaList
            )
        }
    }

    private fun fetchWatchedMediaData() {
        tryToCollect(
            block = ::loadWatchedMediaHistory,
            onCollect = ::onFetchWatchedMediaSuccess,
            onError = ::onDataLoadError
        )
    }

    private fun onFetchWatchedMediaSuccess(mediaList: List<MediaHistoryItem>) {
        updateState {
            copy(
                continueWatchingMedia = mediaList.map { it.toState() },
                isNoInternetConnection = false,
                isLoadingHistory = false
            )
        }
    }

    private fun fetchMovieGenres() {
        tryToExecute(
            block = ::fetchMovieGenresOperation,
            onSuccess = ::onFetchMovieGenresSuccess,
            onError = ::onDataLoadError,
        )
    }

    private suspend fun fetchMovieGenresOperation(): List<GenreUiState> {
        updateState { copy(isLoadingGenre = true) }
        return getMovieGenresUseCase().map { it.toState() }
    }

    private fun onFetchMovieGenresSuccess(genres: List<GenreUiState>) {
        updateState {
            copy(movieGenres = genres, isLoadingGenre = false, isNoInternetConnection = false)
        }
    }

    private fun fetchUpcomingMovies(genreId: Int? = null) {
        tryToCollect(
            block = {
                loadUpcomingMovies(genreId)
                    .combine(savedListsStatusProvider.savedIds) { pagingData, savedIds ->
                        pagingData.map { it.withSaved(savedIds) }
                    }
                    .cachedIn(viewModelScope)
            },
            onCollect = ::onFetchUpcomingMoviesSuccess,
            onError = ::onDataLoadError,
        )
    }


    private fun loadUpcomingMovies(
        genreId: Int?,
    ): Flow<PagingData<MediaItem>> {
        return createPagingFlow(
            pagingSourceFactory = { createUpcomingMoviesPagingDataSource(genreId = genreId) },
            mapper = Movie::toState
        )
    }

    private fun onFetchUpcomingMoviesSuccess(pagingData: PagingData<MediaItem>) {
        updateState { copy(upcomingMovies = flowOf(pagingData), isNoInternetConnection = false) }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadWatchedMediaHistory(): Flow<List<MediaHistoryItem>> {
        updateState { copy(isLoadingHistory = true) }

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
            updateState { copy(movieSelectedGenreId = id) }
            fetchUpcomingMovies(id)
        }
    }

    override fun onMediaClick(id: Int, mediaTypeUi: MediaTypeUi) {
        emitEffect(HomeScreenEffect.NavigateToMediaDetails(id, mediaTypeUi))
    }

    override fun onSaveIconClick(media: MediaItem) {
        if (state.value.userIsLoggedIn.not()) {
            updateState { copy(showBottomSheet = true) }
            return
        }

        if (media.isSaved) {
            tryToExecute(
                block = {
                    val userLists = manageSavedListsUseCase.getSavedLists()
                    val defaultList = userLists.firstOrNull()
                    if (defaultList != null) {
                        manageSavedListItemsUseCase.removeMovieFromSavedList(
                            defaultList.id,
                            media.id
                        )
                        savedListsStatusProvider.refreshLists()
                    }
                },
                onSuccess = {
                    savedListsStatusProvider.markItemUnsaved(media.id)
                    emitEffect(HomeScreenEffect.ShowSuccess(message = "Removed from list."))
                },
                onError = {
                    savedListsStatusProvider.markItemSaved(media.id)
                    emitEffect(HomeScreenEffect.ShowError(message = "Failed to remove from list."))
                }
            )
        } else {
            updateState {
                copy(
                    showSaveToListBottomSheet = true,
                    selectedMediaToSave = media
                )
            }
        }
    }

    override fun onDismissBottomSheet() {
        updateState { copy(showBottomSheet = false) }
    }

    override fun onDismissSaveToListBottomSheet() {
        updateState { copy(showSaveToListBottomSheet = false) }
    }

    override fun onSaveToListSuccess() {
        emitEffect(HomeScreenEffect.ShowSuccess(message = stringProvider.addToListSuccess))
    }

    override fun onSaveToListFailure() {
        emitEffect(HomeScreenEffect.ShowError(message = stringProvider.addToListFailed))
    }

    override fun onCreateNewListClick() {
        updateState { copy(showSaveToListBottomSheet = false, showAddListBottomSheet = true) }
    }

    override fun onDismissAddListBottomSheet() {
        updateState { copy(showAddListBottomSheet = false) }
    }

    override fun onRetryClick() {
        fetchPopularMediaData()
        fetchTopRatedMediaData()
        fetchWatchedMediaData()
        fetchMovieGenres()
        fetchUpcomingMovies()
    }

    private fun onDataLoadError(e: NovixAppException) {
        if (e is NoNetworkException) {
            updateState { copy(isNoInternetConnection = true) }
            emitEffect(HomeScreenEffect.ShowError(message = stringProvider.noInternetConnectionError))
        } else {
            updateState { copy(isNoInternetConnection = false) }
            emitEffect(HomeScreenEffect.ShowError(message = stringProvider.somethingWentWrongError))
        }
    }

    fun createUpcomingMoviesPagingDataSource(
        genreId: Int?,
        onError: (NovixAppException) -> Unit = ::onDataLoadError,
    ): PagingSource<Int, Movie> {
        return BasePagingSourceForHome(onError = onError) { page ->
            getUpcomingMoviesUseCase(
                page = page,
                genreId = genreId
            )
        }
    }
}