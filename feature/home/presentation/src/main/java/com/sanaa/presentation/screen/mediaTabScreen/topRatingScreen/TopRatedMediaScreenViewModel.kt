package com.sanaa.presentation.screen.mediaTabScreen.topRatingScreen

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.base.BasePagingSourceForHome
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaTypeUi
import com.sanaa.presentation.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Movie
import entity.TvSeries
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import service.VodStringProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import javax.inject.Inject

@HiltViewModel
class TopRatedMediaScreenViewModel @Inject constructor(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<TopRatedMediaScreenUiState, TopRatedScreenEffect>(
    initialState = TopRatedMediaScreenUiState(),
    defaultDispatcher = dispatcher
), TopRatedScreenInteractionListener {

    init {
        updateUserLoggingStatus()
        fetchMovieGenres()
        fetchTvShowGenres()
        fetchMovies()
        fetchTvShows()
    }

    fun updateUserLoggingStatus() {
        viewModelScope.launch {
            val isLoggedIn = checkIfUserIsLoggedInUseCase.isLoggedIn()
            updateState {
                it.copy(
                    userIsLoggedIn = isLoggedIn
                )
            }
        }
        onDismissBottomSheet()
    }

    private fun fetchMovies(genreId: Int? = null) {
        tryToExecute(
            callee = {
                loadTopRatedMovies(genreId = genreId)
            }, onSuccess = { mediaList ->
                updateState {
                    it.copy(movieList = mediaList, isLoading = false)
                }
            },
            onError = ::onDataLoadError
        )
    }

    private fun fetchTvShows(genreId: Int? = null) {
        tryToExecute(
            callee = {
                loadTopRatedTvSeries(genreId = genreId)
            }, onSuccess = { mediaList ->
                updateState {
                    it.copy(tvShowList = mediaList, isLoading = false)
                }
            },
            onError = ::onDataLoadError
        )
    }

    private fun fetchMovieGenres() {
        tryToExecute(
            callee = {
                updateState {
                    it.copy(isLoading = true)
                }
                manageMovieUseCase.getMovieGenres().map { it.toState() }
            },
            onSuccess = { genres ->
                updateState {
                    it.copy(movieGenres = genres, isLoading = false)
                }
            },
            onError = ::onDataLoadError
        )
    }

    private fun fetchTvShowGenres() {
        tryToExecute(
            callee = {
                updateState {
                    it.copy(isLoading = true)
                }
                manageTvSeriesUseCase.getSeriesGenres().map { it.toState() }
            },
            onSuccess = { genres ->
                updateState {
                    it.copy(tvShowGenres = genres, isLoading = false)
                }
            },
            onError = ::onDataLoadError
        )
    }

    override fun onMediaTabSelection(mediaTypeUi: MediaTypeUi) {
        updateState { it.copy(selectedMediaTypeUi = mediaTypeUi) }
    }

    override fun onMovieGenreClick(id: Int?) {
        if (id != state.value.movieSelectedGenreId) {
            updateState { it.copy(movieSelectedGenreId = id) }
            fetchMovies(id)
        }
    }

    override fun onTvShowGenreClick(id: Int?) {
        if (id != state.value.tvShowSelectedGenreId) {
            updateState { it.copy(tvShowSelectedGenreId = id) }
            fetchTvShows(id)
        }
    }

    override fun onMediaClick(id: Int, mediaTypeUi: MediaTypeUi) {
        emitEffect(TopRatedScreenEffect.NavigateToMediaDetails(id, mediaTypeUi))
    }

    override fun onSaveIconClick(media: MediaItem) {
        if (!state.value.userIsLoggedIn) {
            updateState {
                it.copy(
                    showLoginBottomSheet = true
                )
            }
        }
    }

    override fun onBackClick() {
        emitEffect(TopRatedScreenEffect.NavigateBack)
    }

    override fun onLoginButtonClick() {
        emitEffect(TopRatedScreenEffect.NavigateToLogin)
    }

    override fun onDismissBottomSheet() {
        updateState {
            it.copy(showLoginBottomSheet = false)
        }
    }


    private fun loadTopRatedMovies(
        genreId: Int?
    ): Flow<PagingData<MediaItem>> {
        updateState { it.copy(isLoading = true) }
        return createPagingFlow(
            pagingSourceFactory = {
                createMoviePagingDataSource(
                    genreId = genreId
                )
            },
            mapper = Movie::toState
        )
    }

    private fun loadTopRatedTvSeries(
        genreId: Int?
    ): Flow<PagingData<MediaItem>> {
        updateState { it.copy(isLoading = true) }
        return createPagingFlow(
            pagingSourceFactory = {
                createTvShowPagingDataSource(
                    genreId = genreId
                )
            },
            mapper = TvSeries::toState
        )
    }
    private fun onDataLoadError(e: Throwable) {
        if (e is NoNetworkException) {
            updateState { it.copy(isNoInternetConnection = true) }
            emitEffect(TopRatedScreenEffect.ShowError(message = stringProvider.noInternetConnectionError))
        } else {
            updateState { it.copy(isNoInternetConnection = false) }
            emitEffect(TopRatedScreenEffect.ShowError(message = stringProvider.somethingWentWrongError))
        }
    }


    private fun createMoviePagingDataSource(genreId: Int?, onError: ((Throwable) -> Unit)? = ::onDataLoadError): PagingSource<Int, Movie> {
        return BasePagingSourceForHome(onError = onError) { page ->
            manageMovieUseCase.getTopRatedMovies(
                page = page,
                genreId = genreId
            )
        }
    }

    private fun createTvShowPagingDataSource(genreId: Int?): PagingSource<Int, TvSeries> {
        return BasePagingSourceForHome { page ->
            manageTvSeriesUseCase.getTopRatedTvSeries(
                page = page,
                genreId = genreId
            )
        }
    }
}