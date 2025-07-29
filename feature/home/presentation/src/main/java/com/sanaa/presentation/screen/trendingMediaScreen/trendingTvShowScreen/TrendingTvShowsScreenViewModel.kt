package com.sanaa.presentation.screen.trendingMediaScreen.trendingTvShowScreen

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.base.BasePagingSourceForHome
import com.sanaa.presentation.screen.trendingMediaScreen.MediaListScreenInteractionListener
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenEffect
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenUiState
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import entity.TvSeries
import exceptions.NoNetworkException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import usecase.ManageTvSeriesUseCase
import javax.inject.Inject

@HiltViewModel
class TrendingTvShowsScreenViewModel @Inject constructor(
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<TrendingMediaScreenUiState, TrendingMediaScreenEffect>(
    initialState = TrendingMediaScreenUiState(),
    defaultDispatcher = dispatcher
), MediaListScreenInteractionListener {

    init {
        fetchGenres()
        loadTvShows()
    }

    private fun fetchGenres() {
        tryToExecute(
            callee = {
                updateState {
                    it.copy(isLoading = true)
                }
                manageTvSeriesUseCase.getSeriesGenres().map { it.toState() }
            },
            onSuccess = { genres ->
                updateState {
                    it.copy(genreList = genres, isLoading = false)
                }
            },
            onError = ::onDataLoadError
        )
    }

    override fun onGenreClick(id: Int?) {
        if (id != state.value.selectedGenreId) {
            updateState {
                it.copy(selectedGenreId = id)
            }
            loadTvShows()
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

    private fun loadTvShows() {
        tryToCollect(
            callee = ::loadTvShowsOperation,
            onCollect = ::onTvShowsLoaded,
            onError = ::onDataLoadError
        )
    }

    private fun loadTvShowsOperation(): Flow<PagingData<MediaItem>> {
        return createPagingFlow(
            pagingSourceFactory = { createTvShowsPagingSource() },
            mapper = TvSeries::toState
        )
    }

    private fun onTvShowsLoaded(pagingData: PagingData<MediaItem>) {
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
                isNoInternetConnection = false,
                error = e.message
            )
        }
    }

    private fun createTvShowsPagingSource(): PagingSource<Int, TvSeries> {
        return BasePagingSourceForHome { page ->
            manageTvSeriesUseCase.getTrendingTvSeries(
                page = page,
                genreId = state.value.selectedGenreId
            )
        }
    }
}