package com.sanaa.presentation.screen.movieDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.map
import com.sanaa.presentation.details_base.BasePagingSource
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.GenreUiModel
import com.sanaa.presentation.model.MovieUiModel
import com.sanaa.presentation.model.mapper.toActorUiModel
import com.sanaa.presentation.model.mapper.toHistory
import com.sanaa.presentation.navigation.MovieDetailsScreenRoute
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import repository.SavedListsStatusProvider
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
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val savedListsStatusProvider: SavedListsStatusProvider,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<MovieDetailsUiState, MovieDetailsUiEffect>(
    initialState = MovieDetailsUiState(),
    defaultDispatcher = dispatcher
), MovieDetailsScreenInteractionListener {
    val route = MovieDetailsScreenRoute(
        movieId = checkNotNull(savedStateHandle["movieId"]),
    )

    init {
        fetchMovieDetails(route.movieId)
        fetchUserRating()
        updateUserLoginState()
        observeSavedStatus()
    }
    private fun observeSavedStatus() {
        viewModelScope.launch {
            var previousSavedIds = savedListsStatusProvider.savedIds.value
            savedListsStatusProvider.savedIds.collect { newSavedIds ->
                updateState {
                    copy(
                        movieDetails = movieDetails.copy(
                            isSaved = newSavedIds.contains(route.movieId)
                        )
                    )
                }
                showSnackBarForSaveStatus(previousSavedIds, newSavedIds)
                previousSavedIds = newSavedIds
            }
        }
    }

    private fun showSnackBarForSaveStatus(previousSavedIds: Set<Int>, newSavedIds: Set<Int>) {
        if (newSavedIds.contains(route.movieId) && !previousSavedIds.contains(route.movieId)) {
            updateState {
                copy(
                    snackBarData = SnackData(
                        message = stringProvider.addToListSuccess,
                        isError = false
                    )
                )
            }
        }
    }
    override fun onBackClick() {
        emitEffect(MovieDetailsUiEffect.NavigateBack)
    }

    override fun onWatchTrailerClick() {
        state.value.movieDetails.trailerUrl?.let { url ->
            emitEffect(MovieDetailsUiEffect.OpenTrailer(url))
        }
    }

    override fun onBookmarkClick(movie: MovieUiModel) {
        if (!state.value.isUserLoggedIn) {
            promptLogin(LoginPromptType.BOOKMARK)
            return
        }

        if (movie.isSaved) {
            savedListsStatusProvider.markItemUnsaved(movie.id)
            updateState { copy(snackBarData = SnackData(message = stringProvider.addToListSuccess, isError = false)) }
        } else {
            updateState {
                copy(
                    showSaveToListBottomSheet = true,
                    selectedMediaId = movie.id
                )
            }
        }
    }

    override fun onMovieClick(movieId: Int) {
        emitEffect(MovieDetailsUiEffect.NavigateToAnotherMovieDetails(movieId))
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
        fetchMovieDetails(route.movieId)
    }

    override fun onRatingChanged(newRating: Int) {
        updateState { copy(imdbRating = newRating) }
    }

    override fun onDismissRateBottomSheet() {
        updateState { copy(showRateBottomSheet = false) }
    }

    override fun onSubmitRateBottomSheet() {
        tryToExecute(
            block = ::submitMovieRating,
            onError = ::onShowRateBottomSheetFailed,
        )
        updateState {
            copy(showRateBottomSheet = false)
        }
    }

    private fun onShowRateBottomSheetFailed(exception: NovixAppException) {
        updateState {
            copy(
                errorMessage = exception.message,
                showRateBottomSheet = false,
                snackBarData = SnackData(
                    message = exception.message ?: stringProvider.somethingWentWrongError,
                    isError = true
                )
            )
        }
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
            onError = ::onFetchMovieDetailsFailed,
            dispatcher = defaultDispatcher
        )
    }

    private fun onFetchMovieDetailsFailed(exception: NovixAppException) {
        when (exception) {
            is NoNetworkException -> {
                updateState {
                    copy(
                        noInternetConnection = true,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }
            else -> {
                updateState {
                    copy(
                        isLoading = false,
                        errorMessage = exception.message,
                        noInternetConnection = false
                    )
                }
                updateState {
                    copy(
                        snackBarData = SnackData(
                            message = exception.message ?: stringProvider.somethingWentWrongError,
                            isError = true
                        )
                    )
                }
            }
        }
    }

    private fun loadSimilarMovies(movieId: Int): Flow<PagingData<MovieUiModel>> {
        val pagingFlow = createPagingFlow(
            pagingSourceFactory = { createSimilarMoviesPagingSource(movieId) },
            mapper = { movie -> movie.toState() }
        )

        return pagingFlow.combine(savedListsStatusProvider.savedIds) { pagingData, savedIds ->
            pagingData.map { movieUiModel ->
                movieUiModel.copy(isSaved = savedIds.contains(movieUiModel.id))
            }
        }.cachedIn(viewModelScope)
    }

    private fun createSimilarMoviesPagingSource(movieId: Int): PagingSource<Int, Movie> {
        return BasePagingSource { page ->
            manageMovieDetails.getSimilarMoviesByMovieId(movieId, page)
        }
    }

    private fun fetchUserRating() {
        if (state.value.isUserLoggedIn) {
            tryToCollect(
                block = { getCurrentUserRating(route.movieId) },
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

        val currentSavedIds = savedListsStatusProvider.savedIds.value
        val isMovieSaved = currentSavedIds.contains(movie.id)

        addMovieToHistory(movie)
        updateState {
            copy(
                movieDetails = movie.toState(trailerUrl = trailerUrl)
                    .copy(isSaved = isMovieSaved),
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
            movieId = route.movieId,
            rating = rating.toFloat()
        )
        if (isSendRateSuccess) {
            updateState { copy(snackBarData = SnackData(message = stringProvider.deleteRatingSuccess, isError = false)) }
            updateState { copy(showRateBottomSheet = false) }
        } else {
            updateState { copy(snackBarData = SnackData(message = stringProvider.deleteRatingFailed, isError = true)) }
        }
    }

    private fun updateUserLoginState() {
        tryToCollect(
            block = { checkUserLogin.isLoggedIn() },
            onCollect = ::onCollectLoggedFlag
        )
    }

    private fun onCollectLoggedFlag(isLogged: Boolean) {
        updateState { copy(isUserLoggedIn = isLogged) }
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

    private fun promptLogin(type: LoginPromptType) {
        updateState {
            copy(
                showLoginBottomSheet = true,
                loginPromptType = type
            )
        }
    }

    override fun onSnackDismissRequested() {
        updateState {
            copy(snackBarData = null)
        }
    }
}