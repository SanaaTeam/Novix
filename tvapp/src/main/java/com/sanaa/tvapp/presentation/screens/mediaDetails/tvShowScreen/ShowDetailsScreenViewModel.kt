package com.sanaa.tvapp.presentation.screens.mediaDetails.tvShowScreen

import androidx.lifecycle.SavedStateHandle
import com.sanaa.tvapp.base.BaseViewModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.GenreUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toActorUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toSeasonUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toTvShowUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import usecase.ManageTvShowUseCase
import javax.inject.Inject


@HiltViewModel
class ShowDetailsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageTvShowDetails: ManageTvShowUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<TvShowDetailsScreenUiState, TvShowDetailsScreenEffects>(
    initialState = TvShowDetailsScreenUiState(),
    defaultDispatcher = dispatcher
), TvShowScreenInteractionListener {

    private val tvShowId: Int = checkNotNull(savedStateHandle["seriesId"]) {
        "seriesId is required in SavedStateHandle"
    }


    init {
        loadSeries()
    }


    override fun onActorClicked(actorId: Int) {
        emitEffect(TvShowDetailsScreenEffects.NavigateToActorScreen(actorId))
    }

    override fun onSeasonNumberClicked(seasonNumber: Int) {
        if (state.value.selectedSeason == seasonNumber) return
        tryToExecute(
            block = { fetchSeasonDetails(seasonNumber) },
            onSuccess = {
                updateState { copy(isLoadingEpisodes = false) }
            }, onError = { e ->
                if (e is NoNetworkException) {
                    updateState {
                        copy(
                            noInternetConnection = true,
                            isLoadingEpisodes = false
                        )
                    }
                } else {
                    updateState {
                        copy(
                            error = e.message,
                            noInternetConnection = false,
                            isLoadingEpisodes = false
                        )
                    }
                }
            }
        )
    }

    override fun onEpisodeClicked(seriesId: Int, seasonNumber: Int, episodeNumber: Int) {
        emitEffect(
            TvShowDetailsScreenEffects.NavigateToEpisodeDetailsScreen(
                seriesId, seasonNumber, episodeNumber
            )
        )
    }

    override fun onPlayTrailerClicked() {
        emitEffect(TvShowDetailsScreenEffects.PlayTrailer(trailerUrl = state.value.tvShows.trailerUrl))
    }

    override fun onRateClicked() {
        if (state.value.isUserLoggedIn) {
            updateState { copy(showRateBottomSheet = true) }
        } else {
            promptLogin(LoginPromptType.RATE)
        }
    }

    override fun onDismissRateBottomSheet() {
        updateState { copy(showRateBottomSheet = false) }
    }

    override fun onDismissAnyBottomSheet() {
        updateState {
            copy(
                showRateBottomSheet = false,
                showLoginBottomSheet = false
            )
        }
    }

    override fun onLoginButtonClick() {
        updateState { copy(showLoginBottomSheet = false) }
        emitEffect(TvShowDetailsScreenEffects.NavigateToLogin)
    }

    override fun onRatingChanged(newRating: Int) {
        updateState { copy(imdbRating = newRating) }
    }

    override fun onDismissLoginBottomSheet() {
        updateState { copy(showLoginBottomSheet = false) }
    }

    override fun onSubmitRateBottomSheet() {
        tryToExecute(
            block = ::submitTvShowRating,
            onError = { exception ->
                updateState {
                    copy(
                        error = exception.message,
                        showRateBottomSheet = false
                    )
                }
            }
        )
        updateState {
            copy(showRateBottomSheet = false)
        }
    }

    override fun onSaveSeriesClicked() {
        val isLoggIn = state.value.isUserLoggedIn
        if (!isLoggIn) {
            promptLogin(LoginPromptType.BOOKMARK)
        }
    }

    override fun onGenreClicked(genre: GenreUiModel) {
        emitEffect(TvShowDetailsScreenEffects.NavigateToMovieCategoriesScreen(genre))
    }

    override fun onRetryLoadDetails() {
        updateState {
            copy(
                isLoading = true,
                error = null,
                noInternetConnection = false
            )
        }
        loadSeries()
    }

    private fun loadSeries() {
        tryToExecute(
            block = ::fetchSeriesDetails,
            onSuccess = {
                updateState { copy(isLoading = false) }
            },
            onError = { e ->
                if (e is NoNetworkException) {
                    updateState {
                        copy(noInternetConnection = true, isLoading = false, error = null)
                    }
                } else {
                    updateState {
                        copy(
                            error = e.message,
                            isLoading = false,
                            noInternetConnection = false
                        )
                    }
                }
            }
        )
    }

    private suspend fun fetchSeriesDetails() = coroutineScope {
        updateState { copy(isLoading = true) }

        val seriesDeferred = async { manageTvShowDetails.getTvShowDetails(tvShowId) }
        val castDeferred = async { manageTvShowDetails.getTvShowCast(tvShowId) }
        val seasonDeferred = async { manageTvShowDetails.getTvShowSeasonDetails(tvShowId, 1) }
        val imagesDeferred = async { manageTvShowDetails.getTvShowImageUrls(tvShowId) }
        val trailerDeferred = async { manageTvShowDetails.getTvShowTrailer(tvShowId) }

        val series = seriesDeferred.await()
        val cast = castDeferred.await()
        val season = seasonDeferred.await()
        val images = imagesDeferred.await()
        val trailer = trailerDeferred.await()

        updateState {
            copy(
                tvShows = series.toTvShowUiModel(trailer),
                cast = cast.map { actor -> actor.toActorUiModel() },
                season = season.toSeasonUiModel(),
                backgroundImageUrl = images.firstOrNull() ?: "",
            )
        }
    }

    private suspend fun fetchSeasonDetails(seasonNumber: Int) {
        updateState { copy(selectedSeason = seasonNumber, isLoadingEpisodes = true) }

        val season = manageTvShowDetails.getTvShowSeasonDetails(tvShowId, seasonNumber)

        updateState { copy(season = season.toSeasonUiModel()) }
    }

    private suspend fun submitTvShowRating() {
        val isSendRateSuccess = manageTvShowDetails.addTvShowRate(
            tvShowId = tvShowId,
            rating = state.value.imdbRating.toFloat()
        )
        if (isSendRateSuccess) {
            emitEffect(TvShowDetailsScreenEffects.ShowSuccessSnackBar)
        } else {
            emitEffect(TvShowDetailsScreenEffects.ShowErrorSnackBar)
        }
    }


    private fun promptLogin(type: LoginPromptType) {
        updateState {
            copy(
                showLoginBottomSheet = true,
                loginPromptType = type
            )
        }
    }
}