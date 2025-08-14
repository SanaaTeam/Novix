package com.sanaa.presentation.screen.mediaTabScreen.watchingHistoryScreen

import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaTypeUi
import com.sanaa.presentation.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Genre
import entity.MediaHistoryItem
import exceptions.NoLoggedInUserException
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import repository.SavedListsStatusProvider
import service.VodStringProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase
import usecase.search.search_param.MediaType
import javax.inject.Inject

@HiltViewModel
class WatchingMediaHistoryScreenViewModel @Inject constructor(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val stringProvider: VodStringProvider,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val savedListsStatusProvider: SavedListsStatusProvider,
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
            callee = { checkIfUserIsLoggedInUseCase.isLoggedIn() },
            onCollect = ::onCollectLoggedFlag,
        )
        onDismissLoginBottomSheet()
    }

    private fun onCollectLoggedFlag(isLogged: Boolean) {
        updateState { copy(userIsLoggedIn = isLogged) }
    }

    private fun fetchMovies(genreId: Int? = null) {
        tryToCollect(
            callee = {
                loadMediaHistory(mediaType = MediaType.MOVIE, genreId = genreId)
                    .combine(savedListsStatusProvider.savedIds) { mediaList, savedIds ->
                        mediaList.map { media ->
                            media.copy(isSaved = savedIds.contains(media.id))
                        }
                    }
            },
            onCollect = { mediaList -> onFetchMoviesSuccess(mediaList.map { it.toState() }) },
            onError = ::onDataLoadError
        )
    }

    private fun onFetchMoviesSuccess(mediaList: List<MediaItem>) {
        updateState { copy(movieList = mediaList, isLoading = false, showRefreshButton = false) }
    }

    private fun fetchTvShows(genreId: Int? = null) {
        tryToCollect(
            callee = { loadMediaHistory(mediaType = MediaType.TV_SERIES, genreId = genreId) },
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
            callee = ::fetchMovieGenresOperation,
            onSuccess = ::onFetchMovieGenresSuccess,
            onError = ::onDataLoadError
        )
    }

    private suspend fun fetchMovieGenresOperation(): List<Genre> {
        updateState {
            copy(isLoading = true)
        }
        return manageMovieUseCase.getMovieGenres()
    }

    private fun onFetchMovieGenresSuccess(genres: List<Genre>) {
        updateState {
            copy(
                movieGenres = genres.map { it.toState() },
                isLoading = false,
                showRefreshButton = false
            )
        }
    }

    private fun fetchTvShowGenres() {
        tryToExecute(
            callee = ::fetchTvShowGenresOperation,
            onSuccess = ::onFetchTvShowGenresSuccess,
            onError = ::onDataLoadError
        )
    }

    private suspend fun fetchTvShowGenresOperation(): List<Genre> {
        updateState {
            copy(isLoading = true)
        }
        return manageTvSeriesUseCase.getSeriesGenres()
    }

    private fun onFetchTvShowGenresSuccess(genres: List<Genre>) {
        updateState {
            copy(
                tvShowGenres = genres.map { it.toState() },
                isLoading = false,
                showRefreshButton = false
            )
        }
    }

    override fun onMediaTabSelection(mediaTypeUi: MediaTypeUi) {
        updateState { copy(selectedMediaTypeUi = mediaTypeUi) }
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

    override fun onMediaClick(id: Int, mediaTypeUi: MediaTypeUi) {
        emitEffect(WatchingMediaHistoryScreenEffect.NavigateToMediaDetails(id, mediaTypeUi))
    }

    override fun onSaveIconClick(media: MediaItem) {
        if (!state.value.userIsLoggedIn) {
            updateState { copy(showLoginBottomSheet = true) }
            return
        }

        if (media.isSaved) {
            savedListsStatusProvider.markItemUnsaved(media.id)
        } else {
            updateState {
                copy(
                    showSaveToListBottomSheet = true,
                    selectedMediaToSave = media
                )
            }
        }
    }

    override fun onDismissSaveToListBottomSheet() {
        updateState { copy(showSaveToListBottomSheet = false, selectedMediaToSave = null) }
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

    private fun onDataLoadError(e: Throwable) {
        if (e is NoNetworkException) {
            updateState { copy(isNoInternetConnection = true, showRefreshButton = true) }
            emitEffect(WatchingMediaHistoryScreenEffect.ShowError(message = stringProvider.noInternetConnectionError))
        } else {
            updateState { copy(isNoInternetConnection = false, showRefreshButton = true) }
            emitEffect(WatchingMediaHistoryScreenEffect.ShowError(message = stringProvider.somethingWentWrongError))
        }
    }
}