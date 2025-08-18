package com.sanaa.presentation.screen.tvShow

import androidx.lifecycle.SavedStateHandle
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.GenreUiModel
import com.sanaa.presentation.model.mapper.toActorUiModel
import com.sanaa.presentation.model.mapper.toHistory
import com.sanaa.presentation.model.mapper.toState
import com.sanaa.presentation.navigation.TvShowScreenRoute
import com.sanaa.presentation.screen.movieDetails.LoginPromptType
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.TvShow
import exceptions.NoNetworkException
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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
        fetchUserRating()
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
            callee = { fetchSeasonDetails(seasonNumber) },
            onSuccess = { updateState { copy(isLoadingEpisodes = false) } },
            onError = ::onErrorAccrue
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
            callee = ::submitTvShowRating,
            onError = ::onSubmitRateBottomSheetFailed
        )
        updateState {
            copy(showRateBottomSheet = false)
        }
    }

    private fun onSubmitRateBottomSheetFailed(exception: NovixAppException) {
        updateState {
            copy(
                error = exception.message,
                showRateBottomSheet = false
            )
        }
    }

    override fun onGenreClicked(genre: GenreUiModel) {
        emitEffect(TvShowScreenEffects.NavigateToMovieCategoriesScreen(genre))
    }

    override fun onRetryLoadDetails() {
        updateState {
            copy(
                isLoading = true,
                error = null,
                noInternetConnection = false
            )
        }
        loadTvShow()
    }

    private fun loadTvShow() {
        tryToExecute(
            callee = {
                fetchShowDetails()
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
                        callee = { manageTvShowDetails.getTvShowRating(user.id, route.tvShowId) },
                        onSuccess = { rating ->
                            updateState { copy(imdbRating = rating) }
                        },
                    )
                },
            )
        }
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
            emitEffect(TvShowScreenEffects.ShowSuccessSnackBar)
        } else {
            emitEffect(TvShowScreenEffects.ShowErrorSnackBar)
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

    private fun addTvShowToHistory(tvShow: TvShow) {
        tryToCollect(
            callee = { getLoggedInUserUseCase.getLoggedInUser() },
            onCollect = { user ->
                manageWatchedMediaHistoryUseCase.addWatchedMediaHistory(
                    mediaHistoryItem = tvShow.toHistory(),
                    username = user.username
                )
            }
        )
    }

    private fun onErrorAccrue(exception: NovixAppException) {
        if (exception is NoNetworkException) {
            updateState {
                copy(
                    noInternetConnection = true,
                    isLoadingEpisodes = false
                )
            }
        } else {
            updateState {
                copy(
                    error = exception.message,
                    noInternetConnection = false,
                    isLoadingEpisodes = false
                )
            }
        }
    }
}