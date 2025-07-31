package com.sanaa.presentation.screen.movieDetails

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.details_base.BasePagingSource
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.GenreUiModel
import com.sanaa.presentation.model.MovieUiModel
import com.sanaa.presentation.model.toActorUiModel
import com.sanaa.presentation.model.toUiModel
import entity.Movie
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageMovieUseCase

class MovieDetailsViewModel(
    private val movieId: Int,
    private val manageMovieDetails: ManageMovieUseCase,
    private val checkUserLogin : CheckIfUserIsLoggedInUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<MovieDetailsUiState, MovieDetailsUiEffect>(
    MovieDetailsUiState(),
    dispatcher
), MovieDetailsScreenInteractionListener {

    init {
        fetchMovieDetails(movieId)
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
        if (state.value.isUserLoggedIn){
            updateState { it.copy(showRateBottomSheet = true) }
        }else{
            updateState { it.copy(showLoginBottomSheet = true) }
        }

    }

    override fun onDismissLoginBottomSheet() {
        updateState { it.copy(showLoginBottomSheet = false) }
    }

    override fun onLoginButtonClick() {
        updateState { it.copy(showLoginBottomSheet = false) }
        emitEffect(MovieDetailsUiEffect.NavigateToLogin)
    }

    override fun onActorCardClick(actorId: Int) {
        emitEffect(MovieDetailsUiEffect.NavigateToActorScreen(actorId))
    }

    override fun onShowReviewsClick(movieId: Int) {
        emitEffect(MovieDetailsUiEffect.NavigateToReviewsScreen(movieId))
    }

    override fun onGenreClicked(genre: GenreUiModel) {
        emitEffect(MovieDetailsUiEffect.NavigateToMovieCategoriesScreen(genre.id, genre.name))
    }

    override fun onRetryLoadDetails() {
        updateState {
            it.copy(
                isLoading = true,
                errorMessage = null,
                noInternetConnection = false
            )
        }
        fetchMovieDetails(movieId)
    }

    override fun onRatingChanged(newRating: Int) {
        updateState { it.copy(imdbRating = newRating) }
    }

    override fun onDismissRateBottomSheet() {
        updateState { it.copy(showRateBottomSheet = false) }
    }

    override fun onSubmitRateBottomSheet() {
        tryToExecute(
            callee = ::addRate,
            onError = { exception ->
                updateState {
                    it.copy(
                        errorMessage = exception.message,
                        showRateBottomSheet = false
                    )
                }
            }
        )
        updateState {
            it.copy(showRateBottomSheet = false)
        }
    }

    private fun fetchMovieDetails(movieId: Int) {
        updateState { it.copy(isLoading = true, errorMessage = null) }
        tryToExecute(
            callee = { loadMovieDetails(movieId) },
            onSuccess = {
                updateState { it.copy(isLoading = false, errorMessage = null) }
            },
            onError = { exception ->
                if (exception is NoNetworkException) {
                    updateState {
                        it.copy(
                            noInternetConnection = true,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                } else {
                    updateState {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message,
                            noInternetConnection = false
                        )
                    }
                }
            },
            dispatcher = defaultDispatcher
        )
    }


    private fun loadSimilarMovies(movieId: Int): Flow<PagingData<MovieUiModel>> {
        updateState { it.copy(isLoading = true) }
        return createPagingFlow(
            pagingSourceFactory = { createSimilarMoviesPagingSource(movieId) },
            mapper = Movie::toUiModel
        )
    }

    private fun createSimilarMoviesPagingSource(movieId: Int): PagingSource<Int, Movie> {
        return BasePagingSource { page ->
            manageMovieDetails.getSimilarMoviesByMovieId(movieId, page)
        }
    }

    private suspend fun loadMovieDetails(movieId: Int) {
        val movie = manageMovieDetails.getMovieDetails(movieId)
        val cast = manageMovieDetails.getMovieCast(movieId)
        val images = manageMovieDetails.getMovieImages(movieId)
        val similar = loadSimilarMovies(movieId)
        val trailerUrl = manageMovieDetails.getMovieTrailer(movieId)

        updateState {
            it.copy(
                movieDetails = movie.toUiModel(trailerUrl = trailerUrl),
                cast = cast.map { actor -> actor.toActorUiModel() },
                imagesUrls = images,
                similarMovies = similar
            )
        }
    }

    private suspend fun addRate() {
       val massage = manageMovieDetails.addMovieRate(
            movieId = movieId,
            rating = state.value.imdbRating.toFloat()
        )
    }

    private suspend fun getUserState(){
        val isUserLoggedIn = checkUserLogin.isLoggedIn()
        updateState { it.copy(isUserLoggedIn = isUserLoggedIn) }
    }
}