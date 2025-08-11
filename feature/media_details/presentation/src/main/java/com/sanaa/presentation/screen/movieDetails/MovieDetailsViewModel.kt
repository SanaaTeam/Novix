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
import com.sanaa.presentation.model.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Movie
import exceptions.NoNetworkException
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

        viewModelScope.launch {
            savedListsStatusProvider.savedIds.collect { savedIds ->
                updateState { current ->
                    current.copy(
                        movieDetails = current.movieDetails.copy(
                            isSaved = savedIds.contains(current.movieDetails.id)
                        )
                    )
                }
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

    override fun onReadMoreClick() {}

    override fun onBookmarkClick(movie: MovieUiModel) {
        if (!state.value.isUserLoggedIn) {
            promptLogin(LoginPromptType.BOOKMARK)
            return
        }

        if (movie.isSaved) {
            savedListsStatusProvider.markItemUnsaved(movie.id)
        } else {
            updateState {
                it.copy(
                    showSaveToListBottomSheet = true,
                    selectedMediaId = movie.id
                )
            }
        }
    }

    override fun onDismissSaveToListBottomSheet() {
        updateState { it.copy(showSaveToListBottomSheet = false) }
    }

    override fun onCreateNewListClick() {
        updateState { it.copy(showSaveToListBottomSheet = false, showAddListBottomSheet = true) }
    }

    override fun onDismissAddListBottomSheet() {
        updateState { it.copy(showAddListBottomSheet = false) }
    }

    override fun onSimilarMovieClick(movieId: Int) {
        emitEffect(MovieDetailsUiEffect.NavigateToAnotherMovieDetails(movieId))
    }

    override fun onRateMovieClick() {
        if (state.value.isUserLoggedIn) {
            updateState { it.copy(showRateBottomSheet = true) }
        } else {
            promptLogin(LoginPromptType.RATE)
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
            callee = ::submitMovieRating,
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
            callee = {
                loadMovieDetails(movieId)
            },
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
        val pagingFlow = createPagingFlow(
            pagingSourceFactory = { createSimilarMoviesPagingSource(movieId) },
            mapper = { movie -> movie.toUiModel() }
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
                callee = { getCurrentUserRating(movieId) },
                onCollect = { rating ->
                    updateState { it.copy(imdbRating = rating) }
                },
            )

        }
    }


    private suspend fun loadMovieDetails(movieId: Int) = coroutineScope {
        val movieDeferred = async { manageMovieDetails.getMovieDetails(movieId) }
        val castDeferred = async { manageMovieDetails.getMovieCast(movieId) }
        val imagesDeferred = async { manageMovieDetails.getMovieImages(movieId) }
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
            it.copy(
                movieDetails = movie.toUiModel(trailerUrl = trailerUrl)
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
            onCollect = { isLogged ->
                updateState { it.copy(isUserLoggedIn = isLogged) }
                if (isLogged) {
                    fetchUserRating()
                }
            },
        )
    }


    private fun addMovieToHistory(movie: Movie) {
        tryToCollect(
            callee = { getLoggedInUserUseCase.getLoggedInUser() },
            onCollect = { user ->
                manageWatchedMediaHistoryUseCase.addWatchedMediaHistory(
                    mediaHistoryItem = movie.toHistory(),
                    username = user.username
                )
            }
        )
    }

    private fun promptLogin(type: LoginPromptType) {
        updateState {
            it.copy(
                showLoginBottomSheet = true,
                loginPromptType = type
            )
        }
    }

}