package com.sanaa.presentation.screen.mediaTabScreen.continueWatchingScreen

import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.screen.mediaTabScreen.MediaTabScreenEffect
import com.sanaa.presentation.screen.mediaTabScreen.MediaTabScreenInteractionListener
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaTypeUi
import com.sanaa.presentation.state.mapper.toState
import entity.MediaHistoryItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase
import usecase.search.search_param.MediaType

class ContinueWatchingMediaScreenViewModel(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<ContinueWatchingMediaScreenUiState, MediaTabScreenEffect>(
    ContinueWatchingMediaScreenUiState(),
    dispatcher
), MediaTabScreenInteractionListener {

    init {
        fetchMovieGenres()
        fetchTvShowGenres()
        fetchMovies()
        fetchTvShows()
    }

    private fun fetchMovies(genreId: Int? = null) {
        tryToCollect(
            callee = {
                loadTopRatedMovies(genreId = genreId)
            }, onCollect = { mediaList ->
                updateState {
                    it.copy(movieList = mediaList.map { it.toState() }, isLoading = false)
                }
            },
            onError = { exception ->
                updateState {
                    it.copy(error = exception.message, isLoading = false)
                }
            }
        )
    }

    private fun fetchTvShows(genreId: Int? = null) {
        tryToCollect(
            callee = {
                loadTopRatedTvSeries(genreId = genreId)
            }, onCollect = { mediaList ->
                updateState {
                    it.copy(tvShowList = mediaList.map { it.toState() }, isLoading = false)
                }
            },
            onError = { exception ->
                updateState {
                    it.copy(error = exception.message, isLoading = false)
                }
            }
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
            onError = { exception ->
                updateState {
                    it.copy(error = exception.message, isLoading = false)
                }
            }
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
            onError = { exception ->
                updateState {
                    it.copy(error = exception.message, isLoading = false)
                }
            }
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
        emitEffect(MediaTabScreenEffect.NavigateToMediaDetails(id, mediaTypeUi))
    }

    override fun onSaveIconClick(media: MediaItem) {
        updateState {
            it.copy(
                showBottomSheet = true
            )
        }
    }

    override fun onBackClick() {
        emitEffect(MediaTabScreenEffect.NavigateBack)
    }


    private suspend fun loadTopRatedMovies(
        genreId: Int?
    ): Flow<List<MediaHistoryItem>> {
        updateState { it.copy(isLoading = true) }
        return manageWatchedMediaHistoryUseCase.getMediaHistory(
            genreId = genreId,
            mediaType = MediaType.MOVIE,
            username = "sanaa"
        )
    }

    private suspend fun loadTopRatedTvSeries(
        genreId: Int?
    ): Flow<List<MediaHistoryItem>> {
        updateState { it.copy(isLoading = true) }
        return manageWatchedMediaHistoryUseCase.getMediaHistory(
            genreId = genreId,
            mediaType = MediaType.TV_SERIES,
            username = "sanaa"
        )
    }

}