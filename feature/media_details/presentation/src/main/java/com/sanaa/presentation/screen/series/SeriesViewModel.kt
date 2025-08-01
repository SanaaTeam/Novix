package com.sanaa.presentation.screen.series

import androidx.lifecycle.SavedStateHandle
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.GenreUiModel
import com.sanaa.presentation.model.mapper.toActorUiModel
import com.sanaa.presentation.model.mapper.toHistory
import com.sanaa.presentation.model.mapper.toSeasonUiModel
import com.sanaa.presentation.model.mapper.toSeriesUiModel
import com.sanaa.presentation.screen.movieDetails.LoginPromptType
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.TvSeries
import exceptions.NoLoggedInUserException
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.ManageTvSeriesUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase
import javax.inject.Inject

@HiltViewModel
class SeriesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val checkUserLogin: CheckIfUserIsLoggedInUseCase,
    private val getUser: GetLoggedInUserUseCase,
    private val manageTvSeriesDetails: ManageTvSeriesUseCase,
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<SeriesScreenUiState, SeriesScreenEffects>(
    initialState = SeriesScreenUiState(),
    defaultDispatcher = dispatcher
), SeriesScreenInteractionListener {

    private val seriesId: Int = checkNotNull(savedStateHandle["seriesId"])

    init {
        loadSeries()
        tryToExecute(callee = ::getUserState)
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
            }, onError = { e ->
                if (e is NoNetworkException) {
                    updateState {
                        it.copy(
                            noInternetConnection = true,
                            isLoadingEpisodes = false
                        )
                    }
                } else {
                    updateState {
                        it.copy(
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
            SeriesScreenEffects.NavigateToEpisodeDetailsScreen(
                seriesId, seasonNumber, episodeNumber
            )
        )
    }

    override fun onPlayTrailerClicked() {
        emitEffect(SeriesScreenEffects.PlayTrailer(trailerUrl = state.value.series.trailerUrl))
    }

    override fun onRateClicked() {
        if (state.value.isUserLoggedIn) {
            updateState { it.copy(showRateBottomSheet = true) }
        } else {
            updateState {
                it.copy(
                    showLoginBottomSheet = true,
                    loginPromptType = LoginPromptType.RATE
                )
            }
        }
    }

    override fun onDismissRateBottomSheet() {
        updateState { it.copy(showRateBottomSheet = false) }
    }

    override fun onDismissAnyBottomSheet() {
        updateState {
            it.copy(
                showRateBottomSheet = false,
                showLoginBottomSheet = false
            )
        }
    }

    override fun onLoginButtonClick() {
        updateState { it.copy(showLoginBottomSheet = false) }
        emitEffect(SeriesScreenEffects.NavigateToLogin)
    }

    override fun onRatingChanged(newRating: Int) {
        updateState { it.copy(imdbRating = newRating) }
    }

    override fun onDismissLoginBottomSheet() {
        updateState { it.copy(showLoginBottomSheet = false) }
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

    override fun onSaveSeriesClicked() {
        val isLoggIn = state.value.isUserLoggedIn
        if (!isLoggIn) {
            updateState {
                it.copy(
                    showLoginBottomSheet = true,
                    loginPromptType = LoginPromptType.BOOKMARK
                )
            }
        }

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
                        state.copy(
                            error = e.message,
                            isLoading = false,
                            noInternetConnection = false
                        )
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
        val currentSeriesRating = runCatching {
            val userId = getUser.getLoggedInUser().id
            val ratedSeries = manageTvSeriesDetails.getSeriesRate(userId)
            ratedSeries.find { it.id == seriesId }?.rating ?: 0
        }.getOrElse { 0 }

        addTvSeriesToHistory(series)
        updateState {
            it.copy(
                series = series.toSeriesUiModel(trailer),
                cast = cast.map { actor -> actor.toActorUiModel() },
                season = season.toSeasonUiModel(),
                images = images,
                imdbRating = currentSeriesRating
            )
        }
    }

    private suspend fun fetchSeasonDetails(seasonNumber: Int) {
        updateState { it.copy(selectedSeason = seasonNumber, isLoadingEpisodes = true) }

        val season = manageTvSeriesDetails.getTvSeriesSeasonDetails(seriesId, seasonNumber)

        updateState { it.copy(season = season.toSeasonUiModel()) }
    }

    private suspend fun addRate() {
        val isSendRateSuccess = manageTvSeriesDetails.addTvSeriesRate(
            seriesId = seriesId,
            rating = state.value.imdbRating.toFloat()
        )
        if (isSendRateSuccess) {
            emitEffect(SeriesScreenEffects.ShowSuccessSnackBar)
        } else {
            emitEffect(SeriesScreenEffects.ShowErrorSnackBar)
        }
    }

    private suspend fun getUserState() {
        val isUserLoggedIn = checkUserLogin.isLoggedIn()
        updateState { it.copy(isUserLoggedIn = isUserLoggedIn) }
    }

    private suspend fun addTvSeriesToHistory(tvSeries: TvSeries) {
        val user = try {
            getLoggedInUserUseCase.getLoggedInUser()
        } catch (_: NoLoggedInUserException) {
            null
        }
        if (user == null) return
        manageWatchedMediaHistoryUseCase.addWatchedMediaHistory(
            mediaHistoryItem = tvSeries.toHistory(),
            username = user.username
        )
    }
}