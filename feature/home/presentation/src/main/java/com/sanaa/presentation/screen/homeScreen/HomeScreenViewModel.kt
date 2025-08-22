package com.sanaa.presentation.screen.homeScreen

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.components.SnackData
import com.sanaa.presentation.homeBase.BasePagingSourceForHome
import com.sanaa.presentation.homeBase.BaseViewModel
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItemUiState
import com.sanaa.presentation.state.MediaTypeUiState
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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import service.VodStringProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.ManageMovieUseCase
import usecase.ManageTvShowUseCase
import usecase.MangeUserPreferenceUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val manageTvShowUseCase: ManageTvShowUseCase,
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val mangeUserPreference: MangeUserPreferenceUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<HomeScreenUiState, HomeScreenEffect>(
    initialState = HomeScreenUiState(),
    defaultDispatcher = dispatcher
), HomeScreenInteractionListener {

    init {
        updateUserLoggingStatus()
        onLanguageChanges()
        fetchPopularMediaData()
        fetchTopRatedMediaData()
        fetchUpcomingMovies()
    }

    private fun updateUserLoggingStatus() {
        tryToCollect(
            block = { checkIfUserIsLoggedInUseCase.isLoggedIn() },
            onCollect = ::onCollectLoggedFlag,
            onError = ::onDataLoadError
        )
    }

    private fun onLanguageChanges(){
        tryToCollect(
            block = { mangeUserPreference.getLanguage() },
            onCollect = { fetchMovieGenres(freshData = true) },
            onError = ::onDataLoadError
        )
    }

    private fun onCollectLoggedFlag(isLogged: Boolean) {
        if (isLogged != state.value.userIsLoggedIn) {
            fetchWatchedMediaData()
            updateState { copy(userIsLoggedIn = isLogged) }
        }
        if (isLogged && state.value.showLoginBottomSheet) {
            updateState {
                copy(
                    showLoginBottomSheet = false,
                    showSaveToListBottomSheet = true,
                )
            }
        }
    }

    private fun fetchPopularMediaData() {
        tryToExecute(
            onStart = { updateState { copy(isLoadingPopular = true) } },
            block = ::loadPopularMediaOperation,
            onSuccess = ::onFetchPopularMediaSuccess,
            onError = ::onDataLoadError
        )
    }

    private suspend fun loadPopularMediaOperation(): List<MediaItemUiState> {
        val popularMovies = manageMovieUseCase.getPopularMovies(1).map { it.toState() }.take(5)
        val popularTvShows = manageTvShowUseCase.getPopularTvShows(1).map { it.toState() }.take(5)

        return (popularMovies + popularTvShows).sortedByDescending { media -> media.rating }
    }

    private fun onFetchPopularMediaSuccess(mediaList: List<MediaItemUiState>) {
        updateState {
            copy(
                isLoadingPopular = false,
                isNoInternetConnection = false,
                popularMedia = mediaList
            )
        }
    }

    private fun fetchTopRatedMediaData() {
        tryToExecute(
            onStart = { updateState { copy(isLoadingTopRated = true) } },
            block = ::loadTopRatedMediaOperation,
            onSuccess = ::onFetchTopRatedMediaSuccess,
            onError = ::onDataLoadError,
        )
    }

    private suspend fun loadTopRatedMediaOperation(): List<MediaItemUiState> {
        val topRatedMovies = manageMovieUseCase.getTopRatedMovies(1, null)
            .map { it.toState() }.take(5)
        val topRatedTvShows = manageTvShowUseCase.getTopRatedTvShows(1, null)
            .map { it.toState() }.take(5)

        return (topRatedMovies + topRatedTvShows).sortedByDescending { it.rating }
    }

    private fun onFetchTopRatedMediaSuccess(mediaList: List<MediaItemUiState>) {
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
            onStart = { updateState { copy(isLoadingHistory = true) } },
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

    private fun fetchMovieGenres(freshData: Boolean = false) {
        tryToExecute(
            onStart = { updateState { copy(isLoadingGenre = true) } },
            block = { manageMovieUseCase.getMovieGenres(freshData).map { it.toState() } },
            onSuccess = ::onFetchMovieGenresSuccess,
            onError = ::onDataLoadError,
        )
    }

    private fun onFetchMovieGenresSuccess(genres: List<GenreUiState>) {
        updateState {
            copy(movieGenres = genres, isLoadingGenre = false, isNoInternetConnection = false)
        }
    }

    private fun fetchUpcomingMovies(genreId: Int? = null) {
        tryToCollect(
            block = { loadUpcomingMovies(genreId) },
            onCollect = ::onFetchUpcomingMoviesSuccess,
            onError = ::onDataLoadError,
        )
    }

    private fun loadUpcomingMovies(
        genreId: Int?,
    ): Flow<PagingData<MediaItemUiState>> {
        return createPagingFlow(
            pagingSourceFactory = { createUpcomingMoviesPagingDataSource(genreId = genreId) },
            mapper = Movie::toState
        )
    }

    private fun onFetchUpcomingMoviesSuccess(pagingData: PagingData<MediaItemUiState>) {
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
            }.catch { e ->
                emit(emptyList())
            }
    }

    override fun onMoviesCardClick() {
        emitEffect(HomeScreenEffect.NavigateToTrendingMoviesScreen)
    }

    override fun onTvShowsCardClick() {
        emitEffect(HomeScreenEffect.NavigateToTrendingTvShowsScreen)
    }

    override fun onPeopleCardClick() {
        emitEffect(HomeScreenEffect.NavigateToTrendingPeopleScreen)
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

    override fun onMediaClick(id: Int, mediaTypeUiState: MediaTypeUiState) {
        emitEffect(HomeScreenEffect.NavigateToMediaDetails(id, mediaTypeUiState))
    }

    override fun onSaveIconClick(media: MediaItemUiState) {
        if (state.value.userIsLoggedIn.not()) {
            updateState { copy(showLoginBottomSheet = true, selectedMediaToSaveId = media.id) }
            return
        }

        updateState { copy(showSaveToListBottomSheet = true, selectedMediaToSaveId = media.id)        }
    }

    override fun onDismissLoginBottomSheet() {
        updateState { copy(showLoginBottomSheet = false) }
    }

    override fun onDismissSaveToListBottomSheet() {
        updateState { copy(showSaveToListBottomSheet = false) }
    }

    override fun onSaveToListSuccess() {
        updateState {
            copy(
                snackBarData = SnackData(message = stringProvider.addToListSuccess, isError = false),
                showSaveToListBottomSheet = false
            )
        }
    }

    override fun onSaveToListFailure() {
        updateState {
            copy(snackBarData = SnackData(message = stringProvider.addToListFailed, isError = true))
        }
    }

    override fun onCreateNewListClick() {
        updateState { copy(showSaveToListBottomSheet = false, showAddListBottomSheet = true) }
    }

    override fun onDismissAddListBottomSheet() {
        updateState { copy(showAddListBottomSheet = false) }
    }

    override fun onLoginButtonClick() {
        emitEffect(HomeScreenEffect.NavigateToLogin)
    }

    override fun onRetryClick() {
        fetchPopularMediaData()
        fetchTopRatedMediaData()
        fetchWatchedMediaData()
        fetchMovieGenres()
        fetchUpcomingMovies()
    }

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
    }

    private fun onDataLoadError(e: NovixAppException) {
        when (e) {
            is NoNetworkException -> {
                updateState {
                    copy(
                        isNoInternetConnection = true,
                        snackBarData =
                            SnackData(
                                message = stringProvider.noInternetConnectionError,
                                isError = true
                            )
                    )
                }
            }

            else -> {
                updateState {
                    copy(
                        isNoInternetConnection = false,
                        snackBarData =
                            SnackData(
                                message = stringProvider.somethingWentWrongError,
                                isError = true
                            )
                    )
                }
            }
        }
    }

    fun createUpcomingMoviesPagingDataSource(
        genreId: Int?,
        onError: (NovixAppException) -> Unit = ::onDataLoadError,
    ): PagingSource<Int, Movie> {
        return BasePagingSourceForHome(onError = onError) { page ->
            manageMovieUseCase.getUpcomingMovies(
                page = page,
                genreId = genreId
            )
        }
    }
}