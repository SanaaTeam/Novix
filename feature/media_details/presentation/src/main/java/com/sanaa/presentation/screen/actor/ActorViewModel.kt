package com.sanaa.presentation.screen.actor

import androidx.lifecycle.SavedStateHandle
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.mapper.toActorUiModel
import com.sanaa.presentation.model.mapper.toSeriesUiModel
import com.sanaa.presentation.model.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageActorUseCase
import javax.inject.Inject

@HiltViewModel
class ActorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageActorDetails: ManageActorUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase
) : BaseViewModel<ActorScreenUiState, ActorScreenEffects>(
    initialState = ActorScreenUiState(),
    defaultDispatcher = Dispatchers.IO
), ActorsScreenInteractionListener {

    private val actorId: Int = checkNotNull(savedStateHandle["actorId"])

    init {
        updateUserLoggingStatus()
        loadDetails()
    }

    fun updateUserLoggingStatus() {
        tryToCollect(
            callee = { checkIfUserIsLoggedInUseCase.isLoggedIn() },
            onCollect = {isLogged ->
                updateState {
                    it.copy(
                        userIsLoggedIn = isLogged
                    )
                }
            },
        )
    }

    override fun onBackClicked() {
        emitEffect(ActorScreenEffects.NavigateBack)
    }

    override fun onTopMoviesClicked() {
        emitEffect(ActorScreenEffects.NavigateToTopMovies(actorId))
    }

    override fun onTopSeriesClicked() {
        emitEffect(ActorScreenEffects.NavigateToTopSeries(actorId))
    }

    override fun onViewAllGalleryClicked() {
        emitEffect(ActorScreenEffects.NavigateToGallery(actorId))
    }

    override fun onSeriesClicked(id: Int) {
        emitEffect(ActorScreenEffects.NavigateToSeriesDetails(id))
    }

    override fun onMovieClicked(id: Int) {
        emitEffect(ActorScreenEffects.NavigateToMovieDetails(id))
    }

    override fun onDismissBottomSheet() {
        updateState { it.copy(showLoginBottomSheet = false) }
    }

    override fun onLoginButtonClick() {
        updateState { it.copy(showLoginBottomSheet = false) }
        emitEffect(ActorScreenEffects.NavigateToLogin)
    }

    override fun onSaveClicked() {
        if (!state.value.userIsLoggedIn) {
            updateState { it.copy(showLoginBottomSheet = true) }
        }
    }

    override fun onRetryClicked() {
        updateState { it.copy(noInternetConnection = false, isLoading = true, error = null) }
        loadDetails()
    }

    private fun loadDetails() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            callee = ::fetchActorDetails,
            onSuccess = {
                updateState { it.copy(isLoading = false) }
            },
            onError = { e ->
                updateState { it.copy(isLoading = false) }
            }
        )
    }

    private suspend fun fetchActorDetails() = coroutineScope {
        val actorDeferred = async { manageActorDetails.getActorDetails(actorId) }
        val topMoviesDeferred = async { manageActorDetails.getActorTopMovies(actorId) }
        val topSeriesDeferred = async { manageActorDetails.getActorTopTvSeries(actorId) }
        val profilesDeferred = async { manageActorDetails.getProfileImages(actorId) }
        val galleryDeferred = async { manageActorDetails.getGalleryImages(actorId) }

        val actor = actorDeferred.await()
        val topMovies = topMoviesDeferred.await()
        val topSeries = topSeriesDeferred.await()
        val profiles = profilesDeferred.await()
        val gallery = galleryDeferred.await()

        updateState {
            it.copy(
                actor = actor.toActorUiModel(),
                topMovies = topMovies.map { m -> m.toUiModel() },
                topTvSeries = topSeries.map { s -> s.toSeriesUiModel() },
                profileImageUrls = profiles,
                galleryImageUrls = gallery
            )
        }
    }
}
