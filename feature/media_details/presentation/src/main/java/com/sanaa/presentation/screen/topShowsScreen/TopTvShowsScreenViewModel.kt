package com.sanaa.presentation.screen.topShowsScreen

import androidx.lifecycle.SavedStateHandle
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.mapper.toState
import com.sanaa.presentation.navigation.TopTvShowsScreenRoute
import com.sanaa.presentation.screen.movieDetails.SnackData
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NoNetworkException
import exceptions.NovixAppException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import service.VodStringProvider
import usecase.ManageActorUseCase
import javax.inject.Inject

@HiltViewModel
class TopTvShowsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageActorDetails: ManageActorUseCase,
    private val stringProvider: VodStringProvider,
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
        updateState { copy(noInternetConnection = false, isLoading = true) }
        loadDetails()
    }

    override fun onSnackDismissRequested() {
        updateState { copy(snackBarData = null) }
    }

    private fun loadDetails() {
        updateState { copy(isLoading = true) }
        tryToExecute(
            block = ::fetchActorTopTvShows,
            onSuccess = {
                updateState { copy(isLoading = false) }
            },
            onError = ::onErrorAccrue
        )
    }

    private fun onErrorAccrue(e: NovixAppException) {
        when (e) {
            is NoNetworkException -> {
                updateState {
                    copy(
                        noInternetConnection = true,
                        isLoading = false,
                        snackBarData = SnackData(
                            message = stringProvider.noInternetConnectionError,
                            isError = true,
                        )
                    )
                }
            }

            else -> {
                updateState {
                    copy(
                        noInternetConnection = false,
                        isLoading = false,
                        snackBarData = SnackData(
                            message = stringProvider.somethingWentWrongError,
                            isError = true
                        )
                    )
                }
            }
        }
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