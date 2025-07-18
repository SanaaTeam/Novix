package com.sanaa.presentation.screen.series

import android.util.Log
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.toActorUiModel
import com.sanaa.presentation.model.toSeasonUiModel
import com.sanaa.presentation.model.toSeriesUiModel
import details.usecase.ManageTvSeriesDetailsUseCase
import entity.Genre

class SeriesViewModel(
    private val seriesId: Int,
    private val manageTvSeriesDetails: ManageTvSeriesDetailsUseCase
) : BaseViewModel<SeriesScreenUiState, SeriesScreenEffects>(SeriesScreenUiState()),
    SeriesScreenInteractionListener {

    init {
        loadSeries()
    }

    private fun loadSeries() {
        tryToExecute(
            callee = {
                updateState { it.copy(isLoading = true) }
                val series = manageTvSeriesDetails.getTvSeriesDetails(seriesId)
                val cast = manageTvSeriesDetails.getTvSeriesCast(seriesId)
                val season = manageTvSeriesDetails.getTvSeriesSeasonDetails(seriesId, 1)
                val images = manageTvSeriesDetails.getTvSeriesImages(seriesId)
                val trailer = manageTvSeriesDetails.getTvSeriesTrailer(seriesId)
                updateState {
                    it.copy(
                        series = series.toSeriesUiModel(trailer),
                        cast = cast.map { a -> a.toActorUiModel() },
                        season = season.toSeasonUiModel(),
                        images = images
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
                updateState { it.copy(selectedSeason = seasonNumber, isLoadingEpisodes = true) }
                val season = manageTvSeriesDetails.getTvSeriesSeasonDetails(seriesId, seasonNumber)
                updateState { it.copy(season = season.toSeasonUiModel()) }
            },
            onSuccess = {
                updateState { it.copy(isLoadingEpisodes = false) }
            }
        )
    }

    override fun onEpisodeClicked(seriesId: Int, seasonNumber: Int, episodeNumber: Int) {
        emitEffect(
            SeriesScreenEffects.NavigateToEpisodeDetailsScreen(
                seriesId, seasonNumber, episodeNumber
            )
        )
    }

    override fun onPlayTrailerClicked() {
        emitEffect(SeriesScreenEffects.PlayTrailer(trailerUrl = state.value.series.trailerUrl))
    }

    override fun onRateClicked() {
        updateState { it.copy(showLoginBottomSheet = true) }
    }

    override fun onDismissRateBottomSheet() {
        updateState { it.copy(showLoginBottomSheet = false) }
    }

    override fun onSaveSeriesClicked() {
        updateState { it.copy(showLoginBottomSheet = true) }
    }

    override fun onGenreClicked(genre: Genre) {
        emitEffect(SeriesScreenEffects.NavigateToMovieCategoriesScreen(genre.name))
    }
}