package com.sanaa.tvapp.presentation.screens.category

import com.sanaa.tvapp.base.BaseViewModel
import com.sanaa.tvapp.presentation.screens.category.CategoriesScreenUiState.Companion.MOVIE_TAB_INDEX
import com.sanaa.tvapp.presentation.screens.category.state.CategoryUiState
import com.sanaa.tvapp.presentation.screens.category.state.mapper.toUiState
import com.sanaa.tvapp.state.SnackData
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Genre
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import service.VodStringProvider
import usecase.ManageMovieUseCase
import usecase.ManageTvShowUseCase
import javax.inject.Inject

@HiltViewModel
class CategoriesScreenViewModel @Inject constructor(
    private val getTvGenresUseCase: ManageTvShowUseCase,
    private val getMovieGenresUseCase: ManageMovieUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<CategoriesScreenUiState, CategoriesScreenEffects>(
    CategoriesScreenUiState(),
    dispatcher
), CategoriesScreenInteractionListener {

    init {
        loadTvGenres()
        loadMovieGenres()
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
        loadTvGenres(true)
        loadMovieGenres(true)
    }

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
    }


    private fun loadTvGenres(freshData:Boolean = false) {
        tryToExecute(
            onStart = {
                updateState { copy(isLoading = true) }
            },
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
        tryToExecute(
            onStart = {
                updateState { copy(isLoading = true) }
            },
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
                updateState {
                    copy(
                        isNoInternet = true,
                        snackBarData = SnackData(message = stringProvider.noInternetConnectionError, isError = true )
                    )
                }
            }
            else-> updateState {
                copy(
                    isNoInternet = true,
                    snackBarData = SnackData(message = stringProvider.somethingWentWrongError, isError = true )
                )
            }
        }
    }
}