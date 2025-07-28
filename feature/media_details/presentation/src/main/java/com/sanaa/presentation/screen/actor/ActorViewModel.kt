package com.sanaa.presentation.screen.actor

import androidx.lifecycle.SavedStateHandle
import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.toActorUiModel
import com.sanaa.presentation.model.toSeriesUiModel
import com.sanaa.presentation.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import usecase.ManageActorUseCase

@HiltViewModel
class ActorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageActorDetails: ManageActorUseCase
) : BaseViewModel<ActorScreenUiState, ActorScreenEffects>(
    initialState = ActorScreenUiState(),
    defaultDispatcher = Dispatchers.IO
), ActorsScreenInteractionListener {

    private val actorId: Int = checkNotNull(savedStateHandle["actorId"])

    init {
        loadDetails()
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

    override fun onSaveClicked() {
        updateState { it.copy(showLoginBottomSheet = true) }
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

    private suspend fun fetchActorDetails() {

        val actor = manageActorDetails.getActorDetails(actorId)
        val topMovies = manageActorDetails.getActorTopMovies(actorId)
        val topSeries = manageActorDetails.getActorTopTvSeries(actorId)
        val profiles = manageActorDetails.getProfileImages(actorId)
        val gallery = manageActorDetails.getGalleryImages(actorId)
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
