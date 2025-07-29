package com.sanaa.presentation.screen.genreMovies

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.details_base.BasePagingSource
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.MovieUiModel
import com.sanaa.presentation.model.toUiModel
import entity.Movie
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import usecase.ManageMovieUseCase

class GenreMoviesViewModel(
    private val categoryId: Int,
    private val categoryName: String,
    private val manageMoviesDetailsUseCase: ManageMovieUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<GenreMoviesScreenUiState, GenreMoviesEffects>(
    GenreMoviesScreenUiState(),
    dispatcher
), GenreMoviesScreenInteractionListener {
    init {
        fetchMovies(categoryId)
    }

    override fun onRetryClicked() {
        updateState { it.copy(noInternetConnection = false, isLoading = true, error = null) }
        fetchMovies(categoryId)
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

    override fun onLoginButtonClick() {
        updateState { it.copy(showBottomSheet = false) }
        emitEffect(GenreMoviesEffects.NavigateToLogin)
    }

    override fun onBackClick() {
        emitEffect(GenreMoviesEffects.NavigateBack)
    }

    override fun onMovieClick(id: Int) {
        emitEffect(GenreMoviesEffects.NavigateToMovieDetails(id))
    }

    private fun fetchMovies(categoryId: Int) {
        tryToCollect(

            callee = {
                loadMoviesByCategory(categoryId)
            },
            onCollect = { movies ->
                updateState {
                    it.copy(movies = flowOf(movies), title = categoryName, isLoading = false)
                }
            },
            onError = { exception ->
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
            }
        )
    }

    private fun loadMoviesByCategory(genreId: Int): Flow<PagingData<MovieUiModel>> {
        updateState { it.copy(isLoading = true) }
        return createPagingFlow(
            pagingSourceFactory = { createMoviesPagingDataSource(genreId) },
            mapper = Movie::toUiModel

        )
    }


    private fun createMoviesPagingDataSource(
        genreId: Int
    ): PagingSource<Int, Movie> {
        return BasePagingSource { page ->
            manageMoviesDetailsUseCase.getMoviesByCategory(genreId = genreId, page = page)
        }
    }
}