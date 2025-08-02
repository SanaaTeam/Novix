package com.sanaa.presentation.screen.episodeDetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.toActorUiModel
import com.sanaa.presentation.model.toEpisodeUiModel
import com.sanaa.presentation.screen.movieDetails.LoginPromptType
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.ManageEpisodeDetailsUseCase
import usecase.ManageTvSeriesUseCase
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUser: GetLoggedInUserUseCase,
    private val checkUserLogin: CheckIfUserIsLoggedInUseCase,
    private val manageEpisodeDetails: ManageEpisodeDetailsUseCase,
    private val manageTvSeriesDetails: ManageTvSeriesUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<EpisodeDetailsScreenUiState, EpisodeDetailsEffects>(
    initialState = EpisodeDetailsScreenUiState(),
    defaultDispatcher = dispatcher
), EpisodeDetailsInteractionListener {

    private val seriesId: Int = checkNotNull(savedStateHandle["seriesId"]) {
        "seriesId is required in SavedStateHandle"
    }
    private val seasonNumber: Int = checkNotNull(savedStateHandle["seasonNumber"]) {
        "seasonNumber is required in SavedStateHandle"
    }
    private val episodeNumber: Int = checkNotNull(savedStateHandle["episodeNumber"]) {
        "episodeNumber is required in SavedStateHandle"
    }

    init {
        loadEpisode(seriesId, seasonNumber, episodeNumber)
        tryToExecute(callee = ::updateUserLoginState)
    }

    override fun onBackClick() {
        emitEffect(EpisodeDetailsEffects.NavigateBack)
    }

    override fun onPlayTrailerClick() {
        emitEffect(EpisodeDetailsEffects.PlayTrailer(state.value.trailerUrl))
    }

    override fun onGenreTypeClick(genreId: Int) {
        emitEffect(EpisodeDetailsEffects.NavigateToActorDetails(genreId))
    }

    override fun onCastClick(actorId: Int) {
        emitEffect(EpisodeDetailsEffects.NavigateToActorDetails(actorId))
    }

    override fun onSavedClick(seriesId: Int) {
        val isLoggIn = state.value.isUserLoggedIn
        if (!isLoggIn) {
            promptLogin(LoginPromptType.BOOKMARK)
        }

    }

    override fun onDismissBottomSheet() {
        updateState { it.copy(showLoginBottomSheet = false) }
    }

    override fun onLoginButtonClick() {
        updateState { it.copy(showLoginBottomSheet = false) }
        emitEffect(EpisodeDetailsEffects.NavigateToLogin)
    }

    override fun onRateClicked() {
        if (state.value.isUserLoggedIn) {
            updateState { it.copy(showRateBottomSheet = true) }
        } else {
            promptLogin(LoginPromptType.RATE)
        }
    }

    override fun onRetryLoadDetails() {
        updateState { it.copy(noInternetConnection = false, isLoading = true, error = null) }
        loadEpisode(state.value.seriesId, 0, 0)
    }

    override fun onRatingChanged(newRating: Int) {
        updateState { it.copy(imdbRating = newRating) }
    }

    override fun onDismissRateBottomSheet() {
        updateState { it.copy(showRateBottomSheet = false) }
    }

    override fun onSubmitRateBottomSheet() {
        tryToExecute(
            callee = ::submitEpisodeRating,
            onError = { exception ->
                updateState {
                    it.copy(
                        error = exception.message,
                        showRateBottomSheet = false
                    )
                }
            }
        )
        updateState {
            it.copy(showRateBottomSheet = false)
        }
    }

    private fun loadEpisode(seriesId: Int, seasonNumber: Int, episodeNumber: Int) {
        tryToExecute(
            callee = { fetchEpisodeDetails(seriesId, seasonNumber, episodeNumber) },
            onSuccess = {
                updateState { it.copy(isLoading = false) }
            },
            onError = { e ->
                if (e is NoNetworkException) {
                    updateState {
                        it.copy(
                            noInternetConnection = true,
                            error = null,
                            isLoading = false
                        )
                    }
                } else {
                    updateState { it.copy(error = it.error, isLoading = false) }
                }
            }
        )
    }

    private suspend fun fetchEpisodeDetails(seriesId: Int, seasonNumber: Int, episodeNumber: Int) {

        updateState { it.copy(isLoading = true) }
        val episode =
            manageEpisodeDetails.getEpisodeDetails(seriesId, seasonNumber, episodeNumber)
        val guests = manageEpisodeDetails.getEpisodeGuestsOfHonor(
            seriesId,
            seasonNumber,
            episodeNumber
        )
        val images = manageTvSeriesDetails.getTvSeriesImages(seriesId)
        val trailerUrl = manageTvSeriesDetails.getTvSeriesTrailer(seriesId)
        val currentEpisodesRating = getCurrentUserRating()
        Log.i("viewModel", "currentEpisodesRating: $currentEpisodesRating")

        updateState {
            it.copy(
                episode = episode.toEpisodeUiModel(),
                guestOfHonor = guests.map { actor -> actor.toActorUiModel() },
                seriesId = seriesId,
                imagesUrl = images,
                trailerUrl = trailerUrl,
                imdbRating = currentEpisodesRating
            )
        }
    }

    private suspend fun getCurrentUserRating(): Int {
        var rating = 0
        tryToExecute(
            callee = {
                val userId = getUser.getLoggedInUser().id
                manageTvSeriesDetails.getEpisodesRate(userId, seasonNumber, episodeNumber)
            },
            onSuccess = { result ->
                rating = result
            }
        )
        return rating
    }

    private suspend fun submitEpisodeRating() {
        val isSendRateSuccess = manageEpisodeDetails.addTvEpisodeRate(
            seriesId = seriesId,
            episodeNumber = episodeNumber,
            seasonNumber = seasonNumber,
            rating = state.value.imdbRating.toFloat()
        )
        if (isSendRateSuccess) {
            emitEffect(EpisodeDetailsEffects.ShowSuccessSnackBar)
        } else {
            emitEffect(EpisodeDetailsEffects.ShowErrorSnackBar)
        }
    }

    private suspend fun updateUserLoginState() {
        val isUserLoggedIn = checkUserLogin.isLoggedIn()
        updateState { it.copy(isUserLoggedIn = isUserLoggedIn) }
    }

    private fun promptLogin(type: LoginPromptType) {
        updateState {
            it.copy(
                showLoginBottomSheet = true,
                loginPromptType = type
            )
        }
    }
}