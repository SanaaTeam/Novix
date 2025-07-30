package com.sanaa.presentation.screen.series

import androidx.lifecycle.SavedStateHandle
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.GenreUiModel
import com.sanaa.presentation.model.toActorUiModel
import com.sanaa.presentation.model.toSeasonUiModel
import com.sanaa.presentation.model.toSeriesUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.ManageTvSeriesUseCase

@HiltViewModel
class SeriesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageTvSeriesDetails: ManageTvSeriesUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<SeriesScreenUiState, SeriesScreenEffects>(
    initialState = SeriesScreenUiState(),
    defaultDispatcher = dispatcher
), SeriesScreenInteractionListener {

    private val seriesId: Int = checkNotNull(savedStateHandle["seriesId"])

    init {
        loadSeries()
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
            callee = { fetchSeasonDetails(seasonNumber) },
            onSuccess = {
                updateState { it.copy(isLoadingEpisodes = false) }
            }
            , onError = {e ->
                if(e is NoNetworkException){
                    updateState { it.copy(noInternetConnection = true,
                        isLoadingEpisodes = false) }
                }
                else {
                    updateState { it.copy( error = e.message,
                        noInternetConnection = false,
                        isLoadingEpisodes = false) }
                }
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

    override fun onLoginButtonClick() {
        updateState { it.copy(showLoginBottomSheet = false) }
        emitEffect(SeriesScreenEffects.NavigateToLogin)
    }

    override fun onSaveSeriesClicked() {
        updateState { it.copy(showLoginBottomSheet = true) }
    }

    override fun onGenreClicked(genre: GenreUiModel) {
        emitEffect(SeriesScreenEffects.NavigateToMovieCategoriesScreen(genre))
    }

    override fun onRetryLoadDetails() {
        updateState {
            it.copy(
                isLoading = true,
                error = null,
                noInternetConnection = false
            )
        }
        loadSeries()
    }
    private fun loadSeries() {
        tryToExecute(
            callee = ::fetchSeriesDetails,
            onSuccess = {
                updateState { it.copy(isLoading = false) }
            },
            onError = { e ->
                if (e is NoNetworkException) {
                    updateState { state ->
                        state.copy(noInternetConnection = true, isLoading = false, error = null)
                    }
                } else {
                    updateState { state ->
                        state.copy(error = e.message, isLoading = false, noInternetConnection = false)
                    }
                }
            }
        )
    }

    private suspend fun fetchSeriesDetails() {
        updateState { it.copy(isLoading = true) }

        val series = manageTvSeriesDetails.getTvSeriesDetails(seriesId)
        val cast = manageTvSeriesDetails.getTvSeriesCast(seriesId)
        val season = manageTvSeriesDetails.getTvSeriesSeasonDetails(seriesId, 1)
        val images = manageTvSeriesDetails.getTvSeriesImages(seriesId)
        val trailer = manageTvSeriesDetails.getTvSeriesTrailer(seriesId)

        updateState {
            it.copy(
                series = series.toSeriesUiModel(trailer),
                cast = cast.map { actor -> actor.toActorUiModel() },
                season = season.toSeasonUiModel(),
                images = images
            )
        }
    }

    private suspend fun fetchSeasonDetails(seasonNumber: Int) {
        updateState { it.copy(selectedSeason = seasonNumber, isLoadingEpisodes = true) }

        val season = manageTvSeriesDetails.getTvSeriesSeasonDetails(seriesId, seasonNumber)

        updateState { it.copy(season = season.toSeasonUiModel()) }
    }
}