package com.sanaa.presentation.screen.myRating

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.presentation.profileBase.BaseViewModel
import com.sanaa.presentation.profileMapper.toRatedMediaUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Movie
import entity.TvShow
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import service.VodStringProvider
import usecase.ManageMovieUseCase
import usecase.ManageTvShowUseCase
import javax.inject.Inject

@HiltViewModel
class MyRatingScreenViewModel @Inject constructor(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val manageTvShowUseCase: ManageTvShowUseCase,
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
        updateState { copy(isLoading = true) }
        loadRatedMovies()
        loadRatedTvShows()
    }


    private fun loadRatedMovies() {
        tryToExecute(
            block = { manageMovieUseCase.getUserRatedMovies() },
            onSuccess = ::onLoadMoviesSuccess,
            onError = ::onDataLoadError
        )
    }

    private fun loadRatedTvShows() {
        tryToExecute(
            block = {
                val accountId = preferencesManager.accountId.first()
                val sessionId = preferencesManager.sessionId.first()
                manageTvShowUseCase.getRatedTvShows(accountId, sessionId)
            },
            onSuccess = ::onLoadTvShowsSuccess,
            onError = ::onDataLoadError
        )
    }

    private fun onDataLoadError(e: Throwable) {
        when (e) {
            is NoNetworkException -> {
                updateState {
                    copy(
                        isNoInternetConnection = true,
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
                        isNoInternetConnection = false,
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

    override fun onBackClick() {
        emitEffect(MyRatingScreenEffect.NavigateBack)
    }

    override fun onTabSelected(tab: MyRatingTab) {
        updateState { copy(selectedTab = tab) }
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
            onSuccess = { onDeleteRatedMovieSuccess(mediaId) },
            onError = { onShowErrorSnackBar(stringProvider.deleteRatingFailed) }
        )
    }

    private fun deleteRatedTvShow(mediaId: Int) {
        tryToExecute(
            block = { manageTvShowUseCase.deleteTvShowRate(mediaId) },
            onSuccess = { onDeleteRatedTvShowSuccess(mediaId) },
            onError = { onShowErrorSnackBar(stringProvider.deleteRatingFailed) }
        )
    }

    override fun onRetryLoadDetails() {
        updateState {
            copy(
                isLoading = true,
                isNoInternetConnection = false
            )
        }
        loadRatedMedia()
    }

    override fun onMediaClick(id: Int, mediaType: MediaTypeUi) {
        emitEffect(MyRatingScreenEffect.NavigateToMediaDetails(id, mediaType))
    }

    override fun onDismissSnack() {
        updateState { copy(snackBarData = null) }
    }

    override fun onShowSuccessSnackBar(message: String) {
        updateState { copy(snackBarData = SnackData(message = message, isError = false)) }
    }

    override fun onShowErrorSnackBar(message: String) {
        updateState { copy(snackBarData = SnackData(message = message, isError = true)) }
    }

    private fun onLoadMoviesSuccess(movies: List<Movie>) {
        updateState {
            copy(
                ratedMovies = movies.map { it.toRatedMediaUiModel() },
                isLoading = false
            )
        }
    }

    private fun onLoadTvShowsSuccess(tvShows: List<TvShow>) {
        updateState {
            copy(
                ratedTvShows = tvShows.map { it.toRatedMediaUiModel() },
                isLoading = false
            )
        }
    }

    private fun onDeleteRatedTvShowSuccess(mediaId: Int) {
        updateState { copy(ratedTvShows = ratedTvShows.filter { tvShow -> tvShow.id != mediaId }) }
        onShowSuccessSnackBar(stringProvider.deleteRatingSuccess)
    }

    private fun onDeleteRatedMovieSuccess(mediaId: Int) {
        updateState { copy(ratedMovies = ratedMovies.filter { movie -> movie.id != mediaId }) }
        onShowSuccessSnackBar(stringProvider.deleteRatingSuccess)
    }

}