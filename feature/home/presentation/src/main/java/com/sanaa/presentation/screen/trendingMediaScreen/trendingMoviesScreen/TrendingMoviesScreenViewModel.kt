package com.sanaa.presentation.screen.trendingMediaScreen.trendingMoviesScreen

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.base.BasePagingSourceForHome
import com.sanaa.presentation.screen.trendingMediaScreen.MediaListScreenInteractionListener
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenEffect
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenUiState
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Movie
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import service.VodStringProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageMovieUseCase
import javax.inject.Inject

@HiltViewModel
class TrendingMoviesScreenViewModel @Inject constructor(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val stringProvider: VodStringProvider,
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

    fun updateUserLoggingStatus(){
        viewModelScope.launch {
            val isLoggedIn = checkIfUserIsLoggedInUseCase.isLoggedIn()
            updateState {
                it.copy(
                    userIsLoggedIn = isLoggedIn
                )
            }
        }
    }

    private fun fetchGenres() {
        tryToExecute(
            callee = {
                updateState {
                    it.copy(isLoading = true)
                }
                manageMovieUseCase.getMovieGenres().map { it.toState() }
            },
            onSuccess = { genres ->
                updateState {
                    it.copy(genreList = genres, isLoading = false)
                }
            },
            onError = ::onDataLoadError
        )
    }

    override fun onGenreClick(id: Int?) {
        if (id != state.value.selectedGenreId) {
            updateState {
                it.copy(selectedGenreId = id)
            }
            loadMovies()
        }
    }

    override fun onMediaClick(id: Int) {
        emitEffect(TrendingMediaScreenEffect.NavigateToMediaDetails(id))
    }

    override fun onSaveIconClick(media: MediaItem) {
        if (!state.value.userIsLoggedIn){
            updateState {
                it.copy(
                    showBottomSheet = true
                )
            }
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

    override fun onDismissBottomSheet() {
        updateState { it.copy(showBottomSheet = false) }
    }

    private fun loadMovies() {
        tryToCollect(
            callee = ::loadMoviesOperation,
            onCollect = ::onMoviesLoaded,
            onError = ::onDataLoadError
        )
    }

    private fun loadMoviesOperation(): Flow<PagingData<MediaItem>> {
        return createPagingFlow(
            pagingSourceFactory = { createMoviesPagingSource() },
            mapper = Movie::toState
        )
    }

    private fun onMoviesLoaded(pagingData: PagingData<MediaItem>) {
        updateState { it.copy(mediaList = flowOf(pagingData)) }
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