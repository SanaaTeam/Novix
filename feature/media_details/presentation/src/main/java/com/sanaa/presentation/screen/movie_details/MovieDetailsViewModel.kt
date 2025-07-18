package com.sanaa.presentation.screen.movie_details

import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.toActorUiModel
import com.sanaa.presentation.model.toUiModel
import details.usecase.ManageMovieDetailsUseCase

class MovieDetailsViewModel(
    private val movieId: Int,
    private val manageMovieDetails: ManageMovieDetailsUseCase
) : BaseViewModel<MovieDetailsUiState, MovieDetailsUiEffect>(
    MovieDetailsUiState()
), MovieDetailsScreenInteractionListener {

    init {
        fetchMovieDetails(movieId)
    }

    private fun fetchMovieDetails(movieId: Int) {
        updateState { it.copy(isLoading = true, errorMessage = null) }
        tryToExecute(
            callee = {
                val movie = manageMovieDetails.getMovieDetails(movieId)
                val cast = manageMovieDetails.getMovieCast(movieId)
                val images = manageMovieDetails.getMovieImages(movieId)
                val similar = manageMovieDetails.getSimilarMoviesByMovieId(movieId)
                val trailerUrl = manageMovieDetails.getMovieTrailer(movieId)
                updateState {
                    it.copy(
                        movieDetails = movie.toUiModel(trailerUrl = trailerUrl),
                        cast = cast.map { actor -> actor.toActorUiModel() },
                        similarMovies = similar.map { mv -> mv.toUiModel() },
                        imagesUrls = images
                    )
                }
            },
            onSuccess = {
                updateState { it.copy(isLoading = false, errorMessage = null) }
            },
            onError = { exception ->
                updateState { it.copy(isLoading = false, errorMessage = exception.message) }
            },
            dispatcher = defaultDispatcher
        )
    }

    override fun onBackClick() {
        emitEffect(MovieDetailsUiEffect.NavigateBack)
    }

    override fun onWatchTrailerClick() {
        state.value.movieDetails.trailerUrl?.let { url ->
            emitEffect(MovieDetailsUiEffect.OpenTrailer(url))
        }
    }

    override fun onReadMoreClick() {}

    override fun onBookmarkClick(movieId: Int) {
        updateState { it.copy(showLoginBottomSheet = true) }
    }

    override fun onSimilarMovieClick(movieId: Int) {
        emitEffect(MovieDetailsUiEffect.NavigateToAnotherMovieDetails(movieId))
    }

    override fun onRateMovieClick() {
        updateState { it.copy(showLoginBottomSheet = true) }
    }

    override fun onDismissLoginBottomSheet() {
        updateState { it.copy(showLoginBottomSheet = false) }
    }

    override fun onActorCardClick(actorId: Int) {
        emitEffect(MovieDetailsUiEffect.NavigateToActorScreen(actorId))
    }

    override fun onShowReviewsClick(movieId: Int) {
        emitEffect(MovieDetailsUiEffect.NavigateToReviewsScreen(movieId))
    }
}