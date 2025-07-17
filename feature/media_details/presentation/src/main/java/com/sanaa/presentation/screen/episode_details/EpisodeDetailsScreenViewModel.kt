package com.sanaa.presentation.screen.episode_details

import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.module.toActorUiModel
import com.sanaa.presentation.module.toEpisodeUiModel
import details.usecase.tv_series.GetEpisodeDetailsUseCase
import details.usecase.tv_series.GetEpisodeGuestsOfHonorUseCase
import details.usecase.tv_series.GetTvSeriesImagesUseCase
import details.usecase.tv_series.GetTvSeriesTrailerUseCase

class EpisodeDetailsScreenViewModel(
    seriesId: Int,
    seasonNumber: Int,
    episodeNumber: Int,
    private val getEpisodeDetailsUseCase: GetEpisodeDetailsUseCase,
    private val getTvSeriesImagesUseCase: GetTvSeriesImagesUseCase,
    private val getEpisodeGuestsOfHonorUseCase: GetEpisodeGuestsOfHonorUseCase,
    private val getTvSeriesVideosUseCase: GetTvSeriesTrailerUseCase,
) : EpisodeDetailsInteractionListener,
    BaseViewModel<EpisodeDetailsScreenUiState, EpisodeDetailsEffects>(initialState = EpisodeDetailsScreenUiState()) {
    init {
        getEpisodeDetails(seriesId, seasonNumber, episodeNumber)
    }

    private fun getEpisodeDetails(seriesId: Int, seasonNumber: Int, episodeNumber: Int) {
        tryToExecute(callee = {
            updateState {
                it.copy(isLoading = true)
            }
            val episode = getEpisodeDetailsUseCase.execute(seriesId, seasonNumber, episodeNumber)
            val cast = getEpisodeGuestsOfHonorUseCase.execute(seriesId, seasonNumber, episodeNumber)
            val images = getTvSeriesImagesUseCase.execute(seriesId)
            val trailer = getTvSeriesVideosUseCase.execute(seriesId)
            updateState {
                it.copy(
                    episode = episode.toEpisodeUiModel(),
                    guestOfHonor = cast.map { it.toActorUiModel() },
                    seriesId = seriesId,
                    imagesUrl = images,
                    trailerUrl = trailer
                )

            }
        }, onSuccess = {
            updateState {
                it.copy(isLoading = false)
            }
        }, onError = {
            updateState {
                it.copy(error = it.error, isLoading = false)
            }
        })

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
        updateState {
            it.copy(showSaveBottomSheet = true)
        }
    }

    override fun onDismissBottomSheet() {
        updateState {
            it.copy(showSaveBottomSheet = false)
        }
    }
}