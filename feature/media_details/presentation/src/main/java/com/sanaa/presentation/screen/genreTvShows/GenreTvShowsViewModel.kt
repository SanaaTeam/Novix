package com.sanaa.presentation.screen.genreTvShows

import androidx.lifecycle.SavedStateHandle
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.toSeriesUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import usecase.ManageTvSeriesUseCase
import javax.inject.Inject

@HiltViewModel
class GenreTvShowsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase
) : BaseViewModel<GenreTvShowsScreenUiState, GenreTvShowsEffects>(
    initialState = GenreTvShowsScreenUiState(),
    defaultDispatcher = Dispatchers.IO
), GenreTvShowsScreenInteractionListener {

    private val genreId: Int = checkNotNull(savedStateHandle["genreId"])
    private val genreName: String = checkNotNull(savedStateHandle["genreName"])

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