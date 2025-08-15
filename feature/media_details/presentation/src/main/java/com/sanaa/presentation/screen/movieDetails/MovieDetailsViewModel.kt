package com.sanaa.presentation.screen.movieDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.sanaa.presentation.details_base.BasePagingSource
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.GenreUiModel
import com.sanaa.presentation.model.MovieUiModel
import com.sanaa.presentation.model.mapper.toActorUiModel
import com.sanaa.presentation.model.mapper.toHistory
import com.sanaa.presentation.model.mapper.toState
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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
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
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<MovieDetailsUiState, MovieDetailsUiEffect>(
    initialState = MovieDetailsUiState(),
    defaultDispatcher = dispatcher
), MovieDetailsScreenInteractionListener {

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"]) {
        "movieId is required in SavedStateHandle"
    }

    init {
        fetchMovieDetails(movieId)
        fetchUserRating()
        updateUserLoginState()
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

    override fun onBookmarkClick(movie: MovieUiModel) {
        if (!state.value.isUserLoggedIn) {
            promptLogin(LoginPromptType.BOOKMARK)
            return
        }

        updateState {
            copy(
                showSaveToListBottomSheet = true,
                selectedMediaId = movie.id
            )

        }
    }

    override fun onDismissSaveToListBottomSheet() {
        updateState { copy(showSaveToListBottomSheet = false) }
    }

    override fun onCreateNewListClick() {
        updateState { copy(showSaveToListBottomSheet = false, showAddListBottomSheet = true) }
    }

    override fun onDismissAddListBottomSheet() {
        updateState { copy(showAddListBottomSheet = false) }
    }

    override fun onSimilarMovieClick(movieId: Int) {
        emitEffect(MovieDetailsUiEffect.NavigateToAnotherMovieDetails(movieId))
    }

    override fun onRateMovieClick() {
        if (state.value.isUserLoggedIn) {
            updateState { copy(showRateBottomSheet = true) }
        } else {
            promptLogin(LoginPromptType.RATE)
        }
    }

    override fun onDismissLoginBottomSheet() {
        updateState { copy(showLoginBottomSheet = false) }
    }

    override fun onLoginButtonClick() {
        updateState { copy(showLoginBottomSheet = false) }
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
            copy(
                isLoading = true,
                errorMessage = null,
                noInternetConnection = false
            )
        }
        fetchMovieDetails(movieId)
    }

    override fun onRatingChanged(newRating: Int) {
        updateState { copy(imdbRating = newRating) }
    }

    override fun onDismissRateBottomSheet() {
        updateState { copy(showRateBottomSheet = false) }
    }

    override fun onSubmitRateBottomSheet() {
        tryToExecute(
            callee = ::submitMovieRating,
            onError = ::onShowRateBottomSheetFailed
        )
        updateState {
            copy(showRateBottomSheet = false)
        }
    }

    private fun onShowRateBottomSheetFailed(exception: NovixAppException) {
        updateState {
            copy(
                errorMessage = exception.message,
                showRateBottomSheet = false
            )
        }
    }

    private fun fetchMovieDetails(movieId: Int) {
        updateState { copy(isLoading = true, errorMessage = null) }
        tryToExecute(
            callee = {
                loadMovieDetails(movieId)
            },
            onSuccess = {
                updateState { copy(isLoading = false, errorMessage = null) }
            },
            onError = ::onFetchMovieDetailsFailed,
            dispatcher = defaultDispatcher
        )
    }

    private fun onFetchMovieDetailsFailed(exception: NovixAppException) {
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
    }


    private fun loadSimilarMovies(movieId: Int): Flow<PagingData<MovieUiModel>> {
        return createPagingFlow(
            pagingSourceFactory = { createSimilarMoviesPagingSource(movieId) },
            mapper = { movie -> movie.toState() }
        ).cachedIn(viewModelScope)
    }

    private fun createSimilarMoviesPagingSource(movieId: Int): PagingSource<Int, Movie> {
        return BasePagingSource { page ->
            manageMovieDetails.getSimilarMoviesByMovieId(movieId, page)
        }
    }

    private fun fetchUserRating() {
        if (state.value.isUserLoggedIn) {
            tryToCollect(
                callee = { getCurrentUserRating(movieId) },
                onCollect = { rating -> updateState { copy(imdbRating = rating) } },
            )
        }
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
                movieDetails = movie.toState(trailerUrl = trailerUrl),
                cast = cast.map { it.toActorUiModel() },
                imagesUrls = images,
                similarMovies = similar,
            )
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getCurrentUserRating(movieId: Int): Flow<Int> {
        return getLoggedInUserUseCase.getLoggedInUser()
            .flatMapLatest { user ->
                flow {
                    try {
                        val rating = manageMovieDetails.getMovieRate(
                            user.id,
                            movieId
                        )
                        emit(rating)
                    } catch (_: Exception) {
                        emit(0)
                    }
                }
            }
    }

    private suspend fun submitMovieRating() {
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

    private fun updateUserLoginState() {
        tryToCollect(
            callee = { checkUserLogin.isLoggedIn() },
            onCollect = ::onCollectLoggedFlag
        )
    }

    private fun onCollectLoggedFlag(isLogged: Boolean) {
        updateState { copy(isUserLoggedIn = isLogged) }
    }

    private fun addMovieToHistory(movie: Movie) {
        tryToCollect(
            callee = { getLoggedInUserUseCase.getLoggedInUser() },
            onCollect = onCollectUser(movie)
        )
    }

    private fun onCollectUser(movie: Movie): suspend (User) -> Unit = { user ->
        manageWatchedMediaHistoryUseCase.addWatchedMediaHistory(
            mediaHistoryItem = movie.toHistory(),
            username = user.username
        )
    }

    private fun promptLogin(type: LoginPromptType) {
        updateState {
            copy(
                showLoginBottomSheet = true,
                loginPromptType = type
            )
        }
    }
}