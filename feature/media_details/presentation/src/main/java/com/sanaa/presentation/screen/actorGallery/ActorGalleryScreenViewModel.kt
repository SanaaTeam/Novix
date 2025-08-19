package com.sanaa.presentation.screen.actorGallery

import androidx.lifecycle.SavedStateHandle
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.navigation.ActorGalleryScreenRoute
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
class ActorGalleryScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageActorDetails: ManageActorUseCase,
    private val stringProvider: VodStringProvider,
) : BaseViewModel<ActorGalleryScreenUiState, ActorGalleryScreenEffects>(
    initialState = ActorGalleryScreenUiState(),
    defaultDispatcher = Dispatchers.IO
), ActorGalleryInteractionListener {
    private val route = ActorGalleryScreenRoute(
        actorId = checkNotNull(savedStateHandle["actorId"]),
    )

    init {
        loadDetails()
    }

    private fun loadDetails() {
        updateState { copy(isLoading = true) }
        tryToExecute(
            block = ::fetchActorGalleryImages,
            onSuccess = { updateState { copy(isLoading = false) } },
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

    override fun onRetryClicked() {
        updateState { copy(noInternetConnection = false, isLoading = true) }
        loadDetails()
    }

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
    }

    override fun onBackClicked() {
        emitEffect(ActorGalleryScreenEffects.NavigateBack)
    }

    private suspend fun fetchActorGalleryImages() = coroutineScope {
        val galleryDeferred = async { manageActorDetails.getGalleryImages(route.actorId) }

        val gallery = galleryDeferred.await()

        updateState {
            copy(
                galleryImageUrls = gallery
            )
        }
    }
}