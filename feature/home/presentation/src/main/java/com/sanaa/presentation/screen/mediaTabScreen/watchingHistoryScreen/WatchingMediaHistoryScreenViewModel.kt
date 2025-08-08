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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import service.VodStringProvider
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
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<WatchingMediaHistoryScreenUiState, WatchingMediaHistoryScreenEffect>(
    WatchingMediaHistoryScreenUiState(),
    dispatcher
), WatchingMediaHistoryScreenInteractionListener {

    init {
        fetchMovies()
        fetchTvShows()
        fetchMovieGenres()
        fetchTvShowGenres()
    }

    private fun fetchMovies(genreId: Int? = null) {
        tryToCollect(
            callee = { loadMediaHistory(mediaType = MediaType.MOVIE, genreId = genreId) },
            onCollect = ::onFetchMoviesSuccess,
            onError = ::onDataLoadError
        )
    }

    private fun onFetchMoviesSuccess(mediaList: List<MediaHistoryItem>) {
        updateState {
            it.copy(
                movieList = mediaList.map { it.toState() },
                isLoading = false,
                showRefreshButton = false
            )
        }
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
            it.copy(
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
            it.copy(isLoading = true)
        }
        return manageMovieUseCase.getMovieGenres()
    }

    private fun onFetchMovieGenresSuccess(genres: List<Genre>) {
        updateState {
            it.copy(
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
            it.copy(isLoading = true)
        }
        return manageTvSeriesUseCase.getSeriesGenres()
    }

    private fun onFetchTvShowGenresSuccess(genres: List<Genre>) {
        updateState {
            it.copy(
                tvShowGenres = genres.map { it.toState() },
                isLoading = false,
                showRefreshButton = false
            )
        }
    }

    override fun onMediaTabSelection(mediaTypeUi: MediaTypeUi) {
        updateState { it.copy(selectedMediaTypeUi = mediaTypeUi) }
    }

    override fun onMovieGenreClick(id: Int?) {
        if (id == state.value.movieSelectedGenreId) return

        updateState { it.copy(movieSelectedGenreId = id) }
        fetchMovies(id)
    }

    override fun onTvShowGenreClick(id: Int?) {
        if (id == state.value.tvShowSelectedGenreId) return

        updateState { it.copy(tvShowSelectedGenreId = id) }
        fetchTvShows(id)
    }

    override fun onMediaClick(id: Int, mediaTypeUi: MediaTypeUi) {
        emitEffect(WatchingMediaHistoryScreenEffect.NavigateToMediaDetails(id, mediaTypeUi))
    }

    override fun onSaveIconClick(media: MediaItem) {
        updateState {
            it.copy(
                showBottomSheet = true
            )
        }
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
        updateState { it.copy(isLoading = true) }
      return try {
            return getLoggedInUserUseCase.getLoggedInUser().first().run {
                 manageWatchedMediaHistoryUseCase.getMediaHistory(
                    genreId = genreId,
                    mediaType = mediaType,
                    username = username
                )
            }

        } catch (_: NoLoggedInUserException){
            flowOf(emptyList())
        }
    }

    private fun onDataLoadError(e: Throwable) {
        if (e is NoNetworkException) {
            updateState { it.copy(isNoInternetConnection = true, showRefreshButton = true) }
            emitEffect(WatchingMediaHistoryScreenEffect.ShowError(message = stringProvider.noInternetConnectionError))
        } else {
            updateState { it.copy(isNoInternetConnection = false, showRefreshButton = true) }
            emitEffect(WatchingMediaHistoryScreenEffect.ShowError(message = stringProvider.somethingWentWrongError))
        }
    }
}