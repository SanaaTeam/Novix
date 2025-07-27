package com.sanaa.presentation.screen.genreTvShows

import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.toSeriesUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.ManageTvSeriesUseCase

class GenreTvShowsViewModel(
    private val genreId: Int,
    private val genreName: String,
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<GenreTvShowsScreenUiState, GenreTvShowsEffects>(
    GenreTvShowsScreenUiState(), dispatcher
), GenreTvShowsScreenInteractionListener {
    init {
        getTvShowsByGenreId(genreId)
    }

    override fun onSaveIconClick() {
        updateState {
            it.copy(
                showBottomSheet = true
            )
        }
    }

    override fun onBottomSheetDismiss() {
        updateState { it.copy(showBottomSheet = false) }
    }

    override fun onLoginButtonClick() {
        updateState { it.copy(showBottomSheet = false) }
        emitEffect(GenreTvShowsEffects.NavigateToLogin)
    }

    override fun onBackClick() {
        emitEffect(GenreTvShowsEffects.NavigateBack)
    }

    override fun onTvShowClick(id: Int) {
        emitEffect(GenreTvShowsEffects.NavigateToTvShowDetails(id))
    }

    private fun getTvShowsByGenreId(genreId: Int) {
        tryToExecute(callee = {
            updateState {
                it.copy(isLoading = true)
            }
            val tvShows = manageTvSeriesUseCase.getTvSeriesByGenre(genreId)
            updateState {
                it.copy(
                    title = genreName,
                    tvShows = tvShows.map { it.toSeriesUiModel() },
                    isLoading = false
                )
            }
        }, onSuccess = {
            updateState {
                it.copy(isLoading = false)
            }
        }, onError = { exception ->
            updateState {
                it.copy(error = exception.message, isLoading = false)
            }
        })
    }
}