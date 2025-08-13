package com.sanaa.presentation.screen.actor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.MovieUiModel
import com.sanaa.presentation.model.mapper.toActorUiModel
import com.sanaa.presentation.model.mapper.toSeriesUiModel
import com.sanaa.presentation.model.mapper.toUiModel
import com.sanaa.presentation.navigation.ActorScreenRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import repository.SavedListsStatusProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageActorUseCase
import javax.inject.Inject

@HiltViewModel
class ActorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageActorDetails: ManageActorUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val savedListsStatusProvider: SavedListsStatusProvider,
) : BaseViewModel<ActorScreenUiState, ActorScreenEffects>(
    initialState = ActorScreenUiState(),
    defaultDispatcher = Dispatchers.IO
), ActorsScreenInteractionListener {
    private val actorId: Int = checkNotNull(
        savedStateHandle[ActorScreenRoute.ARG_ACTOR_ID]
    )

    init {
        updateUserLoggingStatus()
        loadDetails()
        viewModelScope.launch {
            savedListsStatusProvider.savedIds.collect { savedIds ->
                updateState {
                    copy(
                        topMovies = topMovies.map { movie ->
                            movie.copy(isSaved = savedIds.contains(movie.id))
                        }
                    )
                }
            }
        }
    }

    fun updateUserLoggingStatus() {
        tryToCollect(
            callee = { checkIfUserIsLoggedInUseCase.isLoggedIn() },
            onCollect = ::onCollectLoggedFlag
        )
    }

    private fun onCollectLoggedFlag(isLogged: Boolean) {
        updateState { copy(userIsLoggedIn = isLogged) }
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
        updateState { copy(showLoginBottomSheet = false) }
    }

    override fun onLoginButtonClick() {
        updateState { copy(showLoginBottomSheet = false) }
        emitEffect(ActorScreenEffects.NavigateToLogin)
    }

    override fun onSaveClicked(movie: MovieUiModel) {
        if (!state.value.userIsLoggedIn) {
            updateState { copy(showLoginBottomSheet = true) }
            return
        }

        if (movie.isSaved) {
            savedListsStatusProvider.markItemUnsaved(movie.id)
        } else {
            updateState {
                copy(
                    showSaveToListBottomSheet = true,
                    selectedMediaToSave = movie
                )
            }
        }
    }

    override fun onDismissSaveToListBottomSheet() {
        updateState { copy(showSaveToListBottomSheet = false, selectedMediaToSave = null) }
    }

    override fun onCreateNewListClick() {
        updateState { copy(showSaveToListBottomSheet = false, showAddListBottomSheet = true) }
    }

    override fun onDismissAddListBottomSheet() {
        updateState { copy(showAddListBottomSheet = false) }
    }

    override fun onRetryClicked() {
        updateState { copy(noInternetConnection = false, isLoading = true, error = null) }
        loadDetails()
    }

    private fun loadDetails() {
        updateState { copy(isLoading = true) }
        tryToExecute(
            callee = ::fetchActorDetails,
            onSuccess = { updateState { copy(isLoading = false) } },
            onError = { e -> updateState { copy(isLoading = false) } }
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
            copy(
                actor = actor.toActorUiModel(),
                topMovies = topMovies.map { m -> m.toUiModel() },
                topTvSeries = topSeries.map { s -> s.toSeriesUiModel() },
                profileImageUrls = profiles,
                galleryImageUrls = gallery
            )
        }
    }
}
