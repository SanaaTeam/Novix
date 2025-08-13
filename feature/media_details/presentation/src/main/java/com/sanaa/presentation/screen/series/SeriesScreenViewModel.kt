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
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.ManageTvSeriesUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase
import javax.inject.Inject

@HiltViewModel
class SeriesScreenViewModel @Inject constructor(
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

    private val seriesId: Int = checkNotNull(savedStateHandle["seriesId"]) {
        "seriesId is required in SavedStateHandle"
    }

    init {
        loadSeries()
        fetchUserRating()
        updateUserLoginState()
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
            onSuccess = { updateState { copy(isLoadingEpisodes = false) } },
            onError = ::onErrorAccrue
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
        emitEffect(SeriesScreenEffects.NavigateToLogin)
    }

    override fun onRatingChanged(newRating: Int) {
        updateState { copy(imdbRating = newRating) }
    }

    override fun onDismissLoginBottomSheet() {
        updateState { copy(showLoginBottomSheet = false) }
    }

    override fun onSubmitRateBottomSheet() {
        tryToExecute(
            callee = ::submitTvSeriesRating,
            onError = ::onSubmitRateBottomSheetFailed
        )
        updateState {
            copy(showRateBottomSheet = false)
        }
    }

    private fun onSubmitRateBottomSheetFailed(throwable: Throwable) {
        updateState {
            copy(
                error = throwable.message,
                showRateBottomSheet = false
            )
        }
    }

    override fun onSaveSeriesClicked() {
        val isLoggIn = state.value.isUserLoggedIn
        if (!isLoggIn) {
            promptLogin(LoginPromptType.BOOKMARK)
        }
    }

    override fun onGenreClicked(genre: GenreUiModel) {
        emitEffect(SeriesScreenEffects.NavigateToMovieCategoriesScreen(genre))
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
            callee = {
                fetchSeriesDetails()
            },
            onSuccess = {
                updateState { copy(isLoading = false) }
            },
            onError = ::onErrorAccrue
        )
    }

    private fun fetchUserRating() {
        if (state.value.isUserLoggedIn) {
            tryToCollect(
                callee = { getUser.getLoggedInUser() },
                onCollect = { user ->
                    tryToExecute(
                        callee = { manageTvSeriesDetails.getSeriesRate(user.id, seriesId) },
                        onSuccess = { rating ->
                            updateState { copy(imdbRating = rating) }
                        },
                    )
                },
            )
        }
    }

    private suspend fun fetchSeriesDetails() = coroutineScope {
        updateState { copy(isLoading = true) }

        val seriesDeferred = async { manageTvSeriesDetails.getTvSeriesDetails(seriesId) }
        val castDeferred = async { manageTvSeriesDetails.getTvSeriesCast(seriesId) }
        val seasonDeferred = async { manageTvSeriesDetails.getTvSeriesSeasonDetails(seriesId, 1) }
        val imagesDeferred = async { manageTvSeriesDetails.getTvSeriesImages(seriesId) }
        val trailerDeferred = async { manageTvSeriesDetails.getTvSeriesTrailer(seriesId) }


        val series = seriesDeferred.await()
        val cast = castDeferred.await()
        val season = seasonDeferred.await()
        val images = imagesDeferred.await()
        val trailer = trailerDeferred.await()
        addTvSeriesToHistory(series)

        updateState {
            copy(
                series = series.toSeriesUiModel(trailer),
                cast = cast.map { actor -> actor.toActorUiModel() },
                season = season.toSeasonUiModel(),
                images = images,
            )
        }
    }


    private suspend fun fetchSeasonDetails(seasonNumber: Int) {
        updateState { copy(selectedSeason = seasonNumber, isLoadingEpisodes = true) }

        val season = manageTvSeriesDetails.getTvSeriesSeasonDetails(seriesId, seasonNumber)

        updateState { copy(season = season.toSeasonUiModel()) }
    }


    private suspend fun submitTvSeriesRating() {
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

    private fun updateUserLoginState() {
        tryToCollect(
            callee = { checkUserLogin.isLoggedIn() },
            onCollect = ::onCollectLoggedFlag,
        )
    }

    private fun onCollectLoggedFlag(isLogged: Boolean) {
        updateState { copy(isUserLoggedIn = isLogged) }
    }

    private fun promptLogin(type: LoginPromptType) {
        updateState {
            copy(
                showLoginBottomSheet = true,
                loginPromptType = type
            )
        }
    }

    private fun addTvSeriesToHistory(tvSeries: TvSeries) {
        tryToCollect(
            callee = { getLoggedInUserUseCase.getLoggedInUser() },
            onCollect = { user ->
                manageWatchedMediaHistoryUseCase.addWatchedMediaHistory(
                    mediaHistoryItem = tvSeries.toHistory(),
                    username = user.username
                )
            }
        )
    }

    private fun onErrorAccrue(throwable: Throwable) {
        if (throwable is NoNetworkException) {
            updateState {
                copy(
                    noInternetConnection = true,
                    isLoadingEpisodes = false
                )
            }
        } else {
            updateState {
                copy(
                    error = throwable.message,
                    noInternetConnection = false,
                    isLoadingEpisodes = false
                )
            }
        }
    }
}