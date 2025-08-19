package com.sanaa.presentation.screen.genreMovies

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.details_base.BasePagingSource
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.MovieUiModel
import com.sanaa.presentation.model.mapper.toState
import com.sanaa.presentation.navigation.GenreMovieScreenRoute
import com.sanaa.presentation.screen.movieDetails.SnackData
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Movie
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import service.VodStringProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageMovieUseCase
import javax.inject.Inject

@HiltViewModel
class GenreMoviesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageMoviesDetailsUseCase: ManageMovieUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<GenreMoviesScreenUiState, GenreMoviesEffects>(
    initialState = GenreMoviesScreenUiState(),
    defaultDispatcher = dispatcher
), GenreMoviesScreenInteractionListener {
    private val route = GenreMovieScreenRoute(
        genreId = checkNotNull(savedStateHandle["genreId"]),
        genreName = checkNotNull(savedStateHandle["genreName"]),
    )

    init {
        updateUserLoggingStatus()
        fetchMovies(route.genreId)
    }

    fun updateUserLoggingStatus() {
        tryToCollect(
            block = { checkIfUserIsLoggedInUseCase.isLoggedIn() },
            onCollect = ::onCollectLoggedFlag,
        )
    }

    private fun onCollectLoggedFlag(isLogged: Boolean) {
        updateState { copy(userIsLoggedIn = isLogged) }
    }


    override fun onRetryClicked() {
        updateState { copy(noInternetConnection = false, isLoading = true) }
        fetchMovies(route.genreId)
    }


    override fun onBottomSheetDismiss() {
        updateState { copy(showBottomSheet = false) }
    }

    override fun onLoginButtonClick() {
        updateState { copy(showBottomSheet = false) }
        emitEffect(GenreMoviesEffects.NavigateToLogin)
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

    override fun onSaveIconClick(media: MovieUiModel) {
        if (!state.value.userIsLoggedIn) {
            updateState { copy(showBottomSheet = true) }
            return
        }

        updateState {
            copy(
                showSaveToListBottomSheet = true,
                selectedMovieToSave = media
            )
        }
    }


    override fun onBackClick() {
        emitEffect(GenreMoviesEffects.NavigateBack)
    }

    override fun onMovieClick(id: Int) {
        emitEffect(GenreMoviesEffects.NavigateToMovieDetails(id))
    }

    override fun onSnackDismissRequested() {
        updateState { copy(snackBarData = null) }
    }
    private fun fetchMovies(categoryId: Int) {
        tryToCollect(
            block = { loadMoviesByCategory(categoryId) },
            onCollect = onCollectMovies(),
            onError = ::onFetchMoviesFailed
        )
    }

    private fun loadMoviesByCategory(genreId: Int): Flow<PagingData<MovieUiModel>> {
        updateState { copy(isLoading = true) }
        return createPagingFlow(
            pagingSourceFactory = { createMoviesPagingDataSource(genreId) },
            mapper = Movie::toState
        )
    }

    private fun onCollectMovies(): suspend (PagingData<MovieUiModel>) -> Unit = { movies ->
        updateState { copy(movies = flowOf(movies), title = route.genreName, isLoading = false) }
    }

    private fun onFetchMoviesFailed(exception: Exception) {
        when (exception) {
            is NoNetworkException -> {
                updateState {
                    copy(
                        noInternetConnection = true,
                        isLoading = false,
                        snackBarData = SnackData(
                            message = stringProvider.noInternetConnectionError,
                            isError = true,
                        )
                    )
                }
            }

            else -> {
                updateState {
                    copy(
                        noInternetConnection = false,
                        isLoading = false,
                        snackBarData = SnackData(
                            message = stringProvider.somethingWentWrongError,
                            isError = true
                        )
                    )
                }
            }
        }
    }


    private fun createMoviesPagingDataSource(
        genreId: Int,
    ): PagingSource<Int, Movie> {
        return BasePagingSource { page ->
            manageMoviesDetailsUseCase.getMoviesByCategory(genreId = genreId, page = page)
        }
    }
}