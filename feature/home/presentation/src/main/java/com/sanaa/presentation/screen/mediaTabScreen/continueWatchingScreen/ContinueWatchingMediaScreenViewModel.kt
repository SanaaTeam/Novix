package com.sanaa.presentation.screen.mediaTabScreen.continueWatchingScreen

import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaTypeUi
import com.sanaa.presentation.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.MediaHistoryItem
import exceptions.NoLoggedInUserException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import usecase.GetLoggedInUserUseCase
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase
import usecase.search.search_param.MediaType
import javax.inject.Inject

@HiltViewModel
class ContinueWatchingMediaScreenViewModel @Inject constructor(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<ContinueWatchingMediaScreenUiState, ContinueWatchingScreenEffect>(
    ContinueWatchingMediaScreenUiState(),
    dispatcher
), ContinueWatchingScreenInteractionListener {

    init {
        fetchMovieGenres()
        fetchTvShowGenres()
        fetchMovies()
        fetchTvShows()
    }

    private fun fetchMovies(genreId: Int? = null) {
        tryToCollect(
            callee = {
                loadWatchedHistoryMovies(genreId = genreId)
            }, onCollect = { mediaList ->
                updateState {
                    it.copy(movieList = mediaList.map { it.toState() }, isLoading = false)
                }
            },
            onError = { ::onLoadDataError }
        )
    }

    private fun fetchTvShows(genreId: Int? = null) {
        tryToCollect(
            callee = {
                loadWatchedHistoryTvSeries(genreId = genreId)
            }, onCollect = { mediaList ->
                updateState {
                    it.copy(tvShowList = mediaList.map { it.toState() }, isLoading = false)
                }
            },
            onError = { ::onLoadDataError }
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
            onError = { ::onLoadDataError }

        )
    }

    private fun fetchTvShowGenres() {
        tryToExecute(
            callee = {
                updateState {
                    it.copy(isLoading = true)
                }
                manageTvSeriesUseCase.getSeriesGenres().map { it.toState() }
            },
            onSuccess = { genres ->
                updateState {
                    it.copy(tvShowGenres = genres, isLoading = false)
                }
            },
            onError = { ::onLoadDataError }
        )
    }

    override fun onMediaTabSelection(mediaTypeUi: MediaTypeUi) {
        updateState { it.copy(selectedMediaTypeUi = mediaTypeUi) }
    }

    override fun onMovieGenreClick(id: Int?) {
        if (id != state.value.movieSelectedGenreId) {
            updateState { it.copy(movieSelectedGenreId = id) }
            fetchMovies(id)
        }
    }

    override fun onTvShowGenreClick(id: Int?) {
        if (id != state.value.tvShowSelectedGenreId) {
            updateState { it.copy(tvShowSelectedGenreId = id) }
            fetchTvShows(id)
        }
    }

    override fun onMediaClick(id: Int, mediaTypeUi: MediaTypeUi) {
        emitEffect(ContinueWatchingScreenEffect.NavigateToMediaDetails(id, mediaTypeUi))
    }

    override fun onSaveIconClick(media: MediaItem) {
        updateState {
            it.copy(
                showBottomSheet = true
            )
        }
    }

    override fun onBackClick() {
        emitEffect(ContinueWatchingScreenEffect.NavigateBack)
    }


    private suspend fun loadWatchedHistoryMovies(
        genreId: Int?
    ): Flow<List<MediaHistoryItem>> {
        updateState { it.copy(isLoading = true) }
        val user = try {
            getLoggedInUserUseCase.getLoggedInUser()
        } catch (_: NoLoggedInUserException) {
            null
        }
        if (user == null) return flowOf(emptyList())
        return manageWatchedMediaHistoryUseCase.getMediaHistory(
            genreId = genreId,
            mediaType = MediaType.MOVIE,
            username = user.username
        )
    }

    private fun onLoadDataError(exception: Throwable) {
        updateState {
            it.copy(
                error = exception.message,
                isLoading = false
            )
        }
    }

    private suspend fun loadWatchedHistoryTvSeries(
        genreId: Int?
    ): Flow<List<MediaHistoryItem>> {
        updateState { it.copy(isLoading = true) }
        val user = try {
            getLoggedInUserUseCase.getLoggedInUser()
        } catch (_: NoLoggedInUserException) {
            null
        }
        if (user == null) return flowOf(emptyList())

        return manageWatchedMediaHistoryUseCase.getMediaHistory(
            genreId = genreId,
            mediaType = MediaType.TV_SERIES,
            username = user.username
        )
    }

}