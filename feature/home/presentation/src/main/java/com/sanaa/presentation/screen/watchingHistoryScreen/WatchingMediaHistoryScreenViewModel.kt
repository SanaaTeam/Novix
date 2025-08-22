package com.sanaa.presentation.screen.watchingHistoryScreen

import com.sanaa.presentation.components.SnackData
import com.sanaa.presentation.homeBase.BaseViewModel
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItemUiState
import com.sanaa.presentation.state.MediaTypeUiState
import com.sanaa.presentation.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.MediaHistoryItem
import exceptions.NoLoggedInUserException
import exceptions.NoNetworkException
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import service.VodStringProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.ManageMovieUseCase
import usecase.ManageTvShowUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase
import usecase.search.search_param.MediaType
import javax.inject.Inject

@HiltViewModel
class WatchingMediaHistoryScreenViewModel @Inject constructor(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val manageTvShowUseCase: ManageTvShowUseCase,
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val stringProvider: VodStringProvider,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<WatchingMediaHistoryScreenUiState, WatchingMediaHistoryScreenEffect>(
    WatchingMediaHistoryScreenUiState(),
    dispatcher
), WatchingMediaHistoryScreenInteractionListener {

    init {
        updateUserLoggingStatus()
        fetchMovies()
        fetchTvShows()
        fetchMovieGenres()
        fetchTvShowGenres()
    }

    fun updateUserLoggingStatus() {
        tryToCollect(
            block = { checkIfUserIsLoggedInUseCase.isLoggedIn() },
            onCollect = ::onCollectLoggedFlag,
        )
        onDismissLoginBottomSheet()
    }

    private fun onCollectLoggedFlag(isLogged: Boolean) {
        if (isLogged && state.value.showLoginBottomSheet) {
            updateState {
                copy(
                    showLoginBottomSheet = false,
                    showSaveToListBottomSheet = true,
                )
            }
        }
        updateState { copy(userIsLoggedIn = isLogged) }
    }

    private fun fetchMovies(genreId: Int? = null) {
        tryToCollect(
            block = {
                loadMediaHistory(mediaType = MediaType.MOVIE, genreId = genreId)
            },
            onCollect = { mediaList -> onFetchMoviesSuccess(mediaList.map { it.toState() }) },
            onError = ::onDataLoadError
        )
    }

    private fun onFetchMoviesSuccess(mediaList: List<MediaItemUiState>) {
        updateState { copy(movieList = mediaList, isLoading = false, showRefreshButton = false) }
    }

    private fun fetchTvShows(genreId: Int? = null) {
        tryToCollect(
            block = { loadMediaHistory(mediaType = MediaType.TV_SHOW, genreId = genreId) },
            onCollect = ::onFetchTvShowsSuccess,
            onError = ::onDataLoadError
        )
    }

    private fun onFetchTvShowsSuccess(mediaList: List<MediaHistoryItem>) {
        updateState {
            copy(
                tvShowList = mediaList.map { it.toState() },
                isLoading = false,
                showRefreshButton = false
            )
        }
    }

    private fun fetchMovieGenres() {
        tryToExecute(
            onStart = { updateState { copy(isLoading = true) } },
            block = { manageMovieUseCase.getMovieGenres().map { it.toState() } },
            onSuccess = ::onFetchMovieGenresSuccess,
            onError = ::onDataLoadError
        )
    }

    private fun onFetchMovieGenresSuccess(genres: List<GenreUiState>) {
        updateState {
            copy(
                movieGenres = genres,
                isLoading = false,
                showRefreshButton = false
            )
        }
    }

    private fun fetchTvShowGenres() {
        tryToExecute(
            onStart = { updateState { copy(isLoading = true) } },
            block = { manageTvShowUseCase.getTvShowGenres().map { it.toState() } },
            onSuccess = ::onFetchTvShowGenresSuccess,
            onError = ::onDataLoadError
        )
    }

    private fun onFetchTvShowGenresSuccess(genres: List<GenreUiState>) {
        updateState {
            copy(
                tvShowGenres = genres,
                isLoading = false,
                showRefreshButton = false
            )
        }
    }

    override fun onMediaTabSelection(mediaTypeUiState: MediaTypeUiState) {
        updateState { copy(selectedMediaTypeUiState = mediaTypeUiState) }
    }

    override fun onMovieGenreClick(id: Int?) {
        if (id == state.value.movieSelectedGenreId) return

        updateState { copy(movieSelectedGenreId = id) }
        fetchMovies(id)
    }

    override fun onTvShowGenreClick(id: Int?) {
        if (id == state.value.tvShowSelectedGenreId) return

        updateState { copy(tvShowSelectedGenreId = id) }
        fetchTvShows(id)
    }

    override fun onMediaClick(id: Int, mediaTypeUiState: MediaTypeUiState) {
        emitEffect(WatchingMediaHistoryScreenEffect.NavigateToMediaDetails(id, mediaTypeUiState))
    }

    override fun onSaveIconClick(media: MediaItemUiState) {
        if (!state.value.userIsLoggedIn) {
            updateState { copy(showLoginBottomSheet = true, selectedMediaToSaveId = media.id) }
            return
        }

        updateState {
            copy(
                showSaveToListBottomSheet = true,
                selectedMediaToSaveId = media.id
            )
        }
    }

    override fun onDismissSaveToListBottomSheet() {
        updateState { copy(showSaveToListBottomSheet = false, selectedMediaToSaveId = null) }
    }

    override fun onCreateNewListClick() {
        updateState { copy(showSaveToListBottomSheet = false, showAddListBottomSheet = true) }
    }

    override fun onDismissAddListBottomSheet() {
        updateState { copy(showAddListBottomSheet = false) }
    }

    override fun onLoginButtonClick() {
        emitEffect(WatchingMediaHistoryScreenEffect.NavigateToLogin)
    }

    override fun onDismissLoginBottomSheet() {
        updateState { copy(showLoginBottomSheet = false) }
    }

    override fun onBackClick() {
        emitEffect(WatchingMediaHistoryScreenEffect.NavigateBack)
    }

    override fun onRetryClick() {
        fetchMovies()
        fetchTvShows()
        fetchMovieGenres()
        fetchTvShowGenres()
    }

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
    }

    private suspend fun loadMediaHistory(
        mediaType: MediaType,
        genreId: Int?
    ): Flow<List<MediaHistoryItem>> {
        updateState { copy(isLoading = true) }
        return try {
            return getLoggedInUserUseCase.getLoggedInUser().first().run {
                manageWatchedMediaHistoryUseCase.getMediaHistory(
                    genreId = genreId,
                    mediaType = mediaType,
                    username = username
                )
            }

        } catch (_: NoLoggedInUserException) {
            flowOf(emptyList())
        }
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
}