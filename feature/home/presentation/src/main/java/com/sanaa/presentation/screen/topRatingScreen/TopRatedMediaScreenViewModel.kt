package com.sanaa.presentation.screen.topRatingScreen

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.map
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.base.BasePagingSourceForHome
import com.sanaa.presentation.components.SnackData
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItemUiState
import com.sanaa.presentation.state.MediaTypeUiState
import com.sanaa.presentation.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Movie
import entity.TvSeries
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import repository.SavedListsStatusProvider
import service.VodStringProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import javax.inject.Inject

@HiltViewModel
class TopRatedMediaScreenViewModel @Inject constructor(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val savedListsStatusProvider: SavedListsStatusProvider,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<TopRatedMediaScreenUiState, TopRatedMediaScreenEffect>(
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
        tryToCollect(
            block = { checkIfUserIsLoggedInUseCase.isLoggedIn() },
            onCollect = ::onCollectLoggedFlag,
        )
        updateState { copy(showLoginBottomSheet = false) }
    }

    private fun onCollectLoggedFlag(isLogged: Boolean) {
        updateState { copy(userIsLoggedIn = isLogged) }
    }

    private fun fetchMovies(genreId: Int? = null) {
        tryToExecute(
            onStart = { updateState { copy(isLoading = true) } },
            block = { loadTopRatedMovies(genreId = genreId) },
            onSuccess = ::onFetchMoviesSuccess,
            onError = ::onDataLoadError
        )
    }

    private fun onFetchMoviesSuccess(mediaList: Flow<PagingData<MediaItemUiState>>) {
        updateState {
            copy(movieList = mediaList, isLoading = false, isNoInternetConnection = false)
        }
    }

    private fun fetchTvShows(genreId: Int? = null) {
        tryToExecute(
            onStart = { updateState { copy(isLoading = true) } },
            block = { loadTopRatedTvSeries(genreId = genreId) },
            onSuccess = ::onFetchTvShowsSuccess,
            onError = ::onDataLoadError
        )
    }

    private fun onFetchTvShowsSuccess(mediaList: Flow<PagingData<MediaItemUiState>>) {
        updateState {
            copy(tvShowList = mediaList, isLoading = false, isNoInternetConnection = false)
        }
    }

    private fun fetchMovieGenres() {
        tryToExecute(
            onStart = { updateState { copy(isLoading = true) } },
            block = { manageMovieUseCase.getMovieGenres().map { it.toState() } },
            onSuccess = ::onFetchMovieGenresSuccess,
            onError = ::onDataLoadError
        )
    }

    private fun onFetchMovieGenresSuccess(genres: List<GenreUiState>) {
        updateState {
            copy(
                movieGenres = genres,
                isLoading = false,
                isNoInternetConnection = false
            )
        }
    }

    private fun fetchTvShowGenres() {
        tryToExecute(
            onStart = { updateState { copy(isLoading = true) } },
            block = { manageTvSeriesUseCase.getSeriesGenres().map { it.toState() } },
            onSuccess = ::onFetchTvShowGenresSuccess,
            onError = ::onDataLoadError
        )
    }

    private fun onFetchTvShowGenresSuccess(genres: List<GenreUiState>) {
        updateState {
            copy(
                tvShowGenres = genres,
                isLoading = false,
                isNoInternetConnection = false
            )
        }
    }

    override fun onMediaTabSelection(mediaTypeUiState: MediaTypeUiState) {
        updateState { copy(selectedMediaTypeUiState = mediaTypeUiState) }
    }

    override fun onMovieGenreClick(id: Int?) {
        if (id == state.value.movieSelectedGenreId) return

        updateState { copy(movieSelectedGenreId = id) }
        fetchMovies(id)
    }

    override fun onTvShowGenreClick(id: Int?) {
        if (id != state.value.tvShowSelectedGenreId) {
            updateState { copy(tvShowSelectedGenreId = id) }
            fetchTvShows(id)
        }
    }

    override fun onMediaClick(id: Int, mediaTypeUiState: MediaTypeUiState) {
        emitEffect(TopRatedMediaScreenEffect.NavigateToMediaDetails(id, mediaTypeUiState))
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
                    selectedMediaToSave = media
                )
            }
        }
    }

    override fun onSaveToListSuccess() {
        updateState {
            copy(
                snackBarData = SnackData(message = stringProvider.addToListSuccess, isError = false)
            )
        }
    }

    override fun onSaveToListFailure() {
        updateState {
            copy(snackBarData = SnackData(message = stringProvider.addToListFailed, isError = true))
        }
    }


    override fun onDismissSaveToListBottomSheet() {
        updateState { copy(showSaveToListBottomSheet = false, selectedMediaToSave = null) }
    }

    override fun onCreateNewListClick() {
        updateState { copy(showSaveToListBottomSheet = false, showAddListBottomSheet = true) }
    }

    override fun onDismissAddListBottomSheet() {
        updateState { copy(showAddListBottomSheet = false) }
    }


    override fun onBackClick() {
        emitEffect(TopRatedMediaScreenEffect.NavigateBack)
    }

    override fun onLoginButtonClick() {
        emitEffect(TopRatedMediaScreenEffect.NavigateToLogin)
    }

    override fun onDismissLoginBottomSheet() {
        updateState { copy(showLoginBottomSheet = false) }
    }

    override fun onRetryClick() {
        updateUserLoggingStatus()
        fetchMovieGenres()
        fetchTvShowGenres()
        fetchMovies()
        fetchTvShows()
    }

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
    }

    private fun loadTopRatedMovies(
        genreId: Int?,
    ): Flow<PagingData<MediaItemUiState>> {

        return createPagingFlow(
            pagingSourceFactory = { createMoviePagingDataSource(genreId = genreId) },
            mapper = Movie::toState
        ).combine(savedListsStatusProvider.savedIds) { pagingData, savedIds ->
            pagingData.map { mediaItem ->
                mediaItem.copy(isSaved = savedIds.contains(mediaItem.id))
            }
        }.cachedIn(viewModelScope)
    }

    private fun loadTopRatedTvSeries(
        genreId: Int?,
    ): Flow<PagingData<MediaItemUiState>> {

        return createPagingFlow(
            pagingSourceFactory = { createTvShowPagingDataSource(genreId = genreId) },
            mapper = TvSeries::toState
        )
    }

    private fun onDataLoadError(e: Throwable) {
        when (e) {
            is NoNetworkException -> {
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
            }

            else -> {
                updateState {
                    copy(
                        isNoInternetConnection = false,
                        snackBarData =
                            SnackData(
                                message = stringProvider.somethingWentWrongError,
                                isError = true
                            )
                    )
                }
            }
        }
    }


    private fun createMoviePagingDataSource(
        genreId: Int?,
        onError: ((Throwable) -> Unit)? = ::onDataLoadError,
    ): PagingSource<Int, Movie> {
        return BasePagingSourceForHome(onError = onError) { page ->
            manageMovieUseCase.getTopRatedMovies(
                page = page,
                genreId = genreId
            )
        }
    }

    private fun createTvShowPagingDataSource(
        genreId: Int?,
        onError: ((Throwable) -> Unit)? = ::onDataLoadError,
    ): PagingSource<Int, TvSeries> {
        return BasePagingSourceForHome(onError = onError) { page ->
            manageTvSeriesUseCase.getTopRatedTvSeries(
                page = page,
                genreId = genreId
            )
        }
    }
}