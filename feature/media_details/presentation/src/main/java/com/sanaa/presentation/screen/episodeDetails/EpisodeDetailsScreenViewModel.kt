package com.sanaa.presentation.screen.episodeDetails

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.mapper.toActorUiModel
import com.sanaa.presentation.model.mapper.toEpisodeUiModel
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
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.ManageEpisodeDetailsUseCase
import usecase.ManageTvSeriesUseCase
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUser: GetLoggedInUserUseCase,
    private val checkUserLogin: CheckIfUserIsLoggedInUseCase,
    private val manageEpisodeDetails: ManageEpisodeDetailsUseCase,
    private val manageTvSeriesDetails: ManageTvSeriesUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<EpisodeDetailsScreenUiState, EpisodeDetailsEffects>(
    initialState = EpisodeDetailsScreenUiState(),
    defaultDispatcher = dispatcher
), EpisodeDetailsInteractionListener {
    private val route: EpisodeDetailsScreenRoute = savedStateHandle.toRoute()

    init {
        loadEpisode(route.seriesId, route.seasonNumber, route.episodeNumber)
        updateUserLoginState()
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

    override fun onSavedClick(seriesId: Int) {
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
        loadEpisode(state.value.seriesId, 0, 0)
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

    private fun onErrorAccrue(throwable: Throwable) {
        updateState {
            copy(
                error = throwable.message,
                showRateBottomSheet = false
            )
        }
    }

    private fun loadEpisode(seriesId: Int, seasonNumber: Int, episodeNumber: Int) {
        tryToExecute(
            callee = { fetchEpisodeDetails(seriesId, seasonNumber, episodeNumber) },
            onSuccess = {
                updateState { copy(isLoading = false) }
            },
            onError = { e ->
                if (e is NoNetworkException) {
                    updateState {
                        copy(
                            noInternetConnection = true,
                            error = null,
                            isLoading = false
                        )
                    }
                } else {
                    updateState { copy(error = error, isLoading = false) }
                }
            }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun fetchEpisodeDetails(seriesId: Int, seasonNumber: Int, episodeNumber: Int) =
        coroutineScope {
            updateState { copy(isLoading = true) }

            val episodeDeferred = async {
                manageEpisodeDetails.getEpisodeDetails(
                    seriesId,
                    seasonNumber,
                    episodeNumber
                )
            }
            val guestsDeferred = async {
                manageEpisodeDetails.getEpisodeGuestsOfHonor(
                    seriesId,
                    seasonNumber,
                    episodeNumber
                )
            }
            val imagesDeferred = async { manageTvSeriesDetails.getTvSeriesImages(seriesId) }
            val trailerDeferred = async { manageTvSeriesDetails.getTvSeriesTrailer(seriesId) }
            val ratingDeferred = async {
                getCurrentUserRating()
            }

            val episode = episodeDeferred.await()
            val guests = guestsDeferred.await()
            val images = imagesDeferred.await()
            val trailerUrl = trailerDeferred.await()
            val currentEpisodesRating = ratingDeferred.await().first()

            updateState {
                copy(
                    episode = episode.toEpisodeUiModel(),
                    guestOfHonor = guests.map { actor -> actor.toActorUiModel() },
                    seriesId = seriesId,
                    imagesUrl = images,
                    trailerUrl = trailerUrl,
                    imdbRating = currentEpisodesRating
                )
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getCurrentUserRating(): Flow<Int> {
        return getUser.getLoggedInUser()
            .flatMapLatest { user ->
                flow {
                    try {
                        val rating = manageTvSeriesDetails.getEpisodesRate(
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
            seriesId = route.seriesId,
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