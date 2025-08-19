package com.sanaa.presentation.screen

import android.util.Log
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
import usecase.ManageTvShowUseCase
import usecase.MangeUserPreferenceUseCase
import javax.inject.Inject

@HiltViewModel
class CategoriesScreenViewModel @Inject constructor(
    private val getTvGenresUseCase: ManageTvShowUseCase,
    private val getMovieGenresUseCase: ManageMovieUseCase,
    private val mangeUserPreference: MangeUserPreferenceUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<CategoriesScreenUiState, CategoriesScreenEffects>(
    CategoriesScreenUiState(),
    dispatcher
), CategoriesScreenInteractionListener {

    init {
        onLanguageChanges()
    }
    private fun onLanguageChanges(){
        tryToCollect(
            block = {
                mangeUserPreference.getLanguage()
                    },
            onCollect = {
                loadTvGenres(true)
                loadMovieGenres(true)
                        },
        )
    }

    override fun onGenreClicked(category: CategoryUiState) {
        emitEffect(
            effect = if (state.value.selectedTabIndex == MOVIE_TAB_INDEX)
                CategoriesScreenEffects.NavigateToMovieGenreDetails(
                    genreId = category.id,
                    genreName = category.name
                )
            else
                CategoriesScreenEffects.NavigateToTvGenreDetails(
                    genreId = category.id,
                    genreName = category.name
                )
        )
    }

    override fun onTabChanged(tabIndex: Int) {
        if (state.value.selectedTabIndex == tabIndex) return
        updateState { copy(selectedTabIndex = tabIndex) }
    }

    override fun onRetryClick() {
        loadTvGenres()
        loadMovieGenres()
    }


    private fun loadTvGenres(freshData: Boolean = false) {
        updateState { copy(isLoading = true) }
        tryToExecute(
            block = {
                getTvGenresUseCase.getTvShowGenres(freshData)
            },
            onSuccess = ::onLoadTvGenresSuccess,
            onError = ::onErrorLoading
        )
    }


    private fun onLoadTvGenresSuccess(tvGenres: List<Genre>) {
        updateState {
            copy(
                isLoading = false,
                tvCategories = tvGenres.map {
                    it.toUiState()
                }
            )
        }
    }


    private fun loadMovieGenres(freshData: Boolean = false) {
        updateState { copy(isLoading = true) }
        tryToExecute(
            block = {
                getMovieGenresUseCase.getMovieGenres(freshData)
            },
            onSuccess = ::onLoadMovieGenresSuccess,
            onError = ::onErrorLoading
        )
    }

    private fun onLoadMovieGenresSuccess(movieGenres: List<Genre>) {
        updateState {
            copy(
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
                updateState { copy(isNoInternet = true) }
            }
        }
    }
}