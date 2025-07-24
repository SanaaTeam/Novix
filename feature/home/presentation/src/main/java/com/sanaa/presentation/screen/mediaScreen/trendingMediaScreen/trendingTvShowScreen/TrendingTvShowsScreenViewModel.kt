package com.sanaa.presentation.screen.mediaScreen.trendingMediaScreen.trendingTvShowScreen

import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.screen.mediaScreen.trendingMediaScreen.MediaListScreenInteractionListener
import com.sanaa.presentation.screen.mediaScreen.trendingMediaScreen.TrendingMediaScreenEffect
import com.sanaa.presentation.screen.mediaScreen.trendingMediaScreen.TrendingMediaScreenUiState
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.mapper.toState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.ManageTvSeriesUseCase

class TrendingTvShowsScreenViewModel(
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<TrendingMediaScreenUiState, TrendingMediaScreenEffect>(
    TrendingMediaScreenUiState(),
    dispatcher
), MediaListScreenInteractionListener {

    init {
        fetchGenres()
        fetchMedia()
    }

    private fun fetchMedia(genreId: Int? = null) {
        tryToExecute(
            callee = {
                updateState {
                    it.copy(isLoading = true, selectedGenreId = genreId)
                }
                val mediaList =
                    manageTvSeriesUseCase.getTrendingTvSeries(1, state.value.selectedGenreId)
                        .map { it.toState() }
                updateState {
                    it.copy(mediaList = mediaList)
                }
            }, onSuccess = {
                updateState {
                    it.copy(isLoading = false)
                }
            },
            onError = { exception ->
                updateState {
                    it.copy(error = exception.message, isLoading = false)
                }
            }
        )
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
            onError = { exception ->
                updateState {
                    it.copy(error = exception.message, isLoading = false)
                }
            }
        )
    }

    override fun onGenreClick(id: Int?) {
        if (id != state.value.selectedGenreId) {
            fetchMedia(id)
        }
    }

    override fun onMediaClick(id: Int) {
        emitEffect(TrendingMediaScreenEffect.NavigateToMediaDetails(id))
    }

    override fun onSaveIconClick(media: MediaItem) {
        TODO("Not yet implemented")
    }

    override fun onBackClick() {
        emitEffect(TrendingMediaScreenEffect.NavigateBack)
    }
}