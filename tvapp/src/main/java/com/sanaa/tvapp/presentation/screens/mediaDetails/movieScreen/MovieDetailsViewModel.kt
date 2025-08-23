package com.sanaa.tvapp.presentation.screens.mediaDetails.movieScreen

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.tvapp.base.BasePagingSource
import com.sanaa.tvapp.base.BaseViewModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.MovieDetailsUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toActorUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toDetailsUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toHistory
import com.sanaa.tvapp.state.SnackData
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Movie
import entity.User
import exceptions.NoNetworkException
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import service.VodStringProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.ManageMovieUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageMovieDetails: ManageMovieUseCase,
    private val checkUserLogin: CheckIfUserIsLoggedInUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<MovieDetailsScreenUiState, MovieDetailsScreenUiEffect>(
    initialState = MovieDetailsScreenUiState(),
    defaultDispatcher = dispatcher
), MovieDetailsScreenInteractionListener {

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"]) {
        "movieId is required in SavedStateHandle"
    }

    init {
        fetchMovieDetails(movieId)
        updateUserLoginState()
    }


    private fun fetchMovieDetails(movieId: Int) {
        tryToExecute(
            onStart = {
                updateState { copy(isLoading = true) }
            },
            block = {
                loadMovieDetails(movieId)
            },
            onSuccess = {
                updateState { copy(isLoading = false) }
            },
            onError = ::onErrorAccrue
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

        addMovieToHistory(movie)
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


    override fun onWatchTrailerClick(urlString: String) {
        emitEffect(MovieDetailsScreenUiEffect.OpenTrailer(urlString))
    }


    override fun onDismissLoginBottomSheet() {
        updateState { copy(showLoginDialog = false) }
    }

    override fun onLoginButtonClick() {
        emitEffect(MovieDetailsScreenUiEffect.NavigateToLogin)
    }

    override fun onRetryLoadDetails() {
        updateState {
            copy(
                isLoading = true,
                noInternetConnection = false
            )
        }
        fetchMovieDetails(movieId)
    }

    override fun onRateMovieClick() {
        if (state.value.isUserLoggedIn) {
            updateState {
                copy(showRateDialog = true)
            }
        } else {
            updateState {
                copy(showLoginDialog = true)
            }
        }
    }

    override fun onRatingChange(rating: Int) {
        updateState { copy(rating = rating) }
    }

    override fun onDismissRateDialog() {
        updateState { copy(showLoginDialog = false) }
    }

    override fun onSummitRateClick() {
        tryToExecute(
            block = ::submitMovieRating,
            onError = ::onErrorAccrue
        )
    }

    override fun onSimilarMovieClick(movieId: Int) {
        emitEffect(MovieDetailsScreenUiEffect.NavigateToAnotherMovieDetails(movieId))
    }

    override fun onActorCardClick(actorId: Int) {
        emitEffect(MovieDetailsScreenUiEffect.NavigateToActorScreen(actorId))
    }

    override fun onSnackDismissRequested() {
        updateState { copy(snackBarData = null) }
    }

    private fun addMovieToHistory(movie: Movie) {
        tryToCollect(
            block = { getLoggedInUserUseCase.getLoggedInUser() },
            onCollect = onCollectUser(movie)
        )
    }

    private fun onCollectUser(movie: Movie): suspend (User) -> Unit = { user ->
        manageWatchedMediaHistoryUseCase.addWatchedMediaHistory(
            mediaHistoryItem = movie.toHistory(),
            username = user.username
        )
    }

    private fun updateUserLoginState() {
        tryToCollect(
            block = { checkUserLogin.isLoggedIn() },
            onCollect = ::onCollectLoggedFlag,
        )
    }
    private fun onCollectLoggedFlag(isLogged: Boolean) {
        if (isLogged) {
            fetchUserRating()
            if (state.value.showLoginDialog) {
                updateState {
                    copy(
                        showLoginDialog = false,
                        showRateDialog = true,
                    )
                }
            }
        }
        updateState { copy(isUserLoggedIn = isLogged) }
    }

    private fun fetchUserRating() {
        tryToExecute(
            block = { getCurrentUserRating(movieId) },
            onSuccess = ::onFetchUserRatingSuccess,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getCurrentUserRating(movieId: Int): Int {
        try {
            val user = getLoggedInUserUseCase.getLoggedInUser().first()
            return manageMovieDetails.getMovieRate(user.id, movieId)
        } catch (_: Exception) {
            return 0
        }
    }

    private fun onFetchUserRatingSuccess(rating: Int) {
        updateState {
            copy(
                rating = rating,
                isRatingSubmitted = rating != 0
            )
        }
    }

    private suspend fun submitMovieRating() {
        val rating = state.value.rating
        val isSendRateSuccess = manageMovieDetails.addMovieRate(
            movieId = movieId,
            rating = rating.toFloat()
        )
        if (isSendRateSuccess) {
            updateState {
                copy(
                    showRateDialog = false,
                    isRatingSubmitted = true,
                    snackBarData = SnackData(
                        message = stringProvider.submitRatingSuccess,
                        isError = false
                    )
                )
            }
        }
        else {
            updateState {
                copy(
                    showRateDialog = false,
                    snackBarData = SnackData(
                        message = stringProvider.submitRatingFailed,
                        isError = true
                    )
                )
            }
        }
    }

    private fun onErrorAccrue(e: NovixAppException) {
        if (e is NoNetworkException) {
            updateState {
                copy(
                    noInternetConnection = true,
                    isLoading = false,
                    snackBarData = SnackData(
                        message = stringProvider.noInternetConnectionError,
                        isError = true
                    )
                )
            }
        } else {
            updateState {
                copy(
                    isLoading = false,
                    snackBarData = SnackData(
                        message = stringProvider.somethingWentWrongError,
                        isError = true
                    ),
                    noInternetConnection = false
                )
            }
        }
    }
}