package com.sanaa.presentation.screen.watchingHistoryScreen

import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.components.SnackData
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItemUiState
import com.sanaa.presentation.state.MediaTypeUiState
import com.sanaa.presentation.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
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
            block = { loadMediaHistory(mediaType = MediaType.MOVIE, genreId = genreId) },
            onCollect = ::onFetchMoviesSuccess,
            onError = ::onDataLoadError
        )
    }

    private fun onFetchMoviesSuccess(mediaList: List<MediaHistoryItem>) {
        updateState {
            copy(
                movieList = mediaList.map { it.toState() },
                isLoading = false,
                showRefreshButton = false
            )
        }
    }

    private fun fetchTvShows(genreId: Int? = null) {
        tryToCollect(
            block = { loadMediaHistory(mediaType = MediaType.TV_SERIES, genreId = genreId) },
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
            block = { manageTvSeriesUseCase.getSeriesGenres().map { it.toState() } },
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
        updateState { copy(showBottomSheet = true) }
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

    private fun onDataLoadError(e: Throwable) {
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