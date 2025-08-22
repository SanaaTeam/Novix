package com.sanaa.presentation.screen.topMoviesScreen

import androidx.lifecycle.SavedStateHandle
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.MovieUiModel
import com.sanaa.presentation.model.mapper.toState
import com.sanaa.presentation.navigation.TopMoviesScreenRoute
import com.sanaa.presentation.screen.movieDetails.SnackData
import dagger.hilt.android.lifecycle.HiltViewModel
import exceptions.NoNetworkException
import exceptions.NovixAppException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import service.VodStringProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageActorUseCase
import javax.inject.Inject

@HiltViewModel
class TopMoviesScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageActorDetails: ManageActorUseCase,
    private val checkUserLogin: CheckIfUserIsLoggedInUseCase,
    private val stringProvider: VodStringProvider,
) : BaseViewModel<TopMoviesScreenUiState, TopMoviesScreenEffect>(
    initialState = TopMoviesScreenUiState(),
    defaultDispatcher = Dispatchers.IO
), TopMoviesScreenInteractionListener {
    private val route = TopMoviesScreenRoute(
        actorId = checkNotNull(savedStateHandle["actorId"]),
    )

    init {
        loadDetails()
        updateUserLoginState()
    }

    private fun updateUserLoginState() {
        tryToCollect(
            block = { checkUserLogin.isLoggedIn() },
            onCollect = ::onCollectLoggedFlag
        )
    }

    private fun onCollectLoggedFlag(isLogged: Boolean) {
        if (isLogged && state.value.showLoginBottomSheet) {
            updateState {
                copy(
                    showLoginBottomSheet = false,
                    showSaveToListBottomSheet = true,
                )
            }
        }
        updateState { copy(userIsLoggedIn = isLogged) }
    }

    override fun onDismissLoginBottomSheet() {
        updateState { copy(showLoginBottomSheet = false) }
    }

    override fun onSaveClicked(movie: MovieUiModel) {
        if (!state.value.userIsLoggedIn) {
            updateState { copy(selectedMediaToSave = movie, showLoginBottomSheet = true) }
            return
        } else {
            updateState { copy(showSaveToListBottomSheet = true, selectedMediaToSave = movie) }
        }
    }

    override fun onRetryClicked() {
        updateState { copy(noInternetConnection = false, isLoading = true) }
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

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
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

    override fun onBackClick() {
        emitEffect(TopMoviesScreenEffect.NavigateBack)
    }

    override fun onLoginButtonClick() {
        emitEffect(TopMoviesScreenEffect.NavigateToLogin)
    }

    override fun onMovieClick(id: Int) {
        emitEffect(TopMoviesScreenEffect.NavigateToMovieDetails(id))
    }
}