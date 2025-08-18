package com.sanaa.presentation.screen.topMoviesScreen

import androidx.lifecycle.SavedStateHandle
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.MovieUiModel
import com.sanaa.presentation.model.mapper.toState
import com.sanaa.presentation.navigation.TopMoviesScreenRoute
import com.sanaa.presentation.screen.actor.ActorScreenEffects
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NoNetworkException
import exceptions.NovixAppException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import repository.SavedListsStatusProvider
import usecase.ManageActorUseCase
import javax.inject.Inject

@HiltViewModel
class TopMoviesScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageActorDetails: ManageActorUseCase,
    private val savedListsStatusProvider: SavedListsStatusProvider,
) : BaseViewModel<TopMoviesScreenUiState, ActorScreenEffects>(
    initialState = TopMoviesScreenUiState(),
    defaultDispatcher = Dispatchers.IO
), TopMoviesScreenInteractionListener {
    private val route = TopMoviesScreenRoute(
        actorId = checkNotNull(savedStateHandle["actorId"]),
    )

    init {
        loadDetails()
    }

    override fun onDismissBottomSheet() {
        updateState { copy(showLoginBottomSheet = false) }
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

    override fun onRetryClicked() {
        updateState { copy(noInternetConnection = false, isLoading = true, error = null) }
        loadDetails()
    }

    private fun loadDetails() {
        updateState { copy(isLoading = true) }
        tryToExecute(
            block = ::fetchActorTopMovies,
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

    private suspend fun fetchActorTopMovies() = coroutineScope {
        val topMoviesDeferred = async { manageActorDetails.getActorTopMovies(route.actorId) }

        val topMovies = topMoviesDeferred.await()

        updateState {
            copy(topMovies = topMovies.map { m -> m.toState() })
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
}