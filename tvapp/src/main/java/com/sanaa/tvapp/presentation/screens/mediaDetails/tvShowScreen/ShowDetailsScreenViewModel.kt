package com.sanaa.tvapp.presentation.screens.mediaDetails.tvShowScreen

import androidx.lifecycle.SavedStateHandle
import com.sanaa.tvapp.base.BaseViewModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toActorUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toHistory
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toSeasonUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toTvShowUiModel
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute
import com.sanaa.tvapp.state.SnackData
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.TvShow
import entity.User
import exceptions.NoNetworkException
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import service.VodStringProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.ManageTvShowUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase
import javax.inject.Inject


@HiltViewModel
class ShowDetailsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val checkUserLogin: CheckIfUserIsLoggedInUseCase,
    private val manageTvShowDetails: ManageTvShowUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<TvShowDetailsScreenUiState, TvShowDetailsScreenEffects>(
    initialState = TvShowDetailsScreenUiState(),
    defaultDispatcher = dispatcher
), TvShowScreenInteractionListener {
    val route = ScreensRoute.TvShowDetailsRoute(
        tvShowId = checkNotNull(savedStateHandle["tvShowId"])
    )

    init {
        loadTvShows()
        updateUserLoginState()
    }

    override fun onActorClicked(actorId: Int) {
        emitEffect(TvShowDetailsScreenEffects.NavigateToActorScreen(actorId))
    }

    override fun onSeasonNumberClicked(seasonNumber: Int) {
        if (state.value.selectedSeason == seasonNumber) return
        tryToExecute(
            block = { fetchSeasonDetails(seasonNumber) },
            onSuccess = { updateState { copy(isLoadingEpisodes = false) } },
            onError = ::onErrorAccrue
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

    override fun onRetryLoadDetails() {
        updateState {
            copy(
                isLoading = true,
                noInternetConnection = false
            )
        }
        loadTvShows()
        if (state.value.isUserLoggedIn) {
            fetchUserRating()
        }
    }

    private fun loadTvShows() {
        tryToExecute(
            block = ::fetchTvShowsDetails,
            onSuccess = { updateState { copy(isLoading = false, noInternetConnection = false) } },
            onError = ::onErrorAccrue
        )
    }

    private suspend fun fetchTvShowsDetails() = coroutineScope {
        updateState { copy(isLoading = true) }

        val tvShowsDeferred = async { manageTvShowDetails.getTvShowDetails(route.tvShowId) }
        val castDeferred = async { manageTvShowDetails.getTvShowCast(route.tvShowId) }
        val seasonDeferred = async { manageTvShowDetails.getTvShowSeasonDetails(route.tvShowId, 1) }
        val imagesDeferred = async { manageTvShowDetails.getTvShowImageUrls(route.tvShowId) }
        val trailerDeferred = async { manageTvShowDetails.getTvShowTrailer(route.tvShowId) }

        val tvShow = tvShowsDeferred.await()
        val cast = castDeferred.await()
        val season = seasonDeferred.await()
        val images = imagesDeferred.await()
        val trailer = trailerDeferred.await()

        addTvShowToHistory(tvShow)

        updateState {
            copy(
                tvShows = tvShow.toTvShowUiModel(trailer),
                cast = cast.map { actor -> actor.toActorUiModel() },
                season = season.toSeasonUiModel(),
                backgroundImageUrl = images.firstOrNull() ?: "",
            )
        }
    }

    private fun addTvShowToHistory(tvShow: TvShow) {
        tryToCollect(
            block = { getLoggedInUserUseCase.getLoggedInUser() },
            onCollect = { user -> onCollectUserLogged(user, tvShow) }
        )
    }

    private suspend fun onCollectUserLogged(user: User, tvShow: TvShow) {
        manageWatchedMediaHistoryUseCase.addWatchedMediaHistory(
            mediaHistoryItem = tvShow.toHistory(),
            username = user.username
        )
    }

    private suspend fun fetchSeasonDetails(seasonNumber: Int) {
        updateState { copy(selectedSeason = seasonNumber, isLoadingEpisodes = true) }

        val season = manageTvShowDetails.getTvShowSeasonDetails(route.tvShowId, seasonNumber)

        updateState { copy(season = season.toSeasonUiModel()) }
    }

    override fun onRateClick() {
        updateState {
            if (state.value.isUserLoggedIn)
                copy(showRateDialog = true)
            else
                copy(showLoginDialog = true)
        }
    }

    override fun onRatingChange(rating: Int) {
        updateState { copy(filledStarsCount = rating) }
    }

    override fun onDismissRateDialog() {
        updateState { copy(showLoginDialog = false, filledStarsCount = imdbRating) }
    }

    override fun onSummitRateClick() {
        tryToExecute(
            onStart = { updateState { copy(showRateDialog = false) }},
            block = ::submitTvShowRating,
            onError = ::onErrorAccrue
        )
    }

    override fun onDeleteRateClick() {
        tryToExecute(
            onStart = { updateState { copy(showRateDialog = false) }},
            block = ::deleteRating,
            onError = ::onErrorAccrue
        )
    }

    private suspend fun deleteRating() {
        val isSuccess = manageTvShowDetails.deleteTvShowRate(tvShowId = route.tvShowId)
        if (isSuccess) {
            updateState {
                copy(
                    filledStarsCount = 0,
                    imdbRating = 0,
                    isRatingSubmitted = false,
                    snackBarData = SnackData(
                        message = stringProvider.deleteRatingSuccess,
                        isError = false
                    )
                )
            }
        } else {
            updateState {
                copy(
                    filledStarsCount = imdbRating,
                    snackBarData = SnackData(
                        message = stringProvider.deleteRatingFailed,
                        isError = true
                    )
                )
            }
        }
    }

    override fun onLoginButtonClick() {
        emitEffect(TvShowDetailsScreenEffects.NavigateToLogin)
    }

    override fun onDismissLoginDialog() {
        updateState { copy(showLoginDialog = false) }
    }


    private fun updateUserLoginState() {
        tryToCollect(
            block = { checkUserLogin.isLoggedIn() },
            onCollect = ::onCollectLoggedFlag,
        )
    }

    private fun onCollectLoggedFlag(isLogged: Boolean) {
        if (isLogged) {
            fetchUserRating()
            if (state.value.showLoginDialog) {
                updateState {
                    copy(
                        showLoginDialog = false,
                        showRateDialog = true,
                    )
                }
            }
        }
        updateState { copy(isUserLoggedIn = isLogged) }
    }

    private fun fetchUserRating() {
        tryToExecute(
            block = { getCurrentUserRating(route.tvShowId) },
            onSuccess = ::onFetchUserRatingSuccess,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getCurrentUserRating(id: Int): Int {
        try {
            val user = getLoggedInUserUseCase.getLoggedInUser().first()
            return manageTvShowDetails.getTvShowRating(user.id, id)
        } catch (_: Exception) {
            return 0
        }
    }

    private fun onFetchUserRatingSuccess(rating: Int) {
        updateState {
            copy(
                imdbRating = rating,
                filledStarsCount = rating,
                isRatingSubmitted = rating != 0
            )
        }
    }

    private suspend fun submitTvShowRating() {
        val rating = state.value.filledStarsCount
        val isSendRateSuccess = manageTvShowDetails.addTvShowRate(
            tvShowId = route.tvShowId,
            rating = rating.toFloat()
        )
        if (isSendRateSuccess) {
            updateState {
                copy(
                    imdbRating = rating,
                    isRatingSubmitted = true,
                    snackBarData = SnackData(
                        message = stringProvider.submitRatingSuccess,
                        isError = false
                    )
                )
            }
        } else {
            updateState {
                copy(
                    filledStarsCount = imdbRating,
                    snackBarData = SnackData(
                        message = stringProvider.submitRatingFailed,
                        isError = true
                    )
                )
            }
        }
    }

    private fun onErrorAccrue(e: NovixAppException) {
        if (e is NoNetworkException) {
            updateState {
                copy(
                    noInternetConnection = true,
                    isLoading = false,
                    snackBarData = SnackData(
                        message = stringProvider.noInternetConnectionError,
                        isError = true
                    )
                )
            }
        } else {
            updateState {
                copy(
                    isLoading = false,
                    snackBarData = SnackData(
                        message = stringProvider.somethingWentWrongError,
                        isError = true
                    ),
                    noInternetConnection = false
                )
            }
        }
    }

    override fun onDismissSnackBar() {
        updateState { copy(snackBarData = null) }
    }
}