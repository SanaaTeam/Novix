package com.sanaa.presentation.screen.actorGallery

import androidx.lifecycle.SavedStateHandle
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.navigation.ActorGalleryScreenRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import usecase.ManageActorUseCase
import javax.inject.Inject

@HiltViewModel
class ActorGalleryScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageActorDetails: ManageActorUseCase,
) : BaseViewModel<ActorGalleryScreenUiState, Any>(
    initialState = ActorGalleryScreenUiState(),
    defaultDispatcher = Dispatchers.IO
) {
    private val route = ActorGalleryScreenRoute(
        actorId = checkNotNull(savedStateHandle["actorId"]),
    )

    init {
        loadDetails()
    }

    private fun loadDetails() {
        updateState { copy(isLoading = true) }
        tryToExecute(
            callee = ::fetchActorGalleryImages,
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