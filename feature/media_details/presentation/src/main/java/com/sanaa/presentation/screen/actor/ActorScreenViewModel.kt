package com.sanaa.presentation.screen.actor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.MovieUiModel
import com.sanaa.presentation.model.mapper.toActorUiModel
import com.sanaa.presentation.model.mapper.toState
import com.sanaa.presentation.navigation.ActorScreenRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NoNetworkException
import exceptions.NovixAppException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import repository.SavedListsStatusProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageActorUseCase
import javax.inject.Inject

@HiltViewModel
class ActorScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageActorDetails: ManageActorUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val savedListsStatusProvider: SavedListsStatusProvider,
) : BaseViewModel<ActorScreenUiState, ActorScreenEffects>(
    initialState = ActorScreenUiState(),
    defaultDispatcher = Dispatchers.IO
), ActorsScreenInteractionListener {
    private val route = ActorScreenRoute(
        actorId = checkNotNull(savedStateHandle["actorId"]),
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
        emitEffect(ActorScreenEffects.NavigateToTopMovies(route.actorId))
    }

    override fun onTopShowsClicked() {
        emitEffect(ActorScreenEffects.NavigateToTopTvShows(route.actorId))
    }

    override fun onViewAllGalleryClicked() {
        emitEffect(ActorScreenEffects.NavigateToGallery(route.actorId))
    }

    override fun onTvShowClicked(id: Int) {
        emitEffect(ActorScreenEffects.NavigateToTvShowDetails(id))
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
            block = ::fetchActorDetails,
            onSuccess = {
                updateState { copy(isLoading = false) }
            },
            onError = ::onErrorAccrue
        )
    }

    private fun onErrorAccrue(e: NovixAppException) {
        when (e) {
            is NoNetworkException ->
                updateState { copy(isLoading = false, noInternetConnection = true) }

            else ->
                updateState { copy(isLoading = false, error = e.message) }
        }
    }

    private suspend fun fetchActorDetails() = coroutineScope {
        val actorDeferred = async { manageActorDetails.getActorDetails(route.actorId) }
        val topMoviesDeferred = async { manageActorDetails.getActorTopMovies(route.actorId) }
        val topTvShowsDeferred = async { manageActorDetails.getActorTopTvShows(route.actorId) }
        val profilesDeferred = async { manageActorDetails.getProfileImages(route.actorId) }
        val galleryDeferred = async { manageActorDetails.getGalleryImages(route.actorId) }

        val actor = actorDeferred.await()
        val topMovies = topMoviesDeferred.await()
        val topTvShows = topTvShowsDeferred.await()
        val profiles = profilesDeferred.await()
        val gallery = galleryDeferred.await()

        updateState {
            copy(
                actor = actor.toActorUiModel(),
                topMovies = topMovies.map { m -> m.toState() },
                topTvShows = topTvShows.map { s -> s.toState() },
                profileImageUrls = profiles,
                galleryImageUrls = gallery
            )
        }
    }
}
