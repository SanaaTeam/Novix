package com.sanaa.presentation.screen.movie_categories

import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.toUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.ManageMovieUseCase

class MovieCategoriesViewModel(
    private val categoryId: Int,
    private val categoryName: String,
    private val manageMoviesDetailsUseCase: ManageMovieUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<MovieCategoriesScreenUiState, MovieCategoriesScreenEffects>(
    MovieCategoriesScreenUiState(),
    dispatcher
), MovieCategoriesScreenInteractionListener {
    init {
        getListOfMoviesByCategory(categoryId)
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
        emitEffect(MovieCategoriesScreenEffects.NavigateBack)
    }

    override fun onMovieClick(id: Int) {
        emitEffect(MovieCategoriesScreenEffects.NavigateToMovieDetails(id))
    }



    private fun getListOfMoviesByCategory(categoryId: Int) {
        tryToExecute(
            callee = { fetchMovies(categoryId) },
            onSuccess = {
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
    private suspend fun fetchMovies(categoryId: Int) {
        updateState {
            it.copy(isLoading = true)
        }
        val movies = manageMoviesDetailsUseCase.getMoviesByCategory(categoryId)
        updateState {
            it.copy(
                title = categoryName, movies = movies.map { it ->
                    it.toUiModel()
                }
            )
        }
    }
}