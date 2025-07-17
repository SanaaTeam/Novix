package com.sanaa.presentation.screen.movie_categories

import com.sanaa.presentation.details_base.BaseViewModel
import details.usecase.movie.GetMoviesByCategory
import entity.Genre

class MovieCategoriesViewModel(
    private val categoryId: String,
    private val getMoviesByCategoryUseCase: GetMoviesByCategory,
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

    override fun onMovieClick() {
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