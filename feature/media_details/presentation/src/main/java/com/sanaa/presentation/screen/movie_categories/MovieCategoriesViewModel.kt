package com.sanaa.presentation.screen.movie_categories

import android.util.Log
import com.sanaa.presentation.details_base.BaseViewModel
import details.usecase.movie.GetMoviesByCategory
import entity.Genre

class MovieCategoriesViewModel(
    private val categoryId: String,
    private val getMoviesByCategoryUseCase: GetMoviesByCategory,
) : BaseViewModel<MovieCategoriesScreenUiState, MovieCategoriesScreenEffects>
    (MovieCategoriesScreenUiState()), MovieCategoriesScreenInteractionListener {
    private val TAG = "MovieCategoriesViewMode"

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

    override fun hideBottomSheet() {
        updateState { it.copy(showBottomSheet = false) }
    }

    override fun onBackClick() {
        emitEffect(MovieCategoriesScreenEffects.NavigateBack)
    }

    override fun openMovie() {
        TODO("Not yet implemented")
    }

    private fun getListOfMoviesByCategory(categoryId: String) {
        tryToExecute(
            callee = {
                updateState {
                    it.copy(isLoading = true)
                }
                val genre = Genre.fromValue(categoryId)
                    ?: throw IllegalArgumentException("Unknown genre: $categoryId")
                val movies = getMoviesByCategoryUseCase.execute(genre)
                Log.i(TAG, "getListOfMoviesByCategory: $movies")
                updateState {
                    it.copy(
                        title = categoryId,
                        movies = movies.map { it ->
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

                Log.i(TAG, "state: ${state.value}")
            },

            onError = {
                updateState {
                    it.copy(error = it.error, isLoading = false)
                }
            })
    }
}