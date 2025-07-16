package com.sanaa.presentation.screen.series

import com.sanaa.presentation.details_base.BaseViewModel
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
                    cast = cast.map { it.toCastUiModel() },
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
        TODO("Not yet implemented")
    }

    override fun onReadMoreClicked() {
        TODO("Not yet implemented")
    }

    override fun onViewReviewsClicked(seriesId: Int) {
        TODO("Not yet implemented")
    }

    override fun onActorClicked(actorId: Int) {
        TODO("Not yet implemented")
    }

    override fun onSeasonNumberClicked(seasonNumber: Int) {
        tryToExecute(callee = {
            updateState {
                it.copy(selectedSeason = seasonNumber)
            }
            val season = getSeriesSeasonDetailsUseCase.execute(seriesId, seasonNumber)
            updateState {
                it.copy(season = season.toSeasonUiModel())
            }
        }, onSuccess = {

        })
    }

    override fun onEpisodeClicked(
        seriesId: Int, seasonNumber: Int, episodeNumber: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun onPlayTrailerClicked() {
        TODO("Not yet implemented")
    }

}