package com.sanaa.presentation.screen.episodeDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.mapper.toActorUiModel
import com.sanaa.presentation.model.mapper.toState
import com.sanaa.presentation.screen.movieDetails.LoginPromptType
import com.sanaa.presentation.screen.movieDetails.SnackData
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NoNetworkException
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import service.VodStringProvider
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
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<EpisodeDetailsScreenUiState, EpisodeDetailsEffects>(
    initialState = EpisodeDetailsScreenUiState(),
    defaultDispatcher = dispatcher
), EpisodeDetailsInteractionListener {

    private val tvShowId: Int = checkNotNull(savedStateHandle["tvShowId"]) {
        "tvShowId is required in SavedStateHandle"
    }
    private val seasonNumber: Int = checkNotNull(savedStateHandle["seasonNumber"]) {
        "seasonNumber is required in SavedStateHandle"
    }
    private val episodeNumber: Int = checkNotNull(savedStateHandle["episodeNumber"]) {
        "episodeNumber is required in SavedStateHandle"
    }

    init {
        fetchEpisodeDetails(tvShowId, seasonNumber, episodeNumber)
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
        loadEpisode(tvShowId, seasonNumber, episodeNumber)
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
    private fun getCurrentUserRating(): Flow<Int> {
        return getUser.getLoggedInUser()
            .flatMapLatest { user ->
                flow {
                    try {
                        val rating = manageTvShowDetails.getEpisodesRate(
                            user.id,
                            seasonNumber,
                            episodeNumber
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
            tvShowId = this@EpisodeDetailsScreenViewModel.tvShowId,
            episodeNumber = episodeNumber,
            seasonNumber = seasonNumber,
            rating = state.value.imdbRating.toFloat()
        )
        if (isSendRateSuccess) {
            updateState {
                copy(
                    snackBarData = SnackData(
                        message = stringProvider.deleteRatingSuccess, isError = false
                    )
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

    fun fetchEpisodeDetails(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
    ) {
        fetchEpisode(tvShowId, seasonNumber, episodeNumber)
        fetchGuests(tvShowId, seasonNumber, episodeNumber)
        fetchImages(tvShowId)
        fetchTrailer(tvShowId)
        fetchUserRating()
    }

    private fun fetchEpisode(tvShowId: Int, seasonNumber: Int, episodeNumber: Int) {
        tryToExecute(
            callee = {
                manageEpisodeDetails.getEpisodeDetails(tvShowId, seasonNumber, episodeNumber)
            },
            onSuccess = { episode ->
                updateState {
                    copy(episode = episode.toState(), isLoading = false)
                }
            },
            onError = ::onErrorAccrue
        )
    }

    private fun fetchGuests(tvShowId: Int, seasonNumber: Int, episodeNumber: Int) {
        tryToExecute(
            callee = {
                manageEpisodeDetails.getEpisodeGuestsOfHonor(tvShowId, seasonNumber, episodeNumber)
            },
            onSuccess = { guests ->
                updateState {
                    copy(guestOfHonor = guests.map { it.toActorUiModel() })
                }
            },
            onError = ::onErrorAccrue
        )
    }

    private fun fetchImages(tvShowId: Int) {
        tryToExecute(
            callee = { manageTvShowDetails.getTvShowImageUrls(tvShowId) },
            onSuccess = { images ->
                updateState { copy(imagesUrl = images) }
            },
            onError = ::onErrorAccrue
        )
    }

    private fun fetchTrailer(tvShowId: Int) {
        tryToExecute(
            callee = { manageTvShowDetails.getTvShowTrailer(tvShowId) },
            onSuccess = { trailerUrl ->
                updateState { copy(trailerUrl = trailerUrl) }
            },
            onError = ::onErrorAccrue
        )
    }

    override fun onSnackDismissRequested() {
        updateState {
            copy(snackBarData = null)
        }
    }
}