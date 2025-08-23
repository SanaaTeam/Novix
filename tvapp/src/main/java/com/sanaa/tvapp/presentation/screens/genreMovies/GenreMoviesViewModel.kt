package com.sanaa.tvapp.presentation.screens.genreMovies

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.tvapp.base.BasePagingSource
import com.sanaa.tvapp.base.BaseViewModel
import com.sanaa.tvapp.presentation.screens.searchScreen.MovieUiModel
import com.sanaa.tvapp.presentation.screens.searchScreen.mapper.toUiState
import com.sanaa.tvapp.state.SnackData
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Movie
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import service.VodStringProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageMovieUseCase
import javax.inject.Inject

@HiltViewModel
class GenreMoviesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageMoviesDetailsUseCase: ManageMovieUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val stringProvider: VodStringProvider,
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
            onError = ::onErrorLoading
        )
    }

    private fun onCollectLoggedFlag(isLogged: Boolean) {
        updateState { copy(userIsLoggedIn = isLogged) }
    }


    override fun onRetryClicked() {
        updateState { copy(noInternetConnection = false, isLoading = true, error = null) }
        fetchMovies(genreId)
    }

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
    }

    override fun onMovieClick(id: Int) {
        emitEffect(GenreMoviesEffects.NavigateToMovieDetails(id))
    }

    private fun fetchMovies(categoryId: Int) {
        tryToCollect(
            onStart = {
                updateState { copy(isLoading = true) }
            },
            block = { loadMoviesByCategory(categoryId) },
            onCollect = onCollectMovies(),
            onError = ::onErrorLoading
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
        updateState {
            copy(
                movies = flowOf(movies),
                title = genreName,
                categoryId = genreId,
                isLoading = false
            )
        }
    }

    private fun createMoviesPagingDataSource(
        genreId: Int,
    ): PagingSource<Int, Movie> {
        return BasePagingSource { page ->
            manageMoviesDetailsUseCase.getMoviesByCategory(genreId = genreId, page = page)
        }
    }
    private fun onErrorLoading(error: Throwable) {
        when (error) {
            is NoNetworkException -> {
                updateState { copy(
                    isLoading = false,
                    noInternetConnection = true,
                    snackBarData = SnackData(
                        message = stringProvider.noInternetConnectionError,
                        isError = true
                    )
                ) }
            }
            else->{
                updateState { copy(
                    isLoading = false,
                    noInternetConnection = true,
                    snackBarData = SnackData(
                        message = stringProvider.somethingWentWrongError,
                        isError = true
                    )
                ) }
            }
        }
    }
}