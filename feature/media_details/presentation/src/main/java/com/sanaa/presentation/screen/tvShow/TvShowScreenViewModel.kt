package com.sanaa.presentation.screen.tvShow

import androidx.lifecycle.SavedStateHandle
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.GenreUiModel
import com.sanaa.presentation.model.mapper.toActorUiModel
import com.sanaa.presentation.model.mapper.toHistory
import com.sanaa.presentation.model.mapper.toState
import com.sanaa.presentation.navigation.TvShowScreenRoute
import com.sanaa.presentation.screen.movieDetails.SnackData
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.TvShow
import exceptions.NoNetworkException
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import service.VodStringProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.ManageTvShowUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase
import javax.inject.Inject


@HiltViewModel
class TvShowScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val checkUserLogin: CheckIfUserIsLoggedInUseCase,
    private val getUser: GetLoggedInUserUseCase,
    private val manageTvShowDetails: ManageTvShowUseCase,
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<TvShowScreenUiState, TvShowScreenEffects>(
    initialState = TvShowScreenUiState(),
    defaultDispatcher = dispatcher
), TvShowScreenInteractionListener {
    val route = TvShowScreenRoute(
        tvShowId = checkNotNull(savedStateHandle["tvShowId"]),
    )

    init {
        loadTvShow()
        updateUserLoginState()
    }

    override fun onBackClicked() {
        emitEffect(TvShowScreenEffects.NavigateBack)
    }

    override fun onViewReviewsClicked(tvShowId: Int) {
        emitEffect(TvShowScreenEffects.NavigateToReviewsScreen(tvShowId))
    }

    override fun onActorClicked(actorId: Int) {
        emitEffect(TvShowScreenEffects.NavigateToActorScreen(actorId))
    }

    override fun onSeasonNumberClicked(seasonNumber: Int) {
        if (state.value.selectedSeason == seasonNumber) return
        tryToExecute(
            block = { fetchSeasonDetails(seasonNumber) },
            onSuccess = { updateState { copy(isLoadingEpisodes = false) } },
            onError = ::onErrorLoadingSeasonDetails
        )
    }

    override fun onEpisodeClicked(tvShowId: Int, seasonNumber: Int, episodeNumber: Int) {
        emitEffect(
            TvShowScreenEffects.NavigateToEpisodeDetailsScreen(
                tvShowId, seasonNumber, episodeNumber
            )
        )
    }

    override fun onPlayTrailerClicked() {
        emitEffect(TvShowScreenEffects.PlayTrailer(trailerUrl = state.value.tvShow.trailerUrl))
    }

    override fun onRateClicked() {
        if (state.value.isUserLoggedIn) {
            updateState { copy(showRateBottomSheet = true) }
        } else {
            updateState { copy(showLoginBottomSheet = true) }
        }
    }

    override fun onDismissRateBottomSheet() {
        updateState { copy(showRateBottomSheet = false, imdbRating = 0) }
    }

    override fun onLoginButtonClick() {
        updateState { copy(showLoginBottomSheet = false) }
        emitEffect(TvShowScreenEffects.NavigateToLogin)
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
            onError = ::onSubmitRatingFailed,
        )
    }

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
    }

    override fun onGenreClicked(genre: GenreUiModel) {
        emitEffect(TvShowScreenEffects.NavigateToMovieCategoriesScreen(genre))
    }

    override fun onRetryLoadDetails() {
        updateState {
            copy(
                isLoading = true,
                noInternetConnection = false
            )
        }
        loadTvShow()
    }

    private fun loadTvShow() {
        tryToExecute(
            block = { fetchShowDetails() },
            onSuccess = { updateState { copy(isLoading = false, isError = false) } },
            onError = ::onErrorFetchingData
        )
    }

    private fun fetchUserRating() {
        tryToCollect(
            block = { getUser.getLoggedInUser() },
            onCollect = { user ->
                tryToExecute(
                    block = { manageTvShowDetails.getTvShowRating(user.id, route.tvShowId) },
                    onSuccess = { rating ->
                        updateState {
                            copy(
                                imdbRating = rating,
                                showRateButton = rating == 0
                            )
                        }
                    },
                )
            },
        )
    }

    private suspend fun fetchShowDetails() = coroutineScope {
        updateState { copy(isLoading = true) }

        val tvShowDeferred = async { manageTvShowDetails.getTvShowDetails(route.tvShowId) }
        val castDeferred = async { manageTvShowDetails.getTvShowCast(route.tvShowId) }
        val seasonDeferred = async { manageTvShowDetails.getTvShowSeasonDetails(route.tvShowId, 1) }
        val imagesDeferred = async { manageTvShowDetails.getTvShowImageUrls(route.tvShowId) }
        val trailerDeferred = async { manageTvShowDetails.getTvShowTrailer(route.tvShowId) }

        val tvShow = tvShowDeferred.await()
        val cast = castDeferred.await()
        val season = seasonDeferred.await()
        val images = imagesDeferred.await()
        val trailer = trailerDeferred.await()
        addTvShowToHistory(tvShow)

        updateState {
            copy(
                tvShow = tvShow.toState(trailer),
                cast = cast.map { actor -> actor.toActorUiModel() },
                season = season.toState(),
                images = images,
            )
        }
    }

    private suspend fun fetchSeasonDetails(seasonNumber: Int) {
        updateState { copy(selectedSeason = seasonNumber, isLoadingEpisodes = true) }

        val season = manageTvShowDetails.getTvShowSeasonDetails(route.tvShowId, seasonNumber)

        updateState { copy(season = season.toState()) }
    }

    private suspend fun submitTvShowRating() {
        val isSendRateSuccess = manageTvShowDetails.addTvShowRate(
            tvShowId = route.tvShowId,
            rating = state.value.imdbRating.toFloat()
        )
        if (isSendRateSuccess) {
            updateState {
                copy(
                    snackBarData = SnackData(
                        message = stringProvider.deleteRatingSuccess,
                        isError = false
                    ),
                    showRateBottomSheet = false,
                    showRateButton = false
                )
            }
        } else {
            updateState {
                copy(
                    snackBarData = SnackData(
                        message = stringProvider.deleteRatingFailed,
                        isError = true
                    )
                )
            }
        }
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
        }
        updateState { copy(isUserLoggedIn = isLogged) }
    }

    private fun addTvShowToHistory(tvShow: TvShow) {
        tryToCollect(
            block = { getLoggedInUserUseCase.getLoggedInUser() },
            onCollect = { user ->
                manageWatchedMediaHistoryUseCase.addWatchedMediaHistory(
                    mediaHistoryItem = tvShow.toHistory(),
                    username = user.username
                )
            }
        )
    }

    private fun onErrorFetchingData(exception: NovixAppException) {
        when (exception) {
            is NoNetworkException -> {
                updateState {
                    copy(
                        noInternetConnection = true,
                        isLoadingEpisodes = false,
                        isLoading = false,
                        isError = true,
                        snackBarData = SnackData(
                            message = stringProvider.noInternetConnectionError,
                            isError = true,
                        )
                    )
                }
            }

            else -> {
                updateState {
                    copy(
                        noInternetConnection = false,
                        isLoading = false,
                        isLoadingEpisodes = false,
                        isError = true,
                        snackBarData = SnackData(
                            message = stringProvider.somethingWentWrongError,
                            isError = true
                        )
                    )
                }
            }
        }
    }

    private fun onErrorLoadingSeasonDetails(
        exception: NovixAppException
    ) {
        if (exception is NoNetworkException) {
            updateState {
                copy(
                    snackBarData = SnackData(
                        message = stringProvider.noInternetConnectionError,
                        isError = true
                    ),
                    isLoadingEpisodes = false,
                    isLoading = false,
                    isError = true
                )
            }
        } else {
            updateState {
                copy(
                    snackBarData = SnackData(
                        message = stringProvider.somethingWentWrongError,
                        isError = true
                    ),
                    isLoadingEpisodes = false,
                    isError = true
                )
            }
        }

    }

    private fun onSubmitRatingFailed(exception: NovixAppException) {
        updateState {
            copy(
                snackBarData = SnackData(
                    message = stringProvider.deleteRatingFailed,
                    isError = true
                )
            )
        }
    }
}