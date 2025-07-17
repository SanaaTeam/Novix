package com.sanaa.presentation.screen.series

import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.module.toActorUiModel
import com.sanaa.presentation.module.toSeasonUiModel
import com.sanaa.presentation.module.toSeriesUiModel
import details.usecase.tv_series.GetTvSeriesCastUseCase
import details.usecase.tv_series.GetTvSeriesDetailsUseCase
import details.usecase.tv_series.GetTvSeriesImagesUseCase
import details.usecase.tv_series.GetTvSeriesSeasonDetailsUseCase
import details.usecase.tv_series.GetTvSeriesTrailerUseCase

class SeriesViewModel(
    private val seriesId: Int,
    private val getSeriesDetailsUseCase: GetTvSeriesDetailsUseCase,
    private val getSeriesCastUseCase: GetTvSeriesCastUseCase,
    private val getSeriesSeasonDetailsUseCase: GetTvSeriesSeasonDetailsUseCase,
    private val getTvSeriesImagesUseCase: GetTvSeriesImagesUseCase,
    private val getSeriesTrailerUseCase: GetTvSeriesTrailerUseCase,
) : BaseViewModel<SeriesScreenUiState, SeriesScreenEffects>(SeriesScreenUiState()),
    SeriesScreenInteractionListener {
    init {
        getSeriesDetails(seriesId)
    }

    private fun getSeriesDetails(seriesId: Int) {
        tryToExecute(callee = {
            updateState {
                it.copy(isLoading = true)
            }
            val series = getSeriesDetailsUseCase.execute(seriesId)
            val cast = getSeriesCastUseCase.execute(seriesId)
            val season = getSeriesSeasonDetailsUseCase.execute(seriesId, 1)
            val images = getTvSeriesImagesUseCase.execute(seriesId)
            val trailerUrl = getSeriesTrailerUseCase.execute(seriesId)

            updateState {
                it.copy(
                    series = series.toSeriesUiModel(trailerUrl),
                    cast = cast.map { it.toActorUiModel() },
                    season = season.toSeasonUiModel(),
                    images = images,
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

    override fun onBackClicked() {
        emitEffect(SeriesScreenEffects.NavigateBack)
    }

    override fun onViewReviewsClicked(seriesId: Int) {
        emitEffect(SeriesScreenEffects.NavigateToReviewsScreen(seriesId))
    }

    override fun onActorClicked(actorId: Int) {
        emitEffect(SeriesScreenEffects.NavigateToActorScreen(actorId))
    }

    override fun onSeasonNumberClicked(seasonNumber: Int) {
        if (state.value.selectedSeason == seasonNumber) return
        tryToExecute(
            callee = {
                updateState {
                    it.copy(selectedSeason = seasonNumber, isLoadingEpisodes = true)
                }
                val season = getSeriesSeasonDetailsUseCase.execute(seriesId, seasonNumber)
                updateState {
                    it.copy(season = season.toSeasonUiModel())
                }
            },
            onSuccess = {
                updateState {
                    it.copy(isLoadingEpisodes = false)
                }
            },
        )
    }

    override fun onEpisodeClicked(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ) {
        emitEffect(
            SeriesScreenEffects.NavigateToEpisodeDetailsScreen(
                seriesId, seasonNumber, episodeNumber
            )
        )
    }

    override fun onPlayTrailerClicked() {
        emitEffect(
            SeriesScreenEffects.PlayTrailer(
                trailerUrl = state.value.series.trailerUrl
            )
        )
    }

}