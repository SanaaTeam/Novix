package com.sanaa.presentation.screen.genreMovies

import androidx.lifecycle.SavedStateHandle
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import usecase.ManageMovieUseCase

@HiltViewModel
class GenreMoviesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageMoviesDetailsUseCase: ManageMovieUseCase
) : BaseViewModel<GenreMoviesScreenUiState, GenreMoviesEffects>(
    initialState = GenreMoviesScreenUiState(),
    defaultDispatcher = Dispatchers.IO
), GenreMoviesScreenInteractionListener {

    private val categoryId: Int = checkNotNull(savedStateHandle["categoryId"])
    private val categoryName: String = checkNotNull(savedStateHandle["categoryName"])

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
        emitEffect(GenreMoviesEffects.NavigateBack)
    }

    override fun onMovieClick(id: Int) {
        emitEffect(GenreMoviesEffects.NavigateToMovieDetails(id))
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