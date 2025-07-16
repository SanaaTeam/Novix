package com.sanaa.presentation.screen.movie_details

import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.mapper.toCastUiModel
import com.sanaa.presentation.mapper.toSimilarUiModel
import com.sanaa.presentation.mapper.toUiModel
import com.sanaa.presentation.model.MovieDetailsUiModel
import com.sanaa.presentation.screen.movie_details.interaction_listener.MovieDetailsScreenInteractionListener
import com.sanaa.presentation.screen.movie_details.state.MovieDetailsUiState
import details.usecase.movie.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getMovieCastUseCase: GetMovieCastUseCase,
    private val getMovieImagesUseCase: GetMovieImagesUseCase,
    private val getSimilarMoviesByMovieId: GetSimilarMoviesByMovieId,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<MovieDetailsUiState>(MovieDetailsUiState(), defaultDispatcher),
    MovieDetailsScreenInteractionListener {

    override fun onLoadMovieDetails(movieId: Int) {
        updateState { it.copy(isLoading = true, errorMessage = null) }

        tryToExecute(
            callee = {
                val movie = getMovieDetailsUseCase.execute(movieId)
                val cast = getMovieCastUseCase.execute(movieId)
                val posterUrls = getMovieImagesUseCase.execute(movieId)
                val similar = getSimilarMoviesByMovieId.execute(movieId)

                movie.toUiModel(
                    cast = cast.map { it.toCastUiModel() },
                    similarMovies = similar.map { it.toSimilarUiModel(isBookmarked = false) },
                    isBookmarked = movie.isBookmarked,
                    posterUrls = posterUrls
                )
            },
            onSuccess = { uiModel ->
                updateState {
                    it.copy(
                        isLoading = false,
                        data = uiModel,
                        errorMessage = null
                    )
                }
            },
            onError = { exception ->
                updateState { it.copy(isLoading = false, errorMessage = exception.message) }
            },
            dispatcher = defaultDispatcher
        )
    }

    override fun onBackClick() {}

    override fun onWatchTrailerClick() {}

    override fun onReadMoreClick() {}

    override fun onBookmarkClick(movieId: Int) {
        tryToExecute(
            callee = {
                val currentState = state.value
                val currentData = currentState.data ?: throw IllegalStateException()
                val updatedSimilarMovies = currentData.similarMovies.map { similarMovie ->
                    if (similarMovie.id == movieId) {
                        similarMovie.copy(isBookmarked = !similarMovie.isBookmarked)
                    } else {
                        similarMovie
                    }
                }
                currentData.copy(
                    isBookmarked = if (currentData.id == movieId) !currentData.isBookmarked else currentData.isBookmarked,
                    similarMovies = updatedSimilarMovies
                )
            },
            onSuccess = { updatedData ->
                updateState { it.copy(data = updatedData) }
            },
            onError = { exception ->
                updateState { it.copy(errorMessage = exception.message) }
            },
            dispatcher = defaultDispatcher
        )
    }
}