package com.sanaa.presentation.screen.trendingMediaScreen.trendingMoviesScreen

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.base.BasePagingSourceForHome
import com.sanaa.presentation.screen.trendingMediaScreen.MediaListScreenInteractionListener
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenEffect
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenUiState
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.mapper.toState
import entity.Movie
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import usecase.ManageMovieUseCase

class TrendingMoviesScreenViewModel(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<TrendingMediaScreenUiState, TrendingMediaScreenEffect>(
    TrendingMediaScreenUiState(),
    dispatcher
), MediaListScreenInteractionListener {

    init {
        fetchGenres()
        loadMovies()
    }

    private fun fetchGenres() {
        tryToExecute(
            callee = {
                updateState {
                    it.copy(isLoading = true)
                }
                manageMovieUseCase.getMovieGenres().map { it.toState() }
            },
            onSuccess = { genres ->
                updateState {
                    it.copy(genreList = genres, isLoading = false)
                }
            },
            onError = { exception ->
                updateState {
                    it.copy(error = exception.message, isLoading = false)
                }
            }
        )
    }

    override fun onGenreClick(id: Int?) {
        if (id != state.value.selectedGenreId) {
            updateState {
                it.copy(selectedGenreId = id)
            }
            loadMovies()
        }
    }

    override fun onMediaClick(id: Int) {
        emitEffect(TrendingMediaScreenEffect.NavigateToMediaDetails(id))
    }

    override fun onSaveIconClick(media: MediaItem) {
        updateState {
            it.copy(
                showBottomSheet = true
            )
        }
    }

    override fun onBackClick() {
        emitEffect(TrendingMediaScreenEffect.NavigateBack)
    }

    private fun loadMovies() {
        tryToCollect(
            callee = ::loadMoviesOperation,
            onCollect = ::onMoviesLoaded,
            onError = ::onDataLoadError
        )
    }

    private fun loadMoviesOperation(): Flow<PagingData<MediaItem>> {
        return createPagingFlow(
            pagingSourceFactory = { createMoviesPagingSource() },
            mapper = Movie::toState
        )
    }

    private fun onMoviesLoaded(pagingData: PagingData<MediaItem>) {
        updateState { it.copy(mediaList = flowOf(pagingData)) }
    }

    private fun onDataLoadError(e: Throwable) {
        if (e is NoNetworkException) updateState {
            it.copy(
                isLoading = false,
                isNoInternetConnection = true
            )
        }
        else updateState {
            it.copy(
                isLoading = false,
                isNoInternetConnection = false
            )
        }
    }

    private fun createMoviesPagingSource(): PagingSource<Int, Movie> {
        return BasePagingSourceForHome { page ->
            manageMovieUseCase.getTrendingMovies(page = page, genreId = state.value.selectedGenreId)
        }
    }
}