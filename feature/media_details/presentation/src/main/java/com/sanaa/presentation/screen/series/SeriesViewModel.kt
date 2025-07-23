package com.sanaa.presentation.screen.series

import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.GenreUiModel
import com.sanaa.presentation.model.toActorUiModel
import com.sanaa.presentation.model.toSeasonUiModel
import com.sanaa.presentation.model.toSeriesUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.ManageTvSeriesUseCase

class SeriesViewModel(
    private val seriesId: Int,
    private val manageTvSeriesDetails: ManageTvSeriesUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<SeriesScreenUiState, SeriesScreenEffects>(
    initialState = SeriesScreenUiState(),
    defaultDispatcher = dispatcher
),
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
            onError = { e ->
                updateState { state ->
                    state.copy(error = e.message, isLoading = false)
                }
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

    override fun onGenreClicked(genre: GenreUiModel) {
        emitEffect(SeriesScreenEffects.NavigateToMovieCategoriesScreen(genre.id, genre.name))
    }
}