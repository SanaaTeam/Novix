package com.sanaa.presentation.screen.topShowsScreen

import androidx.lifecycle.SavedStateHandle
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.mapper.toState
import com.sanaa.presentation.navigation.TopTvShowsScreenRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import usecase.ManageActorUseCase
import javax.inject.Inject

@HiltViewModel
class TopTvShowsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageActorDetails: ManageActorUseCase,
) : BaseViewModel<TopTvShowsScreenUiState, Any>(
    initialState = TopTvShowsScreenUiState(),
    defaultDispatcher = Dispatchers.IO
), TopTvShowsScreenInteractionListener {
    private val route = TopTvShowsScreenRoute(
        actorId = checkNotNull(savedStateHandle["actorId"]),
    )

    init {
        loadDetails()
    }

    override fun onDismissBottomSheet() {
        updateState { copy(showLoginBottomSheet = false) }
    }

    override fun onRetryClicked() {
        updateState { copy(noInternetConnection = false, isLoading = true, error = null) }
        loadDetails()
    }

    private fun loadDetails() {
        updateState { copy(isLoading = true) }
        tryToExecute(
            block = ::fetchActorTopTvShows,
            onSuccess = {
                updateState { copy(isLoading = false) }
            },
            onError = { e ->
                when (e) {
                    is exceptions.NoNetworkException ->
                        updateState { copy(isLoading = false, noInternetConnection = true) }

                    else ->
                        updateState { copy(isLoading = false, error = e.message) }
                }
            }
        )
    }

    private suspend fun fetchActorTopTvShows() = coroutineScope {
        val topTvShowsDeferred = async { manageActorDetails.getActorTopTvShows(route.actorId) }
        val topTvShows = topTvShowsDeferred.await()
        updateState {
            copy(
                topTvShows = topTvShows.map { s -> s.toState() },
            )
        }
    }
}