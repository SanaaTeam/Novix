package com.sanaa.presentation.screen.episodeDetails

import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.toActorUiModel
import com.sanaa.presentation.model.toEpisodeUiModel
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageEpisodeDetailsUseCase
import usecase.ManageTvSeriesUseCase

class EpisodeDetailsScreenViewModel(
    private val seriesId: Int,
    private val seasonNumber: Int,
    private val episodeNumber: Int,
    private val checkUserLogin : CheckIfUserIsLoggedInUseCase,
    private val manageEpisodeDetails: ManageEpisodeDetailsUseCase,
    private val manageTvSeriesDetails: ManageTvSeriesUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : EpisodeDetailsInteractionListener,
    BaseViewModel<EpisodeDetailsScreenUiState, EpisodeDetailsEffects>(
        EpisodeDetailsScreenUiState(),
        defaultDispatcher = dispatcher
    ) {

    init {
        loadEpisode(seriesId, seasonNumber, episodeNumber)
        tryToExecute(callee = ::getUserState)
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
        updateState { it.copy(showLoginBottomSheet = true) }
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
            updateState { it.copy(showLoginBottomSheet = true) }
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
            callee = ::addRate,
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
        updateState {
            it.copy(
                episode = episode.toEpisodeUiModel(),
                guestOfHonor = guests.map { actor -> actor.toActorUiModel() },
                seriesId = seriesId,
                imagesUrl = images,
                trailerUrl = trailerUrl
            )
        }
    }

    private suspend fun addRate() {
        manageEpisodeDetails.addTvEpisodeRate(
            seriesId = seriesId,
            episodeNumber = episodeNumber,
            seasonNumber = seasonNumber,
            rating = state.value.imdbRating.toFloat()
        )
    }

    private suspend fun getUserState(){
        val isUserLoggedIn = checkUserLogin.isLoggedIn()
        updateState { it.copy(isUserLoggedIn = isUserLoggedIn) }
    }
}
