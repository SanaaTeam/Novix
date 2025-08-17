package com.sanaa.presentation.screen.episodeDetails

import android.R.attr.rating
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.mapper.toActorUiModel
import com.sanaa.presentation.model.mapper.toState
import com.sanaa.presentation.navigation.EpisodeDetailsScreenRoute
import com.sanaa.presentation.screen.movieDetails.LoginPromptType
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.ManageEpisodeDetailsUseCase
import usecase.ManageTvShowUseCase
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUser: GetLoggedInUserUseCase,
    private val checkUserLogin: CheckIfUserIsLoggedInUseCase,
    private val manageEpisodeDetails: ManageEpisodeDetailsUseCase,
    private val manageTvShowDetails: ManageTvShowUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<EpisodeDetailsScreenUiState, EpisodeDetailsEffects>(
    initialState = EpisodeDetailsScreenUiState(),
    defaultDispatcher = dispatcher
), EpisodeDetailsInteractionListener {
    private val route = EpisodeDetailsScreenRoute(
        tvShowId = checkNotNull(savedStateHandle["seriesId"]),
        seasonNumber = checkNotNull(savedStateHandle["seasonNumber"]),
        episodeNumber = checkNotNull(savedStateHandle["episodeNumber"]),
    )


    init {
        loadEpisode(route.tvShowId, route.seasonNumber, route.episodeNumber)
        updateUserLoginState()
    }

    private fun fetchUserRating() {
        viewModelScope.launch {
            val isLogged = checkUserLogin.isLoggedIn().first()
            if (isLogged) {
                tryToCollect(
                    callee = { getCurrentUserRating() },
                    onCollect = { rating ->
                        updateState { copy(imdbRating = rating) }
                    }
                )
            }
        }
    }

    override fun onBackClick() {
        emitEffect(EpisodeDetailsEffects.NavigateBack)
    }

    override fun onPlayTrailerClick() {
        emitEffect(EpisodeDetailsEffects.PlayTrailer(state.value.trailerUrl))
    }

    override fun onGenreTypeClick(genreId: Int) {
        emitEffect(EpisodeDetailsEffects.NavigateToActorDetails(genreId))
    }

    override fun onCastClick(actorId: Int) {
        emitEffect(EpisodeDetailsEffects.NavigateToActorDetails(actorId))
    }

    override fun onSavedClick(tvShowId: Int) {
        val isLoggIn = state.value.isUserLoggedIn
        if (!isLoggIn) {
            promptLogin(LoginPromptType.BOOKMARK)
        }

    }

    override fun onDismissBottomSheet() {
        updateState { copy(showLoginBottomSheet = false) }
    }

    override fun onLoginButtonClick() {
        updateState { copy(showLoginBottomSheet = false) }
        emitEffect(EpisodeDetailsEffects.NavigateToLogin)
    }

    override fun onRateClicked() {
        if (state.value.isUserLoggedIn) {
            updateState { copy(showRateBottomSheet = true) }
        } else {
            promptLogin(LoginPromptType.RATE)
        }
    }

    override fun onRetryLoadDetails() {
        updateState { copy(noInternetConnection = false, isLoading = true, error = null) }
        loadEpisode(route.tvShowId, route.seasonNumber, route.episodeNumber)
    }

    override fun onRatingChanged(newRating: Int) {
        updateState { copy(imdbRating = newRating) }
    }

    override fun onDismissRateBottomSheet() {
        updateState { copy(showRateBottomSheet = false) }
    }

    override fun onSubmitRateBottomSheet() {
        tryToExecute(
            callee = ::submitEpisodeRating,
            onError = ::onErrorAccrue
        )
        updateState {
            copy(showRateBottomSheet = false)
        }
    }

    private fun onErrorAccrue(exception: NovixAppException) {
        updateState {
            copy(
                error = exception.message,
                showRateBottomSheet = false
            )
        }
    }

    private fun loadEpisode(tvShowId: Int, seasonNumber: Int, episodeNumber: Int) {
        tryToExecute(
            callee = { fetchEpisodeDetails(tvShowId, seasonNumber, episodeNumber) },
            onSuccess = {
                updateState { copy(isLoading = false) }
            },
            onError = { e ->
                when (e) {
                    is NoNetworkException -> {
                        updateState {
                            copy(
                                noInternetConnection = true,
                                error = null,
                                isLoading = false
                            )
                        }
                    }

                    else -> {
                        updateState { copy(error = e.message, isLoading = false) }
                    }
                }
            }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun fetchEpisodeDetails(tvShowId: Int, seasonNumber: Int, episodeNumber: Int) =
        coroutineScope {
            updateState { copy(isLoading = true) }

            val episodeDeferred = async {
                manageEpisodeDetails.getEpisodeDetails(
                    tvShowId,
                    seasonNumber,
                    episodeNumber
                )
            }
            val guestsDeferred = async {
                manageEpisodeDetails.getEpisodeGuestsOfHonor(
                    tvShowId,
                    seasonNumber,
                    episodeNumber
                )
            }
            val imagesDeferred = async { manageTvShowDetails.getTvShowImageUrls(tvShowId) }
            val trailerDeferred = async { manageTvShowDetails.getTvShowTrailer(tvShowId) }
            val episode = episodeDeferred.await()
            val guests = guestsDeferred.await()
            val images = imagesDeferred.await()
            val trailerUrl = trailerDeferred.await()

            updateState {
                copy(
                    episode = episode.toState(),
                    guestOfHonor = guests.map { actor -> actor.toActorUiModel() },
                    tvShowId = tvShowId,
                    imagesUrl = images,
                    trailerUrl = trailerUrl,
                )
            }

            fetchUserRating()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getCurrentUserRating(): Flow<Int> {
        return getUser.getLoggedInUser()
            .flatMapLatest { user ->
                flow {
                    try {
                        val rating = manageTvShowDetails.getEpisodesRate(
                            user.id,
                            route.seasonNumber,
                            route.episodeNumber
                        )
                        emit(rating)
                    } catch (e: Exception) {
                        emit(0)
                    }
                }
            }
    }

    private suspend fun submitEpisodeRating() {
        val isSendRateSuccess = manageEpisodeDetails.addTvEpisodeRate(
            tvShowId = route.tvShowId,
            episodeNumber = route.episodeNumber,
            seasonNumber = route.seasonNumber,
            rating = state.value.imdbRating.toFloat()
        )
        if (isSendRateSuccess) {
            emitEffect(EpisodeDetailsEffects.ShowSuccessSnackBar)
        } else {
            emitEffect(EpisodeDetailsEffects.ShowErrorSnackBar)
        }
    }

    private fun updateUserLoginState() {
        tryToCollect(
            callee = { checkUserLogin.isLoggedIn() },
            onCollect = { isLogged ->
                updateState {
                    copy(
                        isUserLoggedIn = isLogged
                    )
                }
            },
        )
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