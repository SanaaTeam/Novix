package com.sanaa.tvapp.presentation.screens.mediaDetails.actorScreen

import androidx.lifecycle.SavedStateHandle
import com.sanaa.tvapp.base.BaseViewModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toActorUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toDetailsUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper.toTvShowUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import usecase.ManageActorUseCase
import javax.inject.Inject

@HiltViewModel
class ActorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageActorDetails: ManageActorUseCase,
) : BaseViewModel<ActorScreenUiState, ActorScreenEffects>(
    initialState = ActorScreenUiState(),
    defaultDispatcher = Dispatchers.IO
), ActorsScreenInteractionListener {

    private val actorId: Int = checkNotNull(savedStateHandle["actorId"])

    init {
        loadDetails()
    }

    override fun onTvShowClicked(id: Int) {
        emitEffect(ActorScreenEffects.NavigateToSeriesDetails(id))
    }

    override fun onMovieClicked(id: Int) {
        emitEffect(ActorScreenEffects.NavigateToMovieDetails(id))
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
            onError = { e ->
                updateState { copy(isLoading = false) }
            }
        )
    }

    private suspend fun fetchActorDetails() = coroutineScope {
        val actorDeferred = async { manageActorDetails.getActorDetails(actorId) }
        val topMoviesDeferred = async { manageActorDetails.getActorTopMovies(actorId) }
        val topTvShowsDeferred = async { manageActorDetails.getActorTopTvShows(actorId) }
        val profilesDeferred = async { manageActorDetails.getProfileImages(actorId) }
        val galleryDeferred = async { manageActorDetails.getGalleryImages(actorId) }

        val actor = actorDeferred.await()
        val topMovies = topMoviesDeferred.await()
        val topTvShows = topTvShowsDeferred.await()
        val profiles = profilesDeferred.await()
        val gallery = galleryDeferred.await()

        updateState {
            copy(
                actor = actor.toActorUiModel(),
                topMovies = topMovies.map { m -> m.toDetailsUiModel() },
                topTvShows = topTvShows.map { t -> t.toTvShowUiModel() },
                profileImageUrls = profiles,
                galleryImageUrls = gallery
            )
        }
    }
}
