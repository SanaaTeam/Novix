package com.sanaa.presentation.screen.episodeDetails

import androidx.lifecycle.SavedStateHandle
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.mapper.toActorUiModel
import com.sanaa.presentation.model.mapper.toState
import com.sanaa.presentation.screen.movieDetails.LoginPromptType
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NoNetworkException
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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

    private val tvShowId: Int = checkNotNull(savedStateHandle["tvShowId"])
    private val seasonNumber: Int = checkNotNull(savedStateHandle["seasonNumber"])
    private val episodeNumber: Int = checkNotNull(savedStateHandle["episodeNumber"])

    init {
        fetchEpisodeDetails()
        updateUserLoginState()
    }

    private fun <T> fetchData(
        block: suspend () -> T,
        onSuccess: (T) -> Unit,
    ) {
        tryToExecute(
            block = block,
            onSuccess = onSuccess,
            onError = ::handleError
        )
    }

    private fun handleError(exception: NovixAppException) {
        when (exception) {
            is NoNetworkException -> {
                updateState { copy(noInternetConnection = true, isLoading = false, error = null) }
            }

            else -> {
                updateState { copy(error = exception.message, isLoading = false) }
            }
        }
    }

    fun fetchEpisodeDetails() {
        updateState { copy(isLoading = true, noInternetConnection = false, error = null) }

        fetchEpisode()
        fetchGuests()
        fetchImages()
        fetchTrailer()
    }

    private fun fetchEpisode() {
        fetchData(
            block = {
                manageEpisodeDetails.getEpisodeDetails(
                    tvShowId,
                    seasonNumber,
                    episodeNumber
                )
            },
            onSuccess = { episode ->
                updateState { copy(episode = episode.toState(), isLoading = false) }
            }
        )
    }

    private fun fetchGuests() {
        fetchData(
            block = {
                manageEpisodeDetails.getEpisodeGuestsOfHonor(
                    tvShowId,
                    seasonNumber,
                    episodeNumber
                )
            },
            onSuccess = { guests ->
                updateState { copy(guestOfHonor = guests.map { it.toActorUiModel() }) }
            }
        )
    }

    private fun fetchImages() {
        fetchData(
            block = { manageTvShowDetails.getTvShowImageUrls(tvShowId) },
            onSuccess = { images ->
                updateState { copy(imagesUrl = images) }
            }
        )
    }

    private fun fetchTrailer() {
        fetchData(
            block = { manageTvShowDetails.getTvShowTrailer(tvShowId) },
            onSuccess = { trailerUrl ->
                updateState { copy(trailerUrl = trailerUrl) }
            }
        )
    }


    override fun onRetryLoadDetails() = fetchEpisodeDetails()
    override fun onSnackDismissRequested() = updateState { copy(snackBarData = null) }
    override fun onDismissBottomSheet() = updateState { copy(showLoginBottomSheet = false) }
    override fun onSavedClick(tvShowId: Int) {
        if (!state.value.isUserLoggedIn) promptLogin(LoginPromptType.BOOKMARK)
    }


    override fun onBackClick() = emitEffect(EpisodeDetailsEffects.NavigateBack)
    override fun onPlayTrailerClick() =
        emitEffect(EpisodeDetailsEffects.PlayTrailer(state.value.trailerUrl))

    override fun onGenreTypeClick(genreId: Int) =
        emitEffect(EpisodeDetailsEffects.NavigateToActorDetails(genreId))

    override fun onCastClick(actorId: Int) =
        emitEffect(EpisodeDetailsEffects.NavigateToActorDetails(actorId))

    override fun onLoginButtonClick() {
        updateState { copy(showLoginBottomSheet = false) }
        emitEffect(EpisodeDetailsEffects.NavigateToLogin)
    }

    private fun updateUserLoginState() {
        tryToCollect(
            block = { checkUserLogin.isLoggedIn() },
            onCollect = { isLogged -> updateState { copy(isUserLoggedIn = isLogged) } }
        )
    }

    private fun promptLogin(type: LoginPromptType) {
        updateState { copy(showLoginBottomSheet = true, loginPromptType = type) }
    }
}
