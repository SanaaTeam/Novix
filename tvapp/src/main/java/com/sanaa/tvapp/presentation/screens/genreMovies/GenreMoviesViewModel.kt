package com.sanaa.tvapp.presentation.screens.genreMovies

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.tvapp.base.BasePagingSource
import com.sanaa.tvapp.base.BaseViewModel
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute
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
import usecase.ManageMovieUseCase
import javax.inject.Inject

@HiltViewModel
class GenreMoviesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageMoviesDetailsUseCase: ManageMovieUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<GenreMoviesScreenUiState, GenreMoviesEffects>(
    initialState = GenreMoviesScreenUiState(),
    defaultDispatcher = dispatcher
), GenreMoviesScreenInteractionListener {
    val route = ScreensRoute.GenreMovieScreenRoute(
        genreId = checkNotNull(savedStateHandle["genreId"]),
        genreName = checkNotNull(savedStateHandle["genreName"])
    )

    init {
        fetchMovies(route.genreId)
    }

    override fun onRetryClicked() {
        updateState { copy(isLoading = true, error = null) }
        fetchMovies(route.genreId)
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
                title = route.genreName,
                categoryId = route.genreId,
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
                updateState {
                    copy(
                        isLoading = false,
                        snackBarData = SnackData(
                            message = stringProvider.noInternetConnectionError,
                            isError = true
                        )
                    )
                }
            }

            else -> {
                updateState {
                    copy(
                        isLoading = false,
                        snackBarData = SnackData(
                            message = stringProvider.somethingWentWrongError,
                            isError = true
                        )
                    )
                }
            }
        }
    }
}