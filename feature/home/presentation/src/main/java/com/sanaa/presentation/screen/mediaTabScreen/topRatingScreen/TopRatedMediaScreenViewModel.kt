package com.sanaa.presentation.screen.mediaTabScreen.topRatingScreen

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.map
import com.sanaa.presentation.BaseViewModel
import com.sanaa.presentation.base.BasePagingSourceForHome
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaTypeUi
import com.sanaa.presentation.state.mapper.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Movie
import entity.TvSeries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import repository.SavedListsStatusProvider
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
        onDismissBottomSheet()
    }

    private fun fetchMovies(genreId: Int? = null) {
        tryToExecute(
            callee = {
                loadTopRatedMovies(genreId = genreId)
                    .combine(savedListsStatusProvider.savedIds) { pagingData, savedIds ->
                        pagingData.map { mediaItem ->
                            mediaItem.copy(isSaved = savedIds.contains(mediaItem.id))
                        }
                    }.cachedIn(viewModelScope)
            }, onSuccess = { mediaListFlow ->
                updateState {
                    it.copy(movieList = mediaListFlow, isLoading = false)
                }
            },
            onError = { exception ->
                updateState {
                    it.copy(error = exception.message, isLoading = false)
                }
            }
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
            onError = { exception ->
                updateState {
                    it.copy(error = exception.message, isLoading = false)
                }
            }
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
            onError = { exception ->
                updateState {
                    it.copy(error = exception.message, isLoading = false)
                }
            }
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
            onError = { exception ->
                updateState {
                    it.copy(error = exception.message, isLoading = false)
                }
            }
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
            updateState { it.copy(showLoginBottomSheet = true) }
            return
        }

        if (media.isSaved) {
            savedListsStatusProvider.markItemUnsaved(media.id)
        } else {
            updateState {
                it.copy(
                    showSaveToListBottomSheet = true,
                    selectedMediaToSave = media
                )
            }
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


    private fun createMoviePagingDataSource(
        genreId: Int?
    ): PagingSource<Int, Movie> {
        return BasePagingSourceForHome { page ->
            manageMovieUseCase.getTopRatedMovies(
                page = page,
                genreId = genreId
            )
        }
    }

    private fun createTvShowPagingDataSource(
        genreId: Int?
    ): PagingSource<Int, TvSeries> {
        return BasePagingSourceForHome { page ->
            manageTvSeriesUseCase.getTopRatedTvSeries(
                page = page,
                genreId = genreId
            )
        }
    }
}