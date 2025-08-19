package com.sanaa.presentation.screen.watchingHistory

import com.sanaa.presentation.model.MediaItemUiModel
import com.sanaa.presentation.profileBase.BaseViewModel
import com.sanaa.presentation.profileMapper.toGenreUiState
import com.sanaa.presentation.profileMapper.toMediaItemUiModel
import com.sanaa.presentation.screen.myRating.MediaTypeUi
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.MediaHistoryItem
import exceptions.NoLoggedInUserException
import exceptions.NoNetworkException
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import service.VodStringProvider
import usecase.GetLoggedInUserUseCase
import usecase.ManageMovieUseCase
import usecase.ManageTvShowUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase
import usecase.search.search_param.MediaType

@HiltViewModel
class WatchingHistoryViewModel @Inject constructor(
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val manageMovieUseCase: ManageMovieUseCase,
    private val manageTvShowUseCase: ManageTvShowUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<WatchingHistoryUiState, WatchingHistoryScreenEffect>(
    WatchingHistoryUiState(),
    dispatcher
), WatchingHistoryInteractionListener {

    init {
        fetchMovieGenres()
        fetchTvShowGenres()
        fetchMovies()
        fetchTvShows()
    }

    private fun fetchMovies(genreId: Int? = null) {
        tryToCollect(
            block = { loadWatchedHistoryMovies(genreId) },
            onCollect = ::onCollectMovies,
            onError = ::onLoadDataError,
        )
    }

    private fun fetchTvShows(genreId: Int? = null) {
        tryToCollect(
            block = { loadWatchedHistoryTvShows(genreId) },
            onCollect = ::onCollectTvShows,
            onError = ::onLoadDataError
        )
    }

    private fun onCollectMovies(mediaList: List<MediaHistoryItem>) {
        updateState {
            copy(
                movieList = mediaList.map { it.toMediaItemUiModel() },
                isLoading = false
            )
        }
    }
    private fun onCollectTvShows(mediaList: List<MediaHistoryItem>) {
        updateState {
            copy(
                tvShowList = mediaList.map { it.toMediaItemUiModel() },
                isLoading = false
            )
        }
    }
    private fun fetchMovieGenres() {
        tryToExecute(
            block = {
                manageMovieUseCase.getMovieGenres().map { it.toGenreUiState() }
            },
            onSuccess = { genres ->
                updateState { copy(movieGenres = genres) }
            },
            onError = { ::onLoadDataError }
        )
    }

    private fun fetchTvShowGenres() {
        tryToExecute(
            block = { manageTvShowUseCase.getTvShowGenres().map { it.toGenreUiState() } },
            onSuccess = { genres -> updateState { copy(tvShowGenres = genres) } },
            onError = { ::onLoadDataError }
        )
    }

    override fun onMediaTabSelection(mediaTypeUi: MediaTypeUi) {
        updateState { copy(selectedMediaTypeUi = mediaTypeUi) }
    }


    override fun onMovieGenreClick(genreId: Int?) {
        if (genreId != state.value.movieSelectedGenreId) {
            updateState { copy(movieSelectedGenreId = genreId) }
            fetchMovies(genreId)
        }
    }

    override fun onTvShowGenreClick(genreId: Int?) {
        if (genreId != state.value.tvShowSelectedGenreId) {
            updateState { copy(tvShowSelectedGenreId = genreId) }
            fetchTvShows(genreId)
        }
    }

    override fun onMediaClick(id: Int, mediaTypeUi: MediaTypeUi) {
        emitEffect(WatchingHistoryScreenEffect.NavigateToMediaDetails(id, mediaTypeUi))
    }

    override fun onSaveIconClick(mediaItem: MediaItemUiModel) {
        updateState {
            copy(
                showSaveToListBottomSheet = true,
                selectedMediaToSave = mediaItem
            )
        }
    }

    override fun onBackClick() {
        emitEffect(WatchingHistoryScreenEffect.NavigateBack)
    }

    private suspend fun loadWatchedHistoryMovies(
        genreId: Int?,
    ): Flow<List<MediaHistoryItem>> {
        updateState { copy(isLoading = true) }
        val user = try {
            getLoggedInUserUseCase.getLoggedInUser()
        } catch (_: NoLoggedInUserException) {
            null
        }
        if (user == null) return flowOf(emptyList())
        return manageWatchedMediaHistoryUseCase.getMediaHistory(
            genreId = genreId,
            mediaType = MediaType.MOVIE,
            username = user.first().username
        )
    }

    private suspend fun loadWatchedHistoryTvShows(
        genreId: Int?,
    ): Flow<List<MediaHistoryItem>> {
        updateState { copy(isLoading = true) }
        val user = try {
            getLoggedInUserUseCase.getLoggedInUser()
        } catch (_: NoLoggedInUserException) {
            null
        }
        if (user == null) return flowOf(emptyList())

        return manageWatchedMediaHistoryUseCase.getMediaHistory(
            genreId = genreId,
            mediaType = MediaType.TV_SHOW,
            username = user.first().username
        )
    }

    private fun onLoadDataError(exception: Throwable) {
        when (exception) {
            is NoNetworkException -> {
                updateState {
                    copy(
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

    override fun onDismissSaveToListBottomSheet() {
        updateState { copy(showSaveToListBottomSheet = false, selectedMediaToSave = null) }
    }

    override fun onCreateNewListClick() {
        updateState { copy(showSaveToListBottomSheet = false, showAddListBottomSheet = true) }
    }


    override fun onDismissAddListBottomSheet() {
        updateState { copy(showAddListBottomSheet = false) }
    }

    override fun onDismissSnack() {
        updateState {
            copy(snackBarData = null)
        }
    }
}