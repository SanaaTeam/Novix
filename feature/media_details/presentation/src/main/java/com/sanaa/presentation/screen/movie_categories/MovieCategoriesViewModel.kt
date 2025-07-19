package com.sanaa.presentation.screen.movie_categories

import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.toUiModel
import details.usecase.ManageMovieDetailsUseCase
import entity.Genre
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MovieCategoriesViewModel(
    private val categoryId: Genre,
    private val manageMoviesDetailsUseCase: ManageMovieDetailsUseCase,
    val dispatcher: CoroutineDispatcher = Dispatchers.IO
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

    private fun getListOfMoviesByCategory(categoryId: Genre) {
        tryToExecute(
            callee = {
                updateState {
                    it.copy(isLoading = true)
                }
                val movies = manageMoviesDetailsUseCase.getMoviesByCategory(categoryId)
                updateState {
                    it.copy(
                        title = categoryId, movies = movies.map { it ->
                            it.toUiModel()
                        },
                        isLoading = false
                    )
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
}