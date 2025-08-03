package com.sanaa.presentation.screen.myRating

import com.sanaa.presentation.profileBase.BaseViewModel
import com.sanaa.presentation.profileMapper.toRatedMediaUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import javax.inject.Inject

@HiltViewModel
class MyRatingScreenViewModel @Inject constructor(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<MyRatingScreenUiState, MyRatingScreenEffect>(
    initialState = MyRatingScreenUiState(),
    defaultDispatcher = dispatcher
), MyRatingScreenInteractionListener {

    init {
        loadRatedMedia()
    }

    private fun loadRatedMedia() {
        val currentUserId = "user_id_from_session"
        val accountId = 12345L

        updateState { it.copy(isLoading = true) }

        tryToExecute(
            callee = { manageMovieUseCase.getMoviesRate(accountId) },
            onSuccess = { movies ->
                val uiModels = movies.map { it.toRatedMediaUiModel() }
                updateState { it.copy(ratedMovies = uiModels) }
                if (state.value.ratedTvShows.isNotEmpty()) {
                    updateState { it.copy(isLoading = false) }
                }
            },
            onError = ::onDataLoadError
        )

        tryToExecute(
            callee = { manageTvSeriesUseCase.getSeriesRate(accountId) },
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
            updateState { it.copy(isNoInternetConnection = true, isLoading = false, error = null) }
        } else {
            updateState { it.copy(isLoading = false, error = e.message) }
        }
    }

    override fun onBackClick() {
        emitEffect(MyRatingScreenEffect.NavigateBack)
    }

    override fun onTabSelected(tab: MyRatingTab) {
        updateState { it.copy(selectedTab = tab) }
    }
//    override fun onMediaClick(id: Int, mediaType: String) {
//        emitEffect(MyRatingScreenEffect.NavigateToMediaDetails(id, mediaType))
//    }
override fun onDeleteIconClick(mediaId: Int, mediaType: String) {
    if (mediaType == "movie") {
        tryToExecute(
            callee = { manageMovieUseCase.deleteMovieRate(mediaId) },
            onSuccess = { success ->
                if (success) {
                    updateState { it.copy(ratedMovies = it.ratedMovies.filter { movie -> movie.id != mediaId }) }
                    emitEffect(MyRatingScreenEffect.ShowSuccessSnackBar)
                } else {
                    emitEffect(MyRatingScreenEffect.ShowErrorSnackBar)
                }
            },
            onError = { e ->
                emitEffect(MyRatingScreenEffect.ShowErrorSnackBar)
            }
        )
    } else if (mediaType == "tv_show") {
        tryToExecute(
            callee = { manageTvSeriesUseCase.deleteTvSeriesRate(mediaId) },
            onSuccess = { success ->
                if (success) {
                    updateState { it.copy(ratedTvShows = it.ratedTvShows.filter { tvShow -> tvShow.id != mediaId }) }
                    emitEffect(MyRatingScreenEffect.ShowSuccessSnackBar)
                } else {
                    emitEffect(MyRatingScreenEffect.ShowErrorSnackBar)
                }
            },
            onError = { e ->
                emitEffect(MyRatingScreenEffect.ShowErrorSnackBar)
            }
        )
    }
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
}