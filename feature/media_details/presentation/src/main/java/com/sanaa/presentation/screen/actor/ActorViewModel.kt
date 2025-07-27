package com.sanaa.presentation.screen.actor

import com.sanaa.presentation.details_base.BaseViewModel
import com.sanaa.presentation.model.toActorUiModel
import com.sanaa.presentation.model.toSeriesUiModel
import com.sanaa.presentation.model.toUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.ManageActorUseCase

class ActorViewModel(
    private val actorId: Int,
    private val manageActorDetails: ManageActorUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<ActorScreenUiState, ActorScreenEffects>(ActorScreenUiState(), dispatcher),
    ActorsScreenInteractionListener {

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

    override fun onLoginButtonClick() {
        updateState { it.copy(showLoginBottomSheet = false) }
        emitEffect(ActorScreenEffects.NavigateToLogin)
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
