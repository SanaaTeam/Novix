package com.sanaa.presentation.screen.genreTvShows

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingSource
import com.sanaa.presentation.details_base.BasePagingSource
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.toSeriesUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.TvSeries
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageTvSeriesUseCase
import javax.inject.Inject

@HiltViewModel
class GenreTvShowsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<GenreTvShowsScreenUiState, GenreTvShowsEffects>(
    initialState = GenreTvShowsScreenUiState(),
    defaultDispatcher = dispatcher
), GenreTvShowsScreenInteractionListener {

    private val genreId: Int = checkNotNull(savedStateHandle["genreId"])
    private val genreName: String = checkNotNull(savedStateHandle["genreName"])

    init {
        updateUserLoggingStatus()
        getTvShowsByGenreId(genreId)
    }

    fun updateUserLoggingStatus(){
        viewModelScope.launch {
            val isLoggedIn = checkIfUserIsLoggedInUseCase.isLoggedIn()
            updateState {
                it.copy(
                    userIsLoggedIn = isLoggedIn
                )
            }
        }
    }

    override fun onSaveIconClick() {
        if (!state.value.userIsLoggedIn) {
            updateState {
                it.copy(
                    showBottomSheet = true
                )
            }
        }
    }

    override fun onRetryClick() {
        updateState {
            it.copy(
                noInternetConnection = false,
                isLoading = true,
                error = null
            )
        }
        getTvShowsByGenreId(genreId)
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
        tryToCollect(callee = { loadTvShowsByGenreId(genreId) }, onCollect = { tvShows ->
            updateState {
                it.copy(
                    title = genreName, tvShows = flowOf(tvShows), isLoading = false
                )
            }
        }, onError = { exception ->
            if (exception is NoNetworkException) {
                updateState {
                    it.copy(
                        noInternetConnection = true,
                        isLoading = false,
                        error = null
                    )
                }
            } else {
                updateState {
                    it.copy(error = exception.message, isLoading = false)
                }
            }
        })
    }

    private fun loadTvShowsByGenreId(genreId: Int) = createPagingFlow(
        pagingSourceFactory = { createTvShowsPagingDataSource(genreId) },
        mapper = TvSeries::toSeriesUiModel
    )

    private fun createTvShowsPagingDataSource(
        genreId: Int
    ): PagingSource<Int, TvSeries> {
        return BasePagingSource { page ->
            manageTvSeriesUseCase.getTvSeriesByGenre(
                genreId = genreId, page = page
            )
        }
    }
}