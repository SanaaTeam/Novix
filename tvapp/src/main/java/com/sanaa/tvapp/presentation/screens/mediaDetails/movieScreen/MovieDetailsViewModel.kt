package com.sanaa.tvapp.presentation.screens.mediaDetails.movieScreen

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.tvapp.base.BasePagingSource
import com.sanaa.tvapp.base.BaseViewModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.MovieDetailsUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toActorUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toDetailsUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Movie
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import usecase.ManageMovieUseCase
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageMovieDetails: ManageMovieUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<MovieDetailsScreenUiState, MovieDetailsScreenUiEffect>(
    initialState = MovieDetailsScreenUiState(),
    defaultDispatcher = dispatcher
) {

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"]) {
        "movieId is required in SavedStateHandle"
    }


    init {
        fetchMovieDetails(movieId)
    }


    private fun fetchMovieDetails(movieId: Int) {
        updateState { copy(isLoading = true, errorMessage = null) }
        tryToExecute(
            block = {
                loadMovieDetails(movieId)
            },
            onSuccess = {
                updateState { copy(isLoading = false, errorMessage = null) }
            },
            onError = { exception ->
                if (exception is NoNetworkException) {
                    updateState {
                        copy(
                            noInternetConnection = true,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                } else {
                    updateState {
                        copy(
                            isLoading = false,
                            errorMessage = exception.message,
                            noInternetConnection = false
                        )
                    }
                }
            },
            dispatcher = dispatcher
        )
    }

    private suspend fun loadMovieDetails(movieId: Int) = coroutineScope {
        val movieDeferred = async { manageMovieDetails.getMovieDetails(movieId) }
        val castDeferred = async { manageMovieDetails.getMovieCast(movieId) }
        val imagesDeferred = async { manageMovieDetails.getMovieImagesUrl(movieId) }
        val trailerDeferred = async { manageMovieDetails.getMovieTrailer(movieId) }
        val similarDeferred = async { loadSimilarMovies(movieId) }

        val movie = movieDeferred.await()
        val cast = castDeferred.await()
        val images = imagesDeferred.await()
        val trailerUrl = trailerDeferred.await()
        val similar = similarDeferred.await()

        updateState {
            copy(
                movieDetails = movie.toDetailsUiModel(trailerUrl = trailerUrl),
                cast = cast.map { it.toActorUiModel() },
                imagesUrls = images,
                similarMovies = similar,
            )
        }
    }

    private fun loadSimilarMovies(movieId: Int): Flow<PagingData<MovieDetailsUiModel>> {
        return createPagingFlow(
            pagingSourceFactory = { createSimilarMoviesPagingSource(movieId) },
            mapper = Movie::toDetailsUiModel
        )
    }

    private fun createSimilarMoviesPagingSource(movieId: Int): PagingSource<Int, Movie> {
        return BasePagingSource { page ->
            manageMovieDetails.getSimilarMoviesByMovieId(movieId, page)
        }
    }
}