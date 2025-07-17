package com.sanaa.presentation.screen.movie_details

import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.mapper.toCastUiModel
import com.sanaa.presentation.mapper.toSimilarUiModel
import com.sanaa.presentation.mapper.toUiModel
import details.usecase.movie.GetMovieCastUseCase
import details.usecase.movie.GetMovieDetailsUseCase
import details.usecase.movie.GetMovieImagesUseCase
import details.usecase.movie.GetMovieTrailerUseCase
import details.usecase.movie.GetSimilarMoviesByMovieId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MovieDetailsViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getMovieCastUseCase: GetMovieCastUseCase,
    private val getMovieImagesUseCase: GetMovieImagesUseCase,
    private val getSimilarMoviesByMovieId: GetSimilarMoviesByMovieId,
    private val getMovieTrailerUseCase: GetMovieTrailerUseCase,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<MovieDetailsUiState, MovieDetailsUiEffect>(MovieDetailsUiState(), defaultDispatcher),
    MovieDetailsScreenInteractionListener {

    override fun onLoadMovieDetails(movieId: Int) {
        updateState { it.copy(isLoading = true, errorMessage = null) }

        tryToExecute(
            callee = {
                val movie = getMovieDetailsUseCase.execute(movieId)
                val cast = getMovieCastUseCase.execute(movieId)
                val posterUrls = getMovieImagesUseCase.execute(movieId)
                val similar = getSimilarMoviesByMovieId.execute(movieId)
                val trailerUrl = getMovieTrailerUseCase.execute(movieId)

                movie.toUiModel(
                    cast = cast.map { it.toCastUiModel() },
                    similarMovies = similar.map { it.toSimilarUiModel(isBookmarked = false) },
                    posterUrls = posterUrls,
                    trailerUrl = trailerUrl
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

    override fun onWatchTrailerClick() {
        state.value.data?.trailerUrl?.let { url ->
            emitEffect(MovieDetailsUiEffect.OpenTrailer(url))
        }
    }


    override fun onReadMoreClick() {}

    override fun onBookmarkClick(movieId: Int) {
    }
}