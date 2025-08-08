package com.sanaa.presentation.screen.trendingMediaScreen.trendingMoviesScreen

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.map
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.base.BasePagingSourceForHome
import com.sanaa.presentation.screen.trendingMediaScreen.MediaListScreenInteractionListener
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenEffect
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenUiState
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Movie
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import repository.SavedMovieStatusProvider
import service.VodStringProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageMovieUseCase
import javax.inject.Inject

@HiltViewModel
class TrendingMoviesScreenViewModel @Inject constructor(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val stringProvider: VodStringProvider,
    private val savedMovieStatusProvider: SavedMovieStatusProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
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
            callee = { checkIfUserIsLoggedInUseCase.isLoggedIn() },
            onCollect = { isLogged ->
                updateState {
                    it.copy(
                        userIsLoggedIn = isLogged
                    )
                }
            },
        )
    }

    private fun fetchGenres() {
        tryToExecute(
            callee = ::loadGenresOperation,
            onSuccess = ::onLoadGenresSuccess,
            onError = ::onDataLoadError
        )
    }

    private suspend fun loadGenresOperation(): List<GenreUiState> {
        updateState {
            it.copy(isLoading = true)
        }
        return manageMovieUseCase.getMovieGenres().map { it.toState() }
    }

    private fun onLoadGenresSuccess(genres: List<GenreUiState>) {
        updateState {
            it.copy(genreList = genres, isLoading = false, isNoInternetConnection = false)
        }
    }

    private fun loadMovies() {
        tryToCollect(
            callee = ::loadMoviesOperation,
            onCollect = ::onLoadMoviesSuccess,
            onError = ::onDataLoadError
        )
    }

    private fun loadMoviesOperation(): Flow<PagingData<MediaItem>> {
        return createPagingFlow(
            pagingSourceFactory = { createMoviesPagingSource() },
            mapper = Movie::toState
        ).combine(savedMovieStatusProvider.savedIds) { pagingData, savedIds ->
            pagingData.map { mediaItem ->
                mediaItem.copy(isSaved = savedIds.contains(mediaItem.id))
            }
        }.cachedIn(viewModelScope)
    }

    private fun onLoadMoviesSuccess(pagingData: PagingData<MediaItem>) {
        updateState {
            it.copy(
                mediaList = flowOf(pagingData),
                isLoading = false,
                isNoInternetConnection = false
            )
        }
    }

    override fun onGenreClick(id: Int?) {
        if (id == state.value.selectedGenreId) return
        updateState {
            it.copy(selectedGenreId = id)
        }
        loadMovies()
    }

    override fun onMediaClick(id: Int) {
        emitEffect(TrendingMediaScreenEffect.NavigateToMediaDetails(id))
    }


    override fun onSaveIconClick(media: MediaItem) {
        if (!state.value.userIsLoggedIn) {
            updateState { it.copy(showBottomSheet = true) }
            return
        }

        if (media.isSaved) {
            savedMovieStatusProvider.markUnsaved(media.id)
        } else {
            updateState {
                it.copy(
                    showSaveToListBottomSheet = true,
                    selectedMediaId = media.id
                )
            }
        }
    }


    override fun onSaveToListSuccess() {
        emitEffect(TrendingMediaScreenEffect.ShowSuccess(message = stringProvider.addToListSuccess))
    }

    override fun onSaveToListFailure() {
        emitEffect(TrendingMediaScreenEffect.ShowError(message = stringProvider.addToListFailed))
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

    override fun onDismissBottomSheet() {
        updateState { it.copy(showBottomSheet = false) }
    }

    override fun onDismissSaveToListBottomSheet() {
        updateState { it.copy(showSaveToListBottomSheet = false, selectedMediaId = null) }
    }

    override fun onCreateNewListClick() {
        updateState { it.copy(showSaveToListBottomSheet = false, showAddListBottomSheet = true) }
    }

    override fun onDismissAddListBottomSheet() {
        updateState { it.copy(showAddListBottomSheet = false) }
    }

    private fun onDataLoadError(e: Throwable) {
        if (e is NoNetworkException) {
            updateState { it.copy(isNoInternetConnection = true) }
            emitEffect(TrendingMediaScreenEffect.ShowError(message = stringProvider.noInternetConnectionError))
        } else {
            updateState { it.copy(isNoInternetConnection = false) }
            emitEffect(TrendingMediaScreenEffect.ShowError(message = stringProvider.somethingWentWrongError))
        }
    }

    private fun createMoviesPagingSource(onError: ((Throwable) -> Unit)? = ::onDataLoadError): PagingSource<Int, Movie> {
        return BasePagingSourceForHome(onError = onError) { page ->
            manageMovieUseCase.getTrendingMovies(page = page, genreId = state.value.selectedGenreId)
        }
    }
}