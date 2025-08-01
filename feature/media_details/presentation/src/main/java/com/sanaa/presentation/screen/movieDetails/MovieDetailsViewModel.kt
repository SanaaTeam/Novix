package com.sanaa.presentation.screen.movieDetails

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.details_base.BasePagingSource
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.GenreUiModel
import com.sanaa.presentation.model.MovieUiModel
import com.sanaa.presentation.model.toActorUiModel
import com.sanaa.presentation.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import entity.Movie
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.ManageMovieUseCase

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageMovieDetails: ManageMovieUseCase,
    private val checkUserLogin: CheckIfUserIsLoggedInUseCase,
    private val getUser: GetLoggedInUserUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<MovieDetailsUiState, MovieDetailsUiEffect>(
    initialState = MovieDetailsUiState(),
    defaultDispatcher = dispatcher
), MovieDetailsScreenInteractionListener {

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"])

    init {
        fetchMovieDetails(movieId)
        tryToExecute(callee = ::getUserState)
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
        updateState { it.copy(showLoginBottomSheetToAddToList = true) }
    }

    override fun onSimilarMovieClick(movieId: Int) {
        emitEffect(MovieDetailsUiEffect.NavigateToAnotherMovieDetails(movieId))
    }

    override fun onRateMovieClick() {
        if (state.value.isUserLoggedIn) {
            updateState { it.copy(showRateBottomSheet = true) }
        } else {
            updateState { it.copy(showLoginBottomSheetToAddToList = true) }
        }
    }

    override fun onDismissLoginBottomSheet() {
        updateState { it.copy(showLoginBottomSheetToAddToList = false) }
    }

    override fun onLoginButtonClick() {
        updateState { it.copy(showLoginBottomSheetToAddToList = false) }
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
        val userId = getUser.getLoggedInUser().id
        val ratedMovies = manageMovieDetails.getMoviesRate(userId)
        val currentMovieRating = ratedMovies.find { it.id == movieId }?.rating ?: 0
        updateState {
            it.copy(
                movieDetails = movie.toUiModel(trailerUrl = trailerUrl),
                cast = cast.map { actor -> actor.toActorUiModel() },
                imagesUrls = images,
                similarMovies = similar,
                imdbRating = currentMovieRating
            )
        }
    }

    private suspend fun addRate() {
        val rating = state.value.imdbRating
        val isSendRateSuccess = manageMovieDetails.addMovieRate(
            movieId = movieId,
            rating = rating.toFloat()
        )
        if (isSendRateSuccess) {
            emitEffect(MovieDetailsUiEffect.ShowSuccessSnackBar)
        } else {
            emitEffect(MovieDetailsUiEffect.ShowErrorSnackBar)
        }
    }

    private suspend fun getUserState() {
        val isUserLoggedIn = checkUserLogin.isLoggedIn()
        updateState { it.copy(isUserLoggedIn = isUserLoggedIn) }
    }
}