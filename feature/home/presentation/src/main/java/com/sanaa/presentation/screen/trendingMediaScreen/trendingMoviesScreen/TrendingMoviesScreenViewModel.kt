package com.sanaa.presentation.screen.trendingMediaScreen.trendingMoviesScreen

import androidx.paging.PagingData
import androidx.paging.PagingSource
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
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import service.VodStringProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageMovieUseCase
import javax.inject.Inject

@HiltViewModel
class TrendingMoviesScreenViewModel @Inject constructor(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
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
                updateState {
                    copy(
                        userIsLoggedIn = isLogged
                    )
                }
            },
        )
    }

    private fun fetchGenres() {
        tryToExecute(
            block = ::loadGenresOperation,
            onSuccess = ::onLoadGenresSuccess,
            onError = ::onDataLoadError
        )
    }

    private suspend fun loadGenresOperation(): List<GenreUiState> {
        updateState { copy(isLoading = true) }
        return manageMovieUseCase.getMovieGenres().map { it.toState() }
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

    private fun loadMoviesOperation(): Flow<PagingData<MediaItem>> {
        return createPagingFlow(
            pagingSourceFactory = { createMoviesPagingSource() },
            mapper = Movie::toState
        )
    }

    private fun onLoadMoviesSuccess(pagingData: PagingData<MediaItem>) {
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
        updateState {
            copy(selectedGenreId = id)
        }
        loadMovies()
    }

    override fun onMediaClick(id: Int) {
        emitEffect(TrendingMediaScreenEffect.NavigateToMediaDetails(id))
    }

    override fun onSaveIconClick(media: MediaItem) {
        if (!state.value.userIsLoggedIn) {
            updateState { copy(showBottomSheet = true) }
            return
        }

        updateState {
            copy(
                showSaveToListBottomSheet = true,
                selectedMediaId = media.id
            )
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
        updateState { copy(showBottomSheet = false) }
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

    private fun onDataLoadError(e: NovixAppException) {
        if (e is NoNetworkException) {
            updateState { copy(isNoInternetConnection = true) }
            emitEffect(TrendingMediaScreenEffect.ShowError(message = stringProvider.noInternetConnectionError))
        } else {
            updateState { copy(isNoInternetConnection = false) }
            emitEffect(TrendingMediaScreenEffect.ShowError(message = stringProvider.somethingWentWrongError))
        }
    }

    private fun createMoviesPagingSource(onError: ((NovixAppException) -> Unit)? = ::onDataLoadError): PagingSource<Int, Movie> {
        return BasePagingSourceForHome(onError = onError) { page ->
            manageMovieUseCase.getTrendingMovies(page = page, genreId = state.value.selectedGenreId)
        }
    }
}