package com.sanaa.presentation.screen.trendingMediaScreen.trendingMoviesScreen

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.map
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.base.BasePagingSourceForHome
import com.sanaa.presentation.components.SnackData
import com.sanaa.presentation.screen.trendingMediaScreen.MediaListScreenInteractionListener
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenEffect
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenUiState
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItemUiState
import com.sanaa.presentation.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Movie
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import repository.SavedListsStatusProvider
import service.VodStringProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageMovieUseCase
import javax.inject.Inject

@HiltViewModel
class TrendingMoviesScreenViewModel @Inject constructor(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val savedListsStatusProvider: SavedListsStatusProvider,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<TrendingMediaScreenUiState, TrendingMediaScreenEffect>(
    initialState = TrendingMediaScreenUiState(),
    defaultDispatcher = dispatcher
), MediaListScreenInteractionListener {

    init {
        updateUserLoggingStatus()
        fetchGenres()
        loadMovies()
    }

    fun updateUserLoggingStatus() {
        tryToCollect(
            block = { checkIfUserIsLoggedInUseCase.isLoggedIn() },
            onCollect = { isLogged ->
                updateState { copy(userIsLoggedIn = isLogged) }
            },
        )
    }

    private fun fetchGenres() {
        tryToExecute(
            onStart = { updateState { copy(isLoading = true) } },
            block = { manageMovieUseCase.getMovieGenres().map { it.toState() } },
            onSuccess = ::onLoadGenresSuccess,
            onError = ::onDataLoadError
        )
    }

    private fun onLoadGenresSuccess(genres: List<GenreUiState>) {
        updateState {
            copy(genreList = genres, isLoading = false, isNoInternetConnection = false)
        }
    }

    private fun loadMovies() {
        tryToCollect(
            block = ::loadMoviesOperation,
            onCollect = ::onLoadMoviesSuccess,
            onError = ::onDataLoadError
        )
    }

    private fun loadMoviesOperation(): Flow<PagingData<MediaItemUiState>> {
        return createPagingFlow(
            pagingSourceFactory = { createMoviesPagingSource() },
            mapper = Movie::toState
        ).combine(savedListsStatusProvider.savedIds) { pagingData, savedIds ->
            pagingData.map { mediaItem ->
                mediaItem.copy(isSaved = savedIds.contains(mediaItem.id))
            }
        }.cachedIn(viewModelScope)
    }

    private fun onLoadMoviesSuccess(pagingData: PagingData<MediaItemUiState>) {
        updateState {
            copy(
                mediaList = flowOf(pagingData),
                isLoading = false,
                isNoInternetConnection = false
            )
        }
    }

    override fun onGenreClick(id: Int?) {
        if (id == state.value.selectedGenreId) return
        updateState { copy(selectedGenreId = id) }
        loadMovies()
    }

    override fun onMediaClick(id: Int) {
        emitEffect(TrendingMediaScreenEffect.NavigateToMediaDetails(id))
    }

    override fun onSaveIconClick(media: MediaItemUiState) {
        if (!state.value.userIsLoggedIn) {
            updateState { copy(showLoginBottomSheet = true) }
            return
        }

        if (media.isSaved) {
            savedListsStatusProvider.markItemUnsaved(media.id)
        } else {
            updateState {
                copy(
                    showSaveToListBottomSheet = true,
                    selectedMediaId = media.id
                )
            }
        }
    }

    override fun onSaveToListSuccess() {
        updateState {
            copy(
                snackBarData = SnackData(
                    message = stringProvider.addToListSuccess,
                    isError = false
                )
            )
        }
    }

    override fun onSaveToListFailure() {
        updateState {
            copy(
                snackBarData = SnackData(
                    message = stringProvider.addToListFailed,
                    isError = true
                )
            )
        }
    }

    override fun onBackClick() {
        emitEffect(TrendingMediaScreenEffect.NavigateBack)
    }

    override fun onRetryClick() {
        updateUserLoggingStatus()
        fetchGenres()
        loadMovies()
    }

    override fun onLoginButtonClick() {
        emitEffect(TrendingMediaScreenEffect.NavigateToLogin)
    }

    override fun onDismissLoginBottomSheet() {
        updateState { copy(showLoginBottomSheet = false) }
    }

    override fun onDismissSaveToListBottomSheet() {
        updateState { copy(showSaveToListBottomSheet = false, selectedMediaId = null) }
    }

    override fun onCreateNewListClick() {
        updateState { copy(showSaveToListBottomSheet = false, showAddListBottomSheet = true) }
    }

    override fun onDismissAddListBottomSheet() {
        updateState { copy(showAddListBottomSheet = false) }
    }

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
    }

    private fun onDataLoadError(e: Throwable) {
        if (e is NoNetworkException) {
            updateState {
                copy(
                    isNoInternetConnection = true,
                    snackBarData =
                        SnackData(
                            message = stringProvider.noInternetConnectionError,
                            isError = true
                        )
                )
            }
        } else {
            updateState {
                copy(
                    isNoInternetConnection = false,
                    snackBarData =
                        SnackData(message = stringProvider.somethingWentWrongError, isError = true)
                )
            }
        }
    }

    private fun createMoviesPagingSource(onError: ((Throwable) -> Unit)? = ::onDataLoadError)
    : PagingSource<Int, Movie> {
        return BasePagingSourceForHome(onError = onError) { page ->
            manageMovieUseCase.getTrendingMovies(page = page, genreId = state.value.selectedGenreId)
        }
    }
}