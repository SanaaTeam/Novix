package com.sanaa.presentation.screen.episodeDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.screen.movieDetails.LoginPromptType
import dagger.hilt.android.lifecycle.HiltViewModel
import com.sanaa.presentation.model.mapper.toActorUiModel
import com.sanaa.presentation.model.mapper.toEpisodeUiModel
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<EpisodeDetailsScreenUiState, EpisodeDetailsEffects>(
    initialState = EpisodeDetailsScreenUiState(),
    defaultDispatcher = dispatcher
), EpisodeDetailsInteractionListener {

    private val seriesId: Int = checkNotNull(savedStateHandle["seriesId"])
    private val seasonNumber: Int = checkNotNull(savedStateHandle["seasonNumber"])
    private val episodeNumber: Int = checkNotNull(savedStateHandle["episodeNumber"])

    init {
        loadEpisode(seriesId, seasonNumber, episodeNumber)
        updateUserStatus()
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
            updateState {
                it.copy(
                    showLoginBottomSheet = true,
                    loginPromptType = LoginPromptType.BOOKMARK
                )
            }
        }

    }

    override fun onDismissBottomSheet() {
        updateState { it.copy(showLoginBottomSheet = false) }
    }

    override fun onLoginButtonClick() {
        updateState { it.copy(showLoginBottomSheet = false) }
        emitEffect(EpisodeDetailsEffects.NavigateToLogin)
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

    override fun onRetryLoadDetails() {
        updateState { it.copy(noInternetConnection = false, isLoading = true, error = null) }
        loadEpisode(state.value.seriesId, 0, 0)
    }

    override fun onRatingChanged(newRating: Int) {
        updateState { it.copy(imdbRating = newRating) }
    }

    override fun onDismissRateBottomSheet() {
        updateState { it.copy(showRateBottomSheet = false) }
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

    private fun loadEpisode(seriesId: Int, seasonNumber: Int, episodeNumber: Int) {
        tryToExecute(
            callee = { fetchEpisodeDetails(seriesId, seasonNumber, episodeNumber) },
            onSuccess = {
                updateState { it.copy(isLoading = false) }
            },
            onError = { e ->
                if (e is NoNetworkException) {
                    updateState {
                        it.copy(
                            noInternetConnection = true,
                            error = null,
                            isLoading = false
                        )
                    }
                } else {
                    updateState { it.copy(error = it.error, isLoading = false) }
                }
            }
        )
    }

    private suspend fun fetchEpisodeDetails(seriesId: Int, seasonNumber: Int, episodeNumber: Int) =
        coroutineScope {
            updateState { it.copy(isLoading = true) }

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
                runCatching {
                    val userId = getUser.getLoggedInUser().id
                    val ratedEpisodes = manageTvSeriesDetails.getEpisodesRate(userId)
                    ratedEpisodes.find {
                        it.seasonNumber == seasonNumber && it.number == episodeNumber
                    }?.rating ?: 0
                }.getOrElse { 0 }
            }

            val episode = episodeDeferred.await()
            val guests = guestsDeferred.await()
            val images = imagesDeferred.await()
            val trailerUrl = trailerDeferred.await()
            val currentEpisodesRating = ratingDeferred.await()

            updateState {
                it.copy(
                    episode = episode.toEpisodeUiModel(),
                    guestOfHonor = guests.map { actor -> actor.toActorUiModel() },
                    seriesId = seriesId,
                    imagesUrl = images,
                    trailerUrl = trailerUrl,
                    imdbRating = currentEpisodesRating
                )
            }
        }


    private suspend fun addRate() {
        val isSendRateSuccess = manageEpisodeDetails.addTvEpisodeRate(
            seriesId = seriesId,
            episodeNumber = episodeNumber,
            seasonNumber = seasonNumber,
            rating = state.value.imdbRating.toFloat()
        )
        if (isSendRateSuccess) {
            emitEffect(EpisodeDetailsEffects.ShowSuccessSnackBar)
        } else {
            emitEffect(EpisodeDetailsEffects.ShowErrorSnackBar)
        }
    }

    private suspend fun getUserState() {
        val isUserLoggedIn = checkUserLogin.isLoggedIn()
        updateState { it.copy(isUserLoggedIn = isUserLoggedIn) }
    }

    fun updateUserStatus(){
        tryToExecute(callee = ::getUserState)
    }
}