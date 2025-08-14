package com.sanaa.presentation.screen.mediaTabScreen.topRatingScreen

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.map
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.base.BasePagingSourceForHome
import com.sanaa.presentation.state.MediaItemUiState
import com.sanaa.presentation.state.MediaTypeUiState
import com.sanaa.presentation.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Genre
import entity.Movie
import entity.TvShow
import exceptions.NoNetworkException
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import repository.SavedListsStatusProvider
import service.VodStringProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageMovieUseCase
import usecase.ManageTvShowUseCase
import javax.inject.Inject

@HiltViewModel
class TopRatedMediaScreenViewModel @Inject constructor(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val manageTvShowUseCase: ManageTvShowUseCase,
    private val savedListsStatusProvider: SavedListsStatusProvider,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
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
        tryToCollect(
            block = { checkIfUserIsLoggedInUseCase.isLoggedIn() },
            onCollect = ::onCollectLoggedFlag,
        )
        onDismissBottomSheet()
    }

    private fun onCollectLoggedFlag(isLogged: Boolean) {
        updateState { copy(userIsLoggedIn = isLogged) }
    }

    private fun fetchMovies(genreId: Int? = null) {
        tryToExecute(
            block = {
                loadTopRatedMovies(genreId = genreId)
                    .combine(savedListsStatusProvider.savedIds) { pagingData, savedIds ->
                        pagingData.map { mediaItem ->
                            mediaItem.copy(isSaved = savedIds.contains(mediaItem.id))
                        }
                    }.cachedIn(viewModelScope)
            },
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
            block = { loadTopRatedTvShows(genreId = genreId) },
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
            block = ::fetchMovieGenresOperation,
            onSuccess = ::onFetchMovieGenresSuccess,
            onError = ::onDataLoadError
        )
    }

    private suspend fun fetchMovieGenresOperation(): List<Genre> {
        updateState {
            copy(isLoading = true)
        }
        return manageMovieUseCase.getMovieGenres()
    }

    private fun onFetchMovieGenresSuccess(genres: List<Genre>) {
        updateState {
            copy(
                movieGenres = genres.map { it.toState() },
                isLoading = false,
                isNoInternetConnection = false
            )
        }
    }

    private fun fetchTvShowGenres() {
        tryToExecute(
            block = ::fetchTvShowGenresOperation,
            onSuccess = ::onFetchTvShowGenresSuccess,
            onError = ::onDataLoadError
        )
    }

    private suspend fun fetchTvShowGenresOperation(): List<Genre> {
        updateState {
            copy(isLoading = true)
        }
        return manageTvShowUseCase.getTvShowGenres()
    }

    private fun onFetchTvShowGenresSuccess(genres: List<Genre>) {
        updateState {
            copy(
                tvShowGenres = genres.map { it.toState() },
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
        emitEffect(TopRatedScreenEffect.NavigateToMediaDetails(id, mediaTypeUiState))
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
        emitEffect(TopRatedScreenEffect.ShowSuccess(message = stringProvider.addToListSuccess))
    }

    override fun onSaveToListFailure() {
        emitEffect(TopRatedScreenEffect.ShowError(message = stringProvider.addToListFailed))
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
        emitEffect(TopRatedScreenEffect.NavigateBack)
    }

    override fun onLoginButtonClick() {
        emitEffect(TopRatedScreenEffect.NavigateToLogin)
    }

    override fun onDismissBottomSheet() {
        updateState {
            copy(showLoginBottomSheet = false)
        }
    }

    override fun onRetryClick() {
        updateUserLoggingStatus()
        fetchMovieGenres()
        fetchTvShowGenres()
        fetchMovies()
        fetchTvShows()
    }

    private fun loadTopRatedMovies(
        genreId: Int?,
    ): Flow<PagingData<MediaItemUiState>> {
        updateState { copy(isLoading = true) }
        return createPagingFlow(
            pagingSourceFactory = {
                createMoviePagingDataSource(
                    genreId = genreId
                )
            },
            mapper = Movie::toState
        )
    }

    private fun loadTopRatedTvShows(
        genreId: Int?,
    ): Flow<PagingData<MediaItemUiState>> {
        updateState { copy(isLoading = true) }
        return createPagingFlow(
            pagingSourceFactory = { createTvShowPagingDataSource(genreId = genreId) },
            mapper = TvShow::toState
        )
    }

    private fun onDataLoadError(e: NovixAppException) {
        if (e is NoNetworkException) {
            updateState { copy(isNoInternetConnection = true) }
            emitEffect(TopRatedScreenEffect.ShowError(message = stringProvider.noInternetConnectionError))
        } else {
            updateState { copy(isNoInternetConnection = false) }
            emitEffect(TopRatedScreenEffect.ShowError(message = stringProvider.somethingWentWrongError))
        }
    }


    private fun createMoviePagingDataSource(
        genreId: Int?,
        onError: ((NovixAppException) -> Unit)? = ::onDataLoadError,
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
        onError: ((NovixAppException) -> Unit)? = ::onDataLoadError,
    ): PagingSource<Int, TvShow> {
        return BasePagingSourceForHome(onError = onError) { page ->
            manageTvShowUseCase.getTopRatedTvShows(
                page = page,
                genreId = genreId
            )
        }
    }
}