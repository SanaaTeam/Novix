package com.sanaa.presentation.screen.episode_details

import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.toActorUiModel
import com.sanaa.presentation.model.toEpisodeUiModel
import details.usecase.ManageEpisodeDetailsUseCase
import details.usecase.ManageTvSeriesDetailsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class EpisodeDetailsScreenViewModel(
    seriesId: Int,
    seasonNumber: Int,
    episodeNumber: Int,
    private val manageEpisodeDetails: ManageEpisodeDetailsUseCase,
    private val manageTvSeriesDetails: ManageTvSeriesDetailsUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : EpisodeDetailsInteractionListener,
    BaseViewModel<EpisodeDetailsScreenUiState, EpisodeDetailsEffects>(EpisodeDetailsScreenUiState(),dispatcher) {

    init {
        loadEpisode(seriesId, seasonNumber, episodeNumber)
    }

    private fun loadEpisode(seriesId: Int, seasonNumber: Int, episodeNumber: Int) {
        tryToExecute(
            callee = {
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
            },
            onSuccess = {
                updateState { it.copy(isLoading = false) }
            },
            onError = {
                updateState { it.copy(error = it.error, isLoading = false) }
            }
        )
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

    override fun onRateClicked() {
        updateState { it.copy(showLoginBottomSheet = true) }
    }
}
