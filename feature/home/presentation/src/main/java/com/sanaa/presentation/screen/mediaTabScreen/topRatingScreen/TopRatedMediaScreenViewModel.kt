package com.sanaa.presentation.screen.mediaTabScreen.topRatingScreen

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.base.BasePagingSourceForHome
import com.sanaa.presentation.screen.mediaTabScreen.MediaTabScreenEffect
import com.sanaa.presentation.screen.mediaTabScreen.MediaTabScreenInteractionListener
import com.sanaa.presentation.screen.mediaTabScreen.MediaTabScreenUiState
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaType
import com.sanaa.presentation.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import entity.Movie
import entity.TvSeries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import javax.inject.Inject

@HiltViewModel
class TopRatedMediaScreenViewModel @Inject constructor(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<MediaTabScreenUiState, MediaTabScreenEffect>(
    initialState = MediaTabScreenUiState(),
    defaultDispatcher = dispatcher
), MediaTabScreenInteractionListener {

    init {
        fetchMovieGenres()
        fetchTvShowGenres()
        fetchMovies()
        fetchTvShows()
    }

    private fun fetchMovies(genreId: Int? = null) {
        tryToExecute(
            callee = {
                loadTopRatedMovies(genreId = genreId)
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
                loadTopRatedTvSeries(genreId = genreId)
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

    override fun onMediaClick(id: Int, mediaType: MediaType) {
        emitEffect(MediaTabScreenEffect.NavigateToMediaDetails(id, mediaType))
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


    private fun loadTopRatedMovies(
        genreId: Int?
    ): Flow<PagingData<MediaItem>> {
        updateState { it.copy(isLoading = true) }
        return createPagingFlow(
            pagingSourceFactory = {
                createMoviePagingDataSource(
                    genreId = genreId
                )
            },
            mapper = Movie::toState
        )
    }

    private fun loadTopRatedTvSeries(
        genreId: Int?
    ): Flow<PagingData<MediaItem>> {
        updateState { it.copy(isLoading = true) }
        return createPagingFlow(
            pagingSourceFactory = {
                createTvShowPagingDataSource(
                    genreId = genreId
                )
            },
            mapper = TvSeries::toState
        )
    }


    private fun createMoviePagingDataSource(
        genreId: Int?
    ): PagingSource<Int, Movie> {
        return BasePagingSourceForHome { page ->
            manageMovieUseCase.getTopRatedMovies(
                page = page,
                genreId = genreId
            )
        }
    }

    private fun createTvShowPagingDataSource(
        genreId: Int?
    ): PagingSource<Int, TvSeries> {
        return BasePagingSourceForHome { page ->
            manageTvSeriesUseCase.getTopRatedTvSeries(
                page = page,
                genreId = genreId
            )
        }
    }
}