package com.sanaa.presentation.screen.movie_details

import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.module.toActorUiModel
import com.sanaa.presentation.module.toUiModel
import details.usecase.movie.GetMovieCastUseCase
import details.usecase.movie.GetMovieDetailsUseCase
import details.usecase.movie.GetMovieImagesUseCase
import details.usecase.movie.GetMovieTrailerUseCase
import details.usecase.movie.GetSimilarMoviesByMovieId

class MovieDetailsViewModel(
    private val movieId: Int,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getMovieCastUseCase: GetMovieCastUseCase,
    private val getMovieImagesUseCase: GetMovieImagesUseCase,
    private val getSimilarMoviesByMovieId: GetSimilarMoviesByMovieId,
    private val getMovieTrailerUseCase: GetMovieTrailerUseCase,
) : BaseViewModel<MovieDetailsUiState, MovieDetailsUiEffect>(
    MovieDetailsUiState(),
), MovieDetailsScreenInteractionListener {

    init {
        fetchMovieDetails(movieId = movieId)
    }

    private fun fetchMovieDetails(movieId: Int) {
        updateState { it.copy(isLoading = true, errorMessage = null) }
        tryToExecute(
            callee = {
                val movie = getMovieDetailsUseCase.execute(movieId)
                val cast = getMovieCastUseCase.execute(movieId)
                val movieImagesUrls = getMovieImagesUseCase.execute(movieId)
                val similar = getSimilarMoviesByMovieId.execute(movieId)
                val trailerUrl = getMovieTrailerUseCase.execute(movieId)

                updateState {
                    it.copy(
                        movieDetails = movie.toUiModel(trailerUrl = trailerUrl),
                        cast = cast.map { actor -> actor.toActorUiModel() },
                        similarMovies = similar.map { movie -> movie.toUiModel() },
                        imagesUrls = movieImagesUrls,
                    )
                }
            }, onSuccess = { uiModel ->
                updateState {
                    it.copy(
                        isLoading = false, errorMessage = null
                    )
                }
            }, onError = { exception ->
                updateState { it.copy(isLoading = false, errorMessage = exception.message) }
            }, dispatcher = defaultDispatcher
        )
    }

    override fun onBackClick() {
        emitEffect(MovieDetailsUiEffect.NavigateBack)

    }

    override fun onWatchTrailerClick() {
        state.value.movieDetails?.trailerUrl?.let { url ->
            emitEffect(MovieDetailsUiEffect.OpenTrailer(url))
        }
    }


    override fun onReadMoreClick() {}

    override fun onBookmarkClick(movieId: Int) {
    }

    override fun onSimilarMovieClick(movieId: Int) {
        emitEffect(MovieDetailsUiEffect.NavigateToAnotherMovieDetails(movieId))
    }
}