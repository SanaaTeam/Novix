package com.sanaa.tvapp.presentation.screens.mediaDetails.episodeScreen

import androidx.lifecycle.SavedStateHandle
import com.sanaa.tvapp.base.BaseViewModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toActorUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toEpisodeUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import usecase.ManageEpisodeDetailsUseCase
import usecase.ManageTvShowUseCase
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageEpisodeDetails: ManageEpisodeDetailsUseCase,
    private val manageTvShowUseCase: ManageTvShowUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
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
    }


    override fun onPlayTrailerClick() {
        emitEffect(EpisodeDetailsEffects.PlayTrailer(state.value.trailerUrl))
    }

    override fun onRetryLoadDetails() {
        updateState { copy(noInternetConnection = false, isLoading = true, error = null) }
        loadEpisode(state.value.seriesId, 0, 0)
    }

    private fun loadEpisode(seriesId: Int, seasonNumber: Int, episodeNumber: Int) {
        tryToExecute(
            block = { fetchEpisodeDetails(seriesId, seasonNumber, episodeNumber) },
            onSuccess = {
                updateState { copy(isLoading = false) }
            },
            onError = { e ->
                if (e is NoNetworkException) {
                    updateState {
                        copy(
                            noInternetConnection = true,
                            error = null,
                            isLoading = false
                        )
                    }
                } else {
                    updateState { copy(error = error, isLoading = false) }
                }
            }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun fetchEpisodeDetails(seriesId: Int, seasonNumber: Int, episodeNumber: Int) =
        coroutineScope {
            updateState { copy(isLoading = true) }

            val episodeDeferred = async {
                manageEpisodeDetails.getEpisodeDetails(
                    seriesId,
                    seasonNumber,
                    episodeNumber
                )
            }
            val guestsDeferred = async {
                manageEpisodeDetails.getEpisodeGuestsOfHonor(
                    seriesId,
                    seasonNumber,
                    episodeNumber
                )
            }
            val imagesDeferred = async { manageTvShowUseCase.getTvShowImageUrls(seriesId) }
            val trailerDeferred = async { manageTvShowUseCase.getTvShowTrailer(seriesId) }

            val episode = episodeDeferred.await()
            val guests = guestsDeferred.await()
            val images = imagesDeferred.await()
            val trailerUrl = trailerDeferred.await()

            updateState {
                copy(
                    episode = episode.toEpisodeUiModel(),
                    guestOfHonor = guests.map { actor -> actor.toActorUiModel() },
                    seriesId = seriesId,
                    backgroundImageUrl = images.firstOrNull().orEmpty(),
                    trailerUrl = trailerUrl,
                )
            }
        }
}