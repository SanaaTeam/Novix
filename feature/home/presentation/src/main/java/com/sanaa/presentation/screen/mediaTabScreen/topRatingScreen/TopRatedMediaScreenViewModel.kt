package com.sanaa.presentation.screen.mediaTabScreen.topRatingScreen

import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.screen.mediaTabScreen.MediaTabScreenEffect
import com.sanaa.presentation.screen.mediaTabScreen.MediaTabScreenInteractionListener
import com.sanaa.presentation.screen.mediaTabScreen.MediaTabScreenUiState
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaType
import com.sanaa.presentation.state.mapper.toState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase

class TopRatedMediaScreenViewModel(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<MediaTabScreenUiState, MediaTabScreenEffect>(
    MediaTabScreenUiState(),
    dispatcher
), MediaTabScreenInteractionListener {

    init {
        fetchMovieGenres()
        fetchTvShowGenres()
        fetchMovies()
    }

    private fun fetchMovies(genreId: Int? = null) {
        tryToExecute(
            callee = {
                updateState {
                    it.copy(isLoading = true, movieSelectedGenreId = genreId)
                }
                manageMovieUseCase.getTrendingMovies(1, state.value.movieSelectedGenreId)
                    .map { it.toState() }
            }, onSuccess = { mediaList ->
                updateState {
                    it.copy(movieList = mediaList, isLoading = false)
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
        tryToExecute(
            callee = {
                updateState {
                    it.copy(isLoading = true, tvShowSelectedGenreId = genreId)
                }
                manageTvSeriesUseCase.getTopRatedTvSeries(1, state.value.tvShowSelectedGenreId)
                    .map { it.toState() }
            }, onSuccess = { mediaList ->
                updateState {
                    it.copy(tvShowList = mediaList, isLoading = false)
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

    override fun onMediaTabSelection(mediaType: MediaType) {
        updateState { it.copy(selectedMediaType = mediaType) }
    }

    override fun onMovieGenreClick(id: Int?) {
        if (id != state.value.movieSelectedGenreId) {
            fetchMovies(id)
        }
    }

    override fun onTvShowGenreClick(id: Int?) {
        if (id != state.value.tvShowSelectedGenreId) {
            fetchTvShows(id)
        }
    }

    override fun onMediaClick(id: Int, mediaType: MediaType) {
        emitEffect(MediaTabScreenEffect.NavigateToMediaDetails(id, mediaType))
    }

    override fun onSaveIconClick(media: MediaItem) {
        TODO("Not yet implemented")
    }

    override fun onBackClick() {
        emitEffect(MediaTabScreenEffect.NavigateBack)
    }
}