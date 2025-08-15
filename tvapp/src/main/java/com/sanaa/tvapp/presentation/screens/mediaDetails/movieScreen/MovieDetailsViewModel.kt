package com.sanaa.tvapp.presentation.screens.mediaDetails.movieScreen

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.tvapp.base.TvBasePagingSource
import com.sanaa.tvapp.base.TvBaseViewModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.MovieDetailsUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toActorUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toDetailsUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Movie
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageMovieUseCase
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageMovieDetails: ManageMovieUseCase,
    private val checkUserLogin: CheckIfUserIsLoggedInUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TvBaseViewModel<MovieDetailsScreenUiState, MovieDetailsScreenUiEffect>(
    initialState = MovieDetailsScreenUiState(),
    defaultDispatcher = dispatcher
), MovieDetailsScreenInteractionListener {

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"]) {
        "movieId is required in SavedStateHandle"
    }


    init {
        fetchMovieDetails(movieId)
//        updateUserStatus()
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
            dispatcher = dispatcher
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

        updateState {
            it.copy(
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
        return TvBasePagingSource { page ->
            manageMovieDetails.getSimilarMoviesByMovieId(movieId, page)
        }
    }



    override fun onWatchTrailerClick(urlString:String) {
        emitEffect(MovieDetailsScreenUiEffect.OpenTrailer(urlString))
    }

    override fun onSimilarMovieClick(movieId: Int) {
        emitEffect(MovieDetailsScreenUiEffect.NavigateToAnotherMovieDetails(movieId))
    }

    override fun onDismissLoginBottomSheet() {
        TODO("Not yet implemented")
    }

    override fun onLoginButtonClick() {
        emitEffect(MovieDetailsScreenUiEffect.NavigateToLogin)
    }

    override fun onActorCardClick(actorId: Int) {
        emitEffect(MovieDetailsScreenUiEffect.NavigateToActorScreen(actorId))
    }

    override fun onRetryLoadDetails() {
        TODO("Not yet implemented")
    }

    private fun updateUserLoginState() {
        tryToCollect(
            callee = { checkUserLogin.isLoggedIn() },
            onCollect = { loggedIn ->
                updateState { it.copy(isUserLoggedIn = loggedIn) }
            },
            onError = { },
        )
    }

    fun updateUserStatus() {
        tryToExecute(callee = ::updateUserLoginState)
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}