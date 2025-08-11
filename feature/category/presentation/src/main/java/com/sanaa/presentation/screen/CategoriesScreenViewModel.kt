package com.sanaa.presentation.screen

import com.sanaa.presentation.categoryBase.BaseViewModel
import com.sanaa.presentation.screen.CategoriesScreenUiState.Companion.MOVIE_TAB_INDEX
import com.sanaa.presentation.state.CategoryUiState
import com.sanaa.presentation.state.mapper.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Genre
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import javax.inject.Inject

@HiltViewModel
class CategoriesScreenViewModel @Inject constructor(
    private val getTvGenresUseCase: ManageTvSeriesUseCase,
    private val getMovieGenresUseCase: ManageMovieUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<CategoriesScreenUiState, CategoriesScreenEffects>(
    CategoriesScreenUiState(),
    dispatcher
), CategoriesScreenInteractionListener {

    init {
        loadTvGenres()
        loadMovieGenres()
    }


    override fun onGenreClicked(category: CategoryUiState) {
        if (state.value.selectedTabIndex == MOVIE_TAB_INDEX)
            emitEffect(
                CategoriesScreenEffects.NavigateToMovieGenreDetails(
                    genreId = category.id,
                    genreName = category.name
                )
            )
        else
            emitEffect(
                CategoriesScreenEffects.NavigateToTvGenreDetails(
                    genreId = category.id,
                    genreName = category.name
                )
            )
    }

    override fun onTabChanged(tabIndex: Int) {
        if (state.value.selectedTabIndex == tabIndex) return
        updateState { it.copy(selectedTabIndex = tabIndex) }
    }


    private fun loadTvGenres() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            callee = {
                getTvGenresUseCase.getSeriesGenres()
            },
            onSuccess = ::onLoadTvGenresSuccess,
            onError = ::onErrorLoading
        )
    }


    private fun onLoadTvGenresSuccess(tvGenres: List<Genre>) {
        updateState {
            it.copy(
                isLoading = false,
                tvCategories = tvGenres.map {
                    it.toUiState()
                }
            )
        }
    }


    private fun loadMovieGenres() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            callee = {
                getMovieGenresUseCase.getMovieGenres()
            },
            onSuccess = ::onLoadMovieGenresSuccess,
            onError = ::onErrorLoading
        )
    }

    private fun onLoadMovieGenresSuccess(movieGenres: List<Genre>) {
        updateState {
            it.copy(
                isLoading = false,
                movieCategories = movieGenres.map {
                    it.toUiState()
                }
            )
        }
    }

    private fun onErrorLoading(error: Throwable) {
        when (error) {
            is NoNetworkException -> {
                updateState { it.copy(isNoInternet = true) }
            }
        }
    }
}