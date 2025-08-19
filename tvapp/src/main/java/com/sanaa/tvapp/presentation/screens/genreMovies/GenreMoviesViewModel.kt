package com.sanaa.tvapp.presentation.screens.genreMovies

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.tvapp.base.BasePagingSource
import com.sanaa.tvapp.base.BaseViewModel
import com.sanaa.tvapp.presentation.screens.searchScreen.MovieUiModel
import com.sanaa.tvapp.presentation.screens.searchScreen.mapper.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Movie
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageMovieUseCase
import javax.inject.Inject

@HiltViewModel
class GenreMoviesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageMoviesDetailsUseCase: ManageMovieUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<GenreMoviesScreenUiState, GenreMoviesEffects>(
    initialState = GenreMoviesScreenUiState(),
    defaultDispatcher = dispatcher
), GenreMoviesScreenInteractionListener {

    private val genreId: Int = checkNotNull(savedStateHandle["genreId"])
    private val genreName: String = checkNotNull(savedStateHandle["genreName"])

    init {
        updateUserLoggingStatus()
        fetchMovies(genreId)
    }

    fun updateUserLoggingStatus() {
        tryToCollect(
            block = { checkIfUserIsLoggedInUseCase.isLoggedIn() },
            onCollect = ::onCollectLoggedFlag,
        )
    }

    private fun onCollectLoggedFlag(isLogged: Boolean) {
        updateState { copy(userIsLoggedIn = isLogged) }
    }


    override fun onRetryClicked() {
        updateState { copy(noInternetConnection = false, isLoading = true, error = null) }
        fetchMovies(genreId)
    }

    override fun onMovieClick(id: Int) {
        emitEffect(GenreMoviesEffects.NavigateToMovieDetails(id))
    }

    private fun fetchMovies(categoryId: Int) {
        tryToCollect(
            block = { loadMoviesByCategory(categoryId) },
            onCollect = onCollectMovies(),
        )
    }

    private fun loadMoviesByCategory(genreId: Int): Flow<PagingData<MovieUiModel>> {
        updateState { copy(isLoading = true) }
        return createPagingFlow(
            pagingSourceFactory = { createMoviesPagingDataSource(genreId) },
            mapper = Movie::toUiState
        )
    }

    private fun onCollectMovies(): suspend (PagingData<MovieUiModel>) -> Unit = { movies ->
        updateState { copy(movies = flowOf(movies),
            title = genreName,
            genreId = genreId,
            isLoading = false) }
    }

    private fun createMoviesPagingDataSource(
        genreId: Int,
    ): PagingSource<Int, Movie> {
        return BasePagingSource { page ->
            manageMoviesDetailsUseCase.getMoviesByCategory(genreId = genreId, page = page)
        }
    }
}