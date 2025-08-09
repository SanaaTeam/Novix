package com.sanaa.presentation.screen.watchingHistory

import com.sanaa.presentation.model.MediaItemUiModel
import com.sanaa.presentation.profileBase.BaseViewModel
import com.sanaa.presentation.profileMapper.toGenreUiState
import com.sanaa.presentation.profileMapper.toMediaItemUiModel
import com.sanaa.presentation.screen.myRating.MediaTypeUi
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.MediaHistoryItem
import exceptions.NoLoggedInUserException
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import repository.SavedMovieStatusProvider
import usecase.GetLoggedInUserUseCase
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase
import usecase.search.search_param.MediaType

@HiltViewModel
class WatchingHistoryViewModel @Inject constructor(
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val manageMovieUseCase: ManageMovieUseCase,
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val savedMovieStatusProvider: SavedMovieStatusProvider,

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
            callee = {
                loadWatchedHistoryMovies(genreId)
            }, onCollect = { mediaList ->
                updateState {
                    it.copy(
                        movieList = mediaList.map { it.toMediaItemUiModel() },
                        isLoading = false
                    )
                }
            },
            onError = { ::onLoadDataError }
        )
    }

    private fun fetchTvShows(genreId: Int? = null) {
        tryToCollect(
            callee = {
                loadWatchedHistoryTvSeries(genreId)
            }, onCollect = { mediaList ->
                updateState {
                    it.copy(
                        tvShowList = mediaList.map { it.toMediaItemUiModel() },
                        isLoading = false
                    )
                }
            },
            onError = { ::onLoadDataError }
        )
    }

    private fun fetchMovieGenres() {
        tryToExecute(
            callee = {
                manageMovieUseCase.getMovieGenres().map { it.toGenreUiState() }
            },
            onSuccess = { genres ->
                updateState {
                    it.copy(movieGenres = genres)
                }
            },
            onError = { ::onLoadDataError }
        )
    }

    private fun fetchTvShowGenres() {
        tryToExecute(
            callee = {
                manageTvSeriesUseCase.getSeriesGenres().map { it.toGenreUiState() }
            },
            onSuccess = { genres ->
                updateState {
                    it.copy(tvShowGenres = genres)
                }
            },
            onError = { ::onLoadDataError }
        )
    }

    override fun onMediaTabSelection(mediaTypeUi: MediaTypeUi) {
        updateState { it.copy(selectedMediaTypeUi = mediaTypeUi) }
    }


    override fun onMovieGenreClick(genreId: Int?) {
        if (genreId != state.value.movieSelectedGenreId) {
            updateState { it.copy(movieSelectedGenreId = genreId) }
            fetchMovies(genreId)
        }
    }

    override fun onTvShowGenreClick(genreId: Int?) {
        if (genreId != state.value.tvShowSelectedGenreId) {
            updateState { it.copy(tvShowSelectedGenreId = genreId) }
            fetchTvShows(genreId)
        }
    }

    override fun onMediaClick(id: Int, mediaTypeUi: MediaTypeUi) {
        emitEffect(WatchingHistoryScreenEffect.NavigateToMediaDetails(id, mediaTypeUi))
    }

    override fun onSaveIconClick(mediaItem: MediaItemUiModel) {
        if (mediaItem.isSaved) {
            savedMovieStatusProvider.markUnsaved(mediaItem.id)
        } else {
            updateState {
                it.copy(
                    showSaveToListBottomSheet = true,
                    selectedMediaToSave = mediaItem
                )
            }
        }
    }

    override fun onBackClick() {
        emitEffect(WatchingHistoryScreenEffect.NavigateBack)
    }

    private suspend fun loadWatchedHistoryMovies(
        genreId: Int?
    ): Flow<List<MediaHistoryItem>> {
        updateState { it.copy(isLoading = true) }
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

    private suspend fun loadWatchedHistoryTvSeries(
        genreId: Int?
    ): Flow<List<MediaHistoryItem>> {
        updateState { it.copy(isLoading = true) }
        val user = try {
            getLoggedInUserUseCase.getLoggedInUser()
        } catch (_: NoLoggedInUserException) {
            null
        }
        if (user == null) return flowOf(emptyList())

        return manageWatchedMediaHistoryUseCase.getMediaHistory(
            genreId = genreId,
            mediaType = MediaType.TV_SERIES,
            username = user.first().username
        )
    }

    private fun onLoadDataError(exception: Throwable) {
        updateState {
            it.copy(
                error = exception.message,
                isLoading = false
            )
        }
    }


    override fun onDismissSaveToListBottomSheet() {
        updateState { it.copy(showSaveToListBottomSheet = false, selectedMediaToSave = null) }
    }

    override fun onCreateNewListClick() {
        updateState { it.copy(showSaveToListBottomSheet = false, showAddListBottomSheet = true) }
    }

    override fun onDismissAddListBottomSheet() {
        updateState { it.copy(showAddListBottomSheet = false) }
    }
}