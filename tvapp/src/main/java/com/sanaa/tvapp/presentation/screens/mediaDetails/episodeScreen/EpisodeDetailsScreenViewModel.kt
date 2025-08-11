package com.sanaa.tvapp.presentation.screens.mediaDetails.episodeScreen

import androidx.lifecycle.SavedStateHandle
import com.sanaa.tvapp.base.TvBaseViewModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toActorUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toEpisodeUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
) : TvBaseViewModel<EpisodeDetailsScreenUiState, EpisodeDetailsEffects>(
    initialState = EpisodeDetailsScreenUiState(),
    defaultDispatcher = dispatcher
), EpisodeDetailsInteractionListener {

    private val tvShowId: Int = 1399

        private val seriesId: Int = checkNotNull(savedStateHandle["seriesId"]) {
        "seriesId is required in SavedStateHandle"
    }
    private val seasonNumber: Int = checkNotNull(savedStateHandle["seasonNumber"]) {
        "seasonNumber is required in SavedStateHandle"
    }
    private val episodeNumber: Int = checkNotNull(savedStateHandle["episodeNumber"]) {
        "episodeNumber is required in SavedStateHandle"
    }

    init {
        loadEpisode(tvShowId, seasonNumber, episodeNumber)
//        updateUserLoginState()
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

    override fun onLoginButtonClick() {
        updateState { it.copy(showLoginBottomSheet = false) }
        emitEffect(EpisodeDetailsEffects.NavigateToLogin)
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
//        tryToExecute(
//            callee = ::submitEpisodeRating,
//            onError = { exception ->
//                updateState {
//                    it.copy(
//                        error = exception.message,
//                        showRateBottomSheet = false
//                    )
//                }
//            }
//        )
//        updateState {
//            it.copy(showRateBottomSheet = false)
//        }
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

    @OptIn(ExperimentalCoroutinesApi::class)
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
//            val ratingDeferred = async {
//                getCurrentUserRating()
//            }

            val episode = episodeDeferred.await()
            val guests = guestsDeferred.await()
            val images = imagesDeferred.await()
            val trailerUrl = trailerDeferred.await()
//            val currentEpisodesRating = ratingDeferred.await().first()

            updateState {
                it.copy(
                    episode = episode.toEpisodeUiModel(),
                    guestOfHonor = guests.map { actor -> actor.toActorUiModel() },
                    seriesId = seriesId,
                    backgroundImageUrl = images.firstOrNull().orEmpty(),
                    trailerUrl = trailerUrl,
//                    imdbRating = currentEpisodesRating
                )
            }
        }

//    @OptIn(ExperimentalCoroutinesApi::class)
//    private fun getCurrentUserRating(): Flow<Int> {
//        return getUser.getLoggedInUser()
//            .flatMapLatest { user ->
//                flow {
//                    try {
//                        val rating = manageTvSeriesDetails.getEpisodesRate(
//                            user.id,
//                            seasonNumber,
//                            episodeNumber
//                        )
//                        emit(rating)
//                    } catch (e: Exception) {
//                        emit(0)
//                    }
//                }
//            }
//    }

//    private suspend fun submitEpisodeRating() {
//        val isSendRateSuccess = manageEpisodeDetails.addTvEpisodeRate(
//            seriesId = tvShowId,
//            episodeNumber = episodeNumber,
//            seasonNumber = seasonNumber,
//            rating = state.value.imdbRating.toFloat()
//        )
//        if (isSendRateSuccess) {
//            emitEffect(EpisodeDetailsEffects.ShowSuccessSnackBar)
//        } else {
//            emitEffect(EpisodeDetailsEffects.ShowErrorSnackBar)
//        }
//    }

//    private fun updateUserLoginState() {
//        tryToCollect(
//            callee = { checkUserLogin.isLoggedIn() },
//            onCollect = { isLogged ->
//                updateState {
//                    it.copy(
//                        isUserLoggedIn = isLogged
//                    )
//                }
//            },
//        )
//    }

//    private fun promptLogin(type: LoginPromptType) {
//        updateState {
//            it.copy(
//                showLoginBottomSheet = true,
//                loginPromptType = type
//            )
//        }
//    }
}