package com.sanaa.presentation.screen.movie_categories

import android.util.Log
import com.sanaa.presentation.details_base.BaseViewModel
import details.usecase.ManageMovieDetailsUseCase
import entity.Genre

class MovieCategoriesViewModel(
    private val categoryId: Genre,
    private val manageMoviesDetailsUseCase: ManageMovieDetailsUseCase,
) : BaseViewModel<MovieCategoriesScreenUiState, MovieCategoriesScreenEffects>(
    MovieCategoriesScreenUiState()
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

    override fun onMovieClick(id:Int) {
        emitEffect(MovieCategoriesScreenEffects.NavigateToMovieDetails(id))
    }

    private fun getListOfMoviesByCategory(categoryId: Genre) {
        tryToExecute(
            callee = {
                updateState {
                    it.copy(isLoading = true)
                }
                val movies = manageMoviesDetailsUseCase.getMoviesByCategory(categoryId)
                Log.i("view_model", "genre: ${categoryId}")
                Log.i("view_model", "movies: ${movies}")
                updateState {
                    it.copy(
                        title = categoryId, movies = movies.map { it ->
                            MovieCardUiModel(
                                id = it.id,
                                title = it.title,
                                imageUrl = it.posterImageUrl,
                                rating = it.imdbRating
                            )
                        }

                    )
                }
            }, onSuccess = {
                updateState {
                    it.copy(isLoading = false)
                }
            },
            onError = {
                updateState {
                    it.copy(error = it.error, isLoading = false)
                }
            })
    }
}