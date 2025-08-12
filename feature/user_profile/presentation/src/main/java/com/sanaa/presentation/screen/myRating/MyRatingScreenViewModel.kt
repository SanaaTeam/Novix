package com.sanaa.presentation.screen.myRating

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.presentation.profileBase.BaseViewModel
import com.sanaa.presentation.profileMapper.toRatedMediaUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import service.VodStringProvider
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import javax.inject.Inject

@HiltViewModel
class MyRatingScreenViewModel @Inject constructor(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val preferencesManager: PreferencesManager,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<MyRatingScreenUiState, MyRatingScreenEffect>(
    initialState = MyRatingScreenUiState(),
    defaultDispatcher = dispatcher
), MyRatingScreenInteractionListener {

    init {
        loadRatedMedia()
    }

    private fun loadRatedMedia() {
        updateState { it.copy(isLoading = true) }
        loadRatedMovies()
        loadRatedTvShows()
    }


    private fun loadRatedMovies() {
        tryToExecute(
            block = { manageMovieUseCase.getUserRatedMovies() },
            onSuccess = { movies ->
                val uiModels = movies.map { it.toRatedMediaUiModel() }
                updateState { it.copy(ratedMovies = uiModels) }
                if (state.value.ratedTvShows.isNotEmpty()) {
                    updateState { it.copy(isLoading = false) }
                }
            },
            onError = ::onDataLoadError
        )
    }

    private fun loadRatedTvShows() {
        tryToExecute(
            block = {
                val accountId = preferencesManager.accountId.first()
                val sessionId = preferencesManager.sessionId.first()

                manageTvSeriesUseCase.getUserRatedTvSeries(accountId, sessionId)
            },
            onSuccess = { tvShows ->
                val uiModels = tvShows.map { it.toRatedMediaUiModel() }
                updateState { it.copy(ratedTvShows = uiModels) }
                if (state.value.ratedMovies.isNotEmpty()) {
                    updateState { it.copy(isLoading = false) }
                }
            },
            onError = ::onDataLoadError
        )
    }

    private fun onDataLoadError(e: Throwable) {
        if (e is NoNetworkException) {
            updateState { it.copy(
                isNoInternetConnection = true,
                isLoading = false,
                error = null) }
        } else {
            updateState { it.copy(
                isLoading = false,
                error = e.message
            ) }
        }
    }

    override fun onBackClick() {
        emitEffect(MyRatingScreenEffect.NavigateBack)
    }

    override fun onTabSelected(tab: MyRatingTab) {
        updateState { it.copy(selectedTab = tab) }
    }

    override fun onDeleteIconClick(mediaId: Int, mediaType: MediaTypeUi) {
        when (mediaType) {
            MediaTypeUi.MOVIE -> deleteRatedMovie(mediaId)
            MediaTypeUi.TV_SHOW -> deleteRatedTvShow(mediaId)
        }
    }

    private fun deleteRatedMovie(mediaId: Int) {
        tryToExecute(
            block = { manageMovieUseCase.deleteMovieRate(mediaId) },
            onSuccess = { success ->
                    updateState { it.copy(ratedMovies = it.ratedMovies.filter { movie -> movie.id != mediaId }) }
                    emitEffect(MyRatingScreenEffect.ShowSuccessSnackBar(stringProvider.deleteRatingSuccess))

            },
            onError = {
                emitEffect(MyRatingScreenEffect.ShowErrorSnackBar(stringProvider.deleteRatingFailed))
            }
        )
    }

    private fun deleteRatedTvShow(mediaId: Int) {
        tryToExecute(
            block = { manageTvSeriesUseCase.deleteTvSeriesRate(mediaId) },
            onSuccess = { success ->
                    updateState { it.copy(ratedTvShows = it.ratedTvShows.filter { tvShow -> tvShow.id != mediaId }) }
                    emitEffect(MyRatingScreenEffect.ShowSuccessSnackBar(stringProvider.deleteRatingSuccess))
            },
            onError = {
                emitEffect(MyRatingScreenEffect.ShowErrorSnackBar(stringProvider.deleteRatingFailed))
            }
        )
    }

    override fun onRetryLoadDetails() {
        updateState {
            it.copy(
                isLoading = true,
                error = null,
                isNoInternetConnection = false
            )
        }
        loadRatedMedia()
    }

    override fun onMediaClick(id: Int, mediaType: MediaTypeUi) {
        emitEffect(MyRatingScreenEffect.NavigateToMediaDetails(id, mediaType))
    }
}